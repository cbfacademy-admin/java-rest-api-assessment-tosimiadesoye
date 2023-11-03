package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.OpenAI.ChatGPTResponse;

import com.cbfacademy.apiassessment.userData.UserData;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;

import java.io.IOException;

import java.util.*;

import com.google.gson.Gson;

import static com.cbfacademy.apiassessment.OpenAI.ChatGPTClient.chatGPT;
import static com.cbfacademy.apiassessment.json.ReadAndWriteToJson.*;

@Component
public class PersonalisedFitnessPlan implements MealPlanner, CalculateCalories, WorkoutPlanner {
    private static final File MEAL_DATA_FILE_PATH = new File("src/main/resources/meals.json");
    private static final File WORKOUT_DATA_FILE_PATH = new File("src/main/resources/workout.json");
    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);


    public static void main(String args[]) {

        try {
            var workout = new PersonalisedFitnessPlan()
                    .generateWorkout("Running");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public double calculateBMR(String gender, double weight, double height, int age) {
        double basalMetabolicRate;
        if ("female".equalsIgnoreCase(gender)) {
            basalMetabolicRate = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        } else if ("male".equalsIgnoreCase(gender)) {
            basalMetabolicRate = 88.364 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            throw new RuntimeException("Invalid gender: " + gender);
        }
        return basalMetabolicRate;
    }

    @Override
    public double calcDailyKcalConsumption(String gender, double weight,
                                           double height, int age,
                                           ActivityLevel activityLevel) {
        double BMR = calculateBMR(gender, weight, height, age);

        return BMR * activityLevel.getMultiplier();

    }

    @Override
    public List<Ideas> mealType(String mealType) throws IOException {
        List<MealIdeas> allMeals = readJsonFile(MEAL_DATA_FILE_PATH, MealIdeas.class);

        List<Ideas> ideas = allMeals.stream().filter(meal -> meal.getMealType()
                        .equalsIgnoreCase(mealType)).findFirst()
                .map(MealIdeas::getIdeas).orElse(Collections.emptyList());

        return ideas;
    }

    @Override
    public Ideas generateMealPlan(String mealType) throws IOException {
        int min = 0;
        int max = mealType(mealType).size();
        int randomNum = randomNumber(max, min);
        return mealType(mealType).get(randomNum);
    }

    @Override
    public HashMap<String, Ideas> generateFullDayMeal() throws IOException {
        var breakfast = generateMealPlan("breakfast");
        var lunch = generateMealPlan("lunch");
        var dinner = generateMealPlan("dinner");
        HashMap<String, Ideas> fullDaysMeal = new HashMap<>();
        fullDaysMeal.put("breakfast", breakfast);
        fullDaysMeal.put("lunch", lunch);
        fullDaysMeal.put("dinner", dinner);
        return fullDaysMeal;
    }

    public int randomNumber(int max, int min) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }


    @Override
    public List<Workout> generateWorkout(String goal) throws IOException {
        List<Workout> workout = new ArrayList<>();
        List<Workout> allWorkout = readJsonFile(WORKOUT_DATA_FILE_PATH, Workout.class);

        var found = false;
        for (Workout w : allWorkout) {
            List<String> suitableForList = w.getSuitable_for();
            if (suitableForList.contains(goal)) {
                found = true;
                workout.add(w);
            }
        }
        if (!found) {

            Workout newWorkout = readChatGPTResponse(goal);
            if (newWorkout != null) {
                logger.info(String.valueOf(newWorkout));
                workout.add(newWorkout);
                addWorkout(newWorkout);
            }
        }
        return workout;

    }

    @Override
    public void addWorkout(Workout workout) throws IOException {
        writeToJsonFile(workout, WORKOUT_DATA_FILE_PATH, Workout.class);
    }

    public Workout readChatGPTResponse(String goal) {
        ChatGPTResponse chatGPT = chatGPT(goal);

        Gson gson = new Gson();
//        String response = gson.toJson(chatGPT, ChatGPTResponse.class);
//        logger.info(String.valueOf(chatGPT));
        var response = chatGPT.getChoices().get(0).getMessage().getContent();
        logger.info(response);
//       var r = gson.toJson(response, Workout.class);
//       logger.info(r);
        return null;
    }


}