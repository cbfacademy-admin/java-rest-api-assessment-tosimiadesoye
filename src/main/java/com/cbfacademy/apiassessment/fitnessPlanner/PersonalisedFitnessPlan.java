package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.userData.UserData;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;

import java.io.IOException;

import java.util.*;
import static com.cbfacademy.apiassessment.json.ReadAndWriteToJson.*;

@Component
public class PersonalisedFitnessPlan implements MealPlanner, CalculateCalories, WorkoutPlanner {
    private static final File MEAL_DATA_FILE_PATH = new File("src/main/resources/meals.json");
    private static final File WORKOUT_DATA_FILE_PATH = new File("src/main/resources/workout.json");
    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);
    UserData user;
    ActivityLevel activityLevel;
    Double basalMetabolicRate;

    public PersonalisedFitnessPlan(UserData user, ActivityLevel activityLevel) {
        this.user = user;
        this.activityLevel = activityLevel;
    }

    @Override
    public double calculateBMR(String gender, double weight, double height, int age) {
        if ("female".equalsIgnoreCase(gender)) {
            basalMetabolicRate = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        } else if ("male".equalsIgnoreCase(gender)) {
            basalMetabolicRate = 88.364 + (13.397 * weight) + (4.799 * height) - (5.677 * weight);
        } else {
            throw new RuntimeException("Invalid gender: " + user.getGender());
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

    public List<Ideas> mealType(String type) throws IOException {
        List<MealIdeas> allMeals = readJsonFile(MEAL_DATA_FILE_PATH, MealIdeas.class);

        List<Ideas> ideas = allMeals.stream().filter(meal -> meal.getMealType()
                .equalsIgnoreCase(type)).findFirst()
                .map(MealIdeas::getIdeas).orElse(Collections.emptyList());

        logger.info(ideas.toString());
        return ideas;
    }

//    add dietary preference
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

//    todo - check if goal is found, if not return create goal request
//    return a personalised message if goal is not find e.g we are still updating our db send a request for fitness goal
    @Override
    public List<Workout> generateWorkout(String goal) throws IOException {
        List<Workout> workout = new ArrayList<>();
        List<Workout> allWorkout = readJsonFile(WORKOUT_DATA_FILE_PATH, Workout.class);
        for (Workout w : allWorkout) {
            List<String> suitableForList = w.getSuitable_for();
            if (suitableForList.contains(goal)) {

                workout.add(w);
            }
        }
        return workout;
    }

//   todo - createWorkout plan, track progress

}