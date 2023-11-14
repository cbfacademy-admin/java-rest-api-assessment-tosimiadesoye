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

import com.cbfacademy.apiassessment.json.ReadAndWriteToJson;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;

import java.io.IOException;

import java.util.*;

import com.google.gson.Gson;

import static com.cbfacademy.apiassessment.OpenAI.ChatGPTClient.chatGPT;

@Component
public class PersonalisedFitnessPlan implements MealPlanner, CalculateCalories, WorkoutPlanner {

    // Constants for the Harris-Benedict Equation
    private static final double MEN_BMR_CONSTANT = 88.362;
    private static final double MEN_WEIGHT_COEFFICIENT = 13.397;
    private static final double MEN_HEIGHT_COEFFICIENT = 4.799;
    private static final double MEN_AGE_COEFFICIENT = 5.677;

    private static final double WOMEN_BMR_CONSTANT = 447.593;
    private static final double WOMEN_WEIGHT_COEFFICIENT = 9.247;
    private static final double WOMEN_HEIGHT_COEFFICIENT = 3.098;
    private static final double WOMEN_AGE_COEFFICIENT = 4.330;

    private final ReadAndWriteToJson readAndWriteToJson;
    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);

    public PersonalisedFitnessPlan(ReadAndWriteToJson readAndWriteToJson){
        this.readAndWriteToJson = readAndWriteToJson;
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
    public double calculateBMR(Gender gender, double weight, double height, int age) {
        double basalMetabolicRate;

        if (!(weight > 0 && height > 0 && age > 0)) {
            throw new IllegalArgumentException("Weight, height, and age must be non zero or a non-negative values.");
        }

        switch (gender) {
            case FEMALE:
                basalMetabolicRate = WOMEN_BMR_CONSTANT + (WOMEN_WEIGHT_COEFFICIENT * weight) + (WOMEN_HEIGHT_COEFFICIENT * height) - (WOMEN_AGE_COEFFICIENT * age);
                break;
            case MALE:
                basalMetabolicRate = MEN_BMR_CONSTANT + (MEN_WEIGHT_COEFFICIENT * weight) + (MEN_HEIGHT_COEFFICIENT * height) - (MEN_AGE_COEFFICIENT * age);
                break;
            default:
                throw new IllegalArgumentException("Invalid gender: " + gender);
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
    public double calculateTDEE(Gender gender, double weight,
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
    public List<Ideas> getMealsFromType(String mealType, File mealDataFile) throws IOException {

            return fetchAllMeals(mealDataFile).stream().filter(meal -> meal.getMealType()
                            .equalsIgnoreCase(mealType)).findFirst()
                    .map(MealIdeas::getIdeas).orElse(Collections.emptyList());

    }


    public List<MealIdeas> fetchAllMeals( File mealDataFile) throws IOException {
        return readAndWriteToJson.readJsonFile(mealDataFile, MealIdeas.class);
    }

    /**
     * Generates a meal idea for a specific meal type (e.g., Grilled Chicken Salad for lunch).
     *
     * @param mealType The type of meal (e.g., breakfast, lunch, or dinner)
     * @return A randomly selected meal idea for the specified meal type
     * @throws IOException if there is a problem reading the input file
     */

    @Override
    public Ideas generateMealIdea(String mealType, File mealDataFile) throws IOException {
        int min = 0;
        int max = getMealsFromType(mealType,mealDataFile ).size();
        int randomNum = getRandomNumber(max, min);
        return getMealsFromType(mealType, mealDataFile).get(randomNum);
    }

    /**
     * Generates a full-day meal plan consisting of breakfast, lunch, and dinner.
     *
     * @return A HashMap containing meal ideas for breakfast, lunch, and dinner
     * @throws IOException if there is a problem reading the input file
     */
    @Override
    public HashMap<String, Ideas> generateFullDayMealIdea(File mealDataFile) throws IOException {
        Ideas breakfast = generateMealIdea("breakfast", mealDataFile);
        Ideas lunch = generateMealIdea("lunch", mealDataFile);
        Ideas dinner = generateMealIdea("dinner", mealDataFile);
        HashMap<String, Ideas> fullDaysMeal = new HashMap<>();
        fullDaysMeal.put("breakfast", breakfast);
        fullDaysMeal.put("lunch", lunch);
        fullDaysMeal.put("dinner", dinner);
        return fullDaysMeal;
    }

    public int getRandomNumber(int max, int min) {
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
    public List<Workout> generateWorkout(String goal, File workOutDataFile) throws IOException {
        List<Workout> workout = new ArrayList<>();
        List<Workout> allWorkout = readAndWriteToJson.readJsonFile(workOutDataFile, Workout.class);

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
                addWorkout(newWorkout,workOutDataFile);
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
    public void addWorkout(Workout workout, File workOutDataFile) throws IOException {
        readAndWriteToJson.writeToJsonFile(workout, workOutDataFile, Workout.class);
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