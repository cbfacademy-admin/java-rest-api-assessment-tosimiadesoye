/**
 * The PersonalisedFitnessPlan class provides functionality for generating personalized fitness plans,
 * including meal plans and workout recommendations based on user inputs and preferences.
 * <p>
 * It implements the MealPlanner, CalculateCalories, and WorkoutPlanner interfaces to encapsulate
 * the respective functionalities related to meal planning, calorie calculation, and workout generation.
 * <p>
 * The class uses external JSON files to store meal and workout data, and it incorporates ChatGPT for
 * workout recommendations if the desired workout goal is not found in the existing data.
 *
 * @author Tosimi
 * @version 1.0
 */

package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.OpenAI.ChatGPTResponse;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

import java.io.IOException;

import java.util.*;

import com.google.gson.Gson;

import static com.cbfacademy.apiassessment.OpenAI.ChatGPTClient.chatGPT;
import static com.cbfacademy.apiassessment.json.ReadAndWriteToJson.*;

@Component
public class PersonalisedFitnessPlan implements MealPlanner, CalculateCalories, WorkoutPlanner {
    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);
    private final File mealDataFilePath;
    private final File workOutDataFilePath;

    /**
     * Constructs a PersonalisedFitnessPlan instance with the specified file paths for meal and workout data.
     *
     * @param mealDataFilePath    the file path for meal data that can be found in application.properties
     * @param workOutDataFilePath the file path for workout data that can be found in application.properties
     */
    public PersonalisedFitnessPlan(@Value("${mealDataFilePath}") String mealDataFilePath,
            @Value("${workOutDataFilePath}") String workOutDataFilePath) {
        this.mealDataFilePath = new File(mealDataFilePath);
        this.workOutDataFilePath = new File(workOutDataFilePath);
    }

    /**
     * Calculates the Basal Metabolic Rate (BMR) based on the user's gender, weight, height, and age.
     *
     * @param gender User's gender ("male" or "female")
     * @param weight User's weight in kilograms
     * @param height User's height in centimeters
     * @param age    User's age in years
     * @return Resting calories representing the Basal Metabolic Rate (BMR)
     * @throws RuntimeException if the provided gender is neither "male" nor "female"
     */
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

    /**
     * Calculates the Total Daily Energy Expenditure (TDEE) based on the user's gender, weight, height, age,
     * and activity level.
     *
     * @param gender        User's gender ("male" or "female")
     * @param weight        User's weight in kilograms
     * @param height        User's height in centimeters
     * @param age           User's age in years
     * @param activityLevel User's activity level (e.g., sedentary, moderate, active)
     * @return Total calories a user burns per day (TDEE)
     */
    @Override
    public double totalDailyEnergyExpenditure(String gender, double weight,
            double height, int age,
            ActivityLevel activityLevel) {
        double BMR = calculateBMR(gender, weight, height, age);

        return BMR * activityLevel.getMultiplier();

    }

    /**
     * Retrieves a list of meal ideas for a specific meal type (e.g., breakfast, lunch, or dinner).
     *
     * @param mealType The type of meal (e.g., breakfast, lunch, or dinner)
     * @return A list of meal ideas corresponding to the specified meal type
     * @throws IOException if there is a problem reading the input file
     */
    @Override
    public List<Ideas> mealType(String mealType) throws IOException {
        List<MealIdeas> allMeals = readJsonFile(mealDataFilePath, MealIdeas.class);

        List<Ideas> ideas = allMeals.stream().filter(meal -> meal.getMealType()
                .equalsIgnoreCase(mealType)).findFirst()
                .map(MealIdeas::getIdeas).orElse(Collections.emptyList());

        return ideas;
    }

    /**
     * Generates a meal idea for a specific meal type (e.g., Grilled Chicken Salad for lunch).
     *
     * @param mealType The type of meal (e.g., breakfast, lunch, or dinner)
     * @return A randomly selected meal idea for the specified meal type
     * @throws IOException if there is a problem reading the input file
     */
    @Override
    public Ideas generateMealPlan(String mealType) throws IOException {
        int min = 0;
        int max = mealType(mealType).size();
        int randomNum = randomNumber(max, min);
        return mealType(mealType).get(randomNum);
    }

    /**
     * Generates a full-day meal plan consisting of breakfast, lunch, and dinner.
     *
     * @return A HashMap containing meal ideas for breakfast, lunch, and dinner
     * @throws IOException if there is a problem reading the input file
     */
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

    /**
     * Provides functionality to generate a list of workouts based on a user's fitness goal.
     * If no matching workouts are found in the JSON file, it queries ChatGPT, retrieves the
     * ChatGPT response, and updates the JSON file with the response.
     *
     * @param goal a user's fitness goal (e.g., build muscle)
     * @return a list of workouts tailored to the fitness goal
     * @throws IOException if there is a problem reading the input file or updating it
     */
    @Override
    public List<Workout> generateWorkout(String goal) throws IOException {
        List<Workout> workout = new ArrayList<>();
        List<Workout> allWorkout = readJsonFile(workOutDataFilePath, Workout.class);

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

    /**
     * Adds a workout generated by ChatGPT to the JSON file.
     *
     * @param workout the workout to be added to the JSON file
     * @throws IOException if there is a problem updating the JSON file
     */
    @Override
    public void addWorkout(Workout workout) throws IOException {
        writeToJsonFile(workout, workOutDataFilePath, Workout.class);
    }

    /**
     * Queries ChatGPT with a user's fitness goal to generate a workout idea response.
     *
     * @param goal a user's fitness goal given to ChatGPT
     * @return the generated workout idea response from ChatGPT
     */
    public Workout readChatGPTResponse(String goal) {
        ChatGPTResponse chatGPT = chatGPT(goal);

        var response = chatGPT.getChoices().get(0).getMessage().getContent();

        Gson gson = new Gson();
        Workout workout = gson.fromJson(response, Workout.class);
        logger.info(String.valueOf(workout));
        return workout;
    }

}