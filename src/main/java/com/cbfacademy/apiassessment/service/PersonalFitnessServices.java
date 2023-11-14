package com.cbfacademy.apiassessment.service;

import com.cbfacademy.apiassessment.fitnessPlanner.*;
import com.cbfacademy.apiassessment.fitnessPlanner.CalculateCalories.ActivityLevel;
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

    public double getRestingCalories(CalculateCalories.Gender gender, double weight, double height, int age) {
        return personalisedFitnessPlan.calculateBMR(gender, weight, height, age);
    }

    public Double getTDEE(CalculateCalories.Gender gender, double weight,
                          double height, int age, ActivityLevel activityLevel) {

        return personalisedFitnessPlan.calculateTDEE(gender, weight,
                height, age, activityLevel);
    }

    public Ideas getMealPlan(String mealType) {
        try {
            if (mealType.equals(MealType.BREAKFAST.name()) || mealType.equals(MealType.LUNCH.name()) || mealType.equals(MealType.DINNER.name())) {
                throw new IllegalArgumentException("Invalid mealType. Expected value is BREAKFAST or LUNCH, or DINNER.");
            } else {
                return personalisedFitnessPlan.generateMealIdea(mealType, MEAL_DATA_FILE_PATH);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
