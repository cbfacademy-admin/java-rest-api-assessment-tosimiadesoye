/**
 *The PersonalisedFitnessPlan class offers functionalities for generating personalized fitness plans,
 * encompassing meal plans and workout recommendations based on user inputs and preferences.
 * It extends the MealPlanner class and implements interfaces like CalculateCalories and WorkoutPlanner to
 * encapsulate meal planning, calorie calculation, and workout generation functionalities.
 *<p>
 * Key Features:
 * Meal and Workout Data: Utilizes external JSON files to store meal and workout data,
 * and integrates ChatGPT for workout recommendations when desired goals are unavailable in the existing data.
 *<p>
 * Calorie Calculation: Calculates Basal Metabolic Rate (BMR) and Total Daily Energy Expenditure
 * (TDEE) based on user's gender, weight, height, age, and activity level.
 *<p>
 * Workout Generation: Provides functionality to generate workout recommendations tailored to a user's fitness goal.
 * Queries ChatGPT for workout ideas if no matching workouts are found in the stored data.
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
public class PersonalisedFitnessPlan extends MealPlanner implements HarrisBenedictCalculator, WorkoutPlanner {

    private static final double MEN_BMR_CONSTANT = 88.362;
    private static final double MEN_WEIGHT_COEFFICIENT = 13.397;
    private static final double MEN_HEIGHT_COEFFICIENT = 4.799;
    private static final double MEN_AGE_COEFFICIENT = 5.677;

    private static final double WOMEN_BMR_CONSTANT = 447.593;
    private static final double WOMEN_WEIGHT_COEFFICIENT = 9.247;
    private static final double WOMEN_HEIGHT_COEFFICIENT = 3.098;
    private static final double WOMEN_AGE_COEFFICIENT = 4.330;
    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);
    private final ReadAndWriteToJson readAndWriteToJson;

    private static final File WORKOUT_DATA_FILE_PATH = new File("src/main/resources/workout.json");

    public PersonalisedFitnessPlan(ReadAndWriteToJson readAndWriteToJson) {
        super(readAndWriteToJson);
        this.readAndWriteToJson = readAndWriteToJson;
    }

    @Override
    public long calculateBMR(Gender gender, double weight, double height, int age) {

        long basalMetabolicRate;

        if (!(weight > 0 && height > 0 && age > 0)) {
            throw new IllegalArgumentException("Weight, height, and age must not be zero or a negative value.");
        }

        basalMetabolicRate = switch (gender) {
            case FEMALE ->
                    (long) (WOMEN_BMR_CONSTANT + (WOMEN_WEIGHT_COEFFICIENT * weight) + (WOMEN_HEIGHT_COEFFICIENT * height) - (WOMEN_AGE_COEFFICIENT * age));
            case MALE ->
                    (long) (MEN_BMR_CONSTANT + (MEN_WEIGHT_COEFFICIENT * weight) + (MEN_HEIGHT_COEFFICIENT * height) - (MEN_AGE_COEFFICIENT * age));
        };
        return  Math.round(basalMetabolicRate);
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
    public long calculateTDEE(Gender gender, double weight,
                              double height, int age,
                              ActivityLevel activityLevel) {
        double BMR = calculateBMR(gender, weight, height, age);

        return Math.round(BMR * activityLevel.getMultiplier());

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


        boolean found = false;
        for (Workout w : fetchWorkouts()) {
            List<String> suitableForList = w.getSuitable_for();
            if (suitableForList.contains(goal)) {
                found = true;
                workout.add(w);
            }
        }
        if (!found) {
            Workout newWorkout = readChatGPTResponse(goal);
            if (newWorkout != null) {
                logger.info("newWorkout" + newWorkout);
                workout.add(newWorkout);
                addWorkout(newWorkout);
            }
        }
        return workout;

    }

    /**
     * Retrieves a list of workout
     * @return A list of workout
     * @throws IOException if there is a problem reading the input file
     */
    public List<Workout> fetchWorkouts() throws IOException {
        return readAndWriteToJson.readJsonFile(WORKOUT_DATA_FILE_PATH, Workout.class);
    }

    /**
     * Adds a workout generated by ChatGPT to the JSON file.
     *
     * @param workout the workout to be added to the JSON file
     * @throws IOException if there is a problem updating the JSON file
     */
    @Override
    public void addWorkout(Workout workout) throws IOException {
        readAndWriteToJson.writeToJsonFile(workout, WORKOUT_DATA_FILE_PATH, Workout.class);
    }

    /**
     * Queries ChatGPT with a user's fitness goal to generate a workout idea response.
     *
     * @param goal a user's fitness goal given to ChatGPT
     * @return the generated workout idea response from ChatGPT
     */
    public Workout readChatGPTResponse(String goal) {
        ChatGPTResponse chatGPT = chatGPT(goal);

        String response = chatGPT.getChoices().get(0).getMessage().getContent();

        Gson gson = new Gson();
        Workout workout = gson.fromJson(response, Workout.class);
        logger.info("workout" + workout);
        return workout;
    }

}