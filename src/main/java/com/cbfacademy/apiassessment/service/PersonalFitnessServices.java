package com.cbfacademy.apiassessment.service;

import com.cbfacademy.apiassessment.fitnessPlanner.*;
import com.cbfacademy.apiassessment.fitnessPlanner.HarrisBenedictCalculator.ActivityLevel;
import com.cbfacademy.apiassessment.fitnessPlanner.HarrisBenedictCalculator.Gender;
import com.cbfacademy.apiassessment.json.ReadAndWriteToJson;
import org.springframework.stereotype.Service;
import com.cbfacademy.apiassessment.fitnessPlanner.MealPlanner.MealType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@Service
public class PersonalFitnessServices {

    private static final File MEAL_DATA_FILE_PATH = new File("src/main/resources/meals.json");
    private static final File WORKOUT_DATA_FILE_PATH = new File("src/main/resources/workout.json");
    private final ReadAndWriteToJson readAndWriteToJson = new ReadAndWriteToJson();
    private final PersonalisedFitnessPlan personalisedFitnessPlan = new PersonalisedFitnessPlan(readAndWriteToJson);

    public long getRestingCalories(String gender, double weight, double height, int age) {
        Gender userGender = Gender.fromString(gender);
        return personalisedFitnessPlan.calculateBMR(userGender, weight, height, age);

    }

    public long getTDEE(String gender, double weight,
                        double height, int age, ActivityLevel activityLevel) {

       Gender userGender = Gender.fromString(gender);
        return personalisedFitnessPlan.calculateTDEE(userGender, weight,
                height, age, activityLevel);
    }

    public Ideas getMealPlan(String mealType) {

        try {
            MealType meal = MealType.fromString(mealType);
                return personalisedFitnessPlan.generateMealIdea(meal, MEAL_DATA_FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException( e);
        }

    }

    public HashMap<String, Ideas> getDailyMeal() {
        try {
            return personalisedFitnessPlan.generateFullDayMealIdea(MEAL_DATA_FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Workout> getWorkoutPlan(String goal) {
        try {
            return personalisedFitnessPlan.generateWorkout(goal, WORKOUT_DATA_FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
