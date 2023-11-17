package com.cbfacademy.apiassessment.service;

import com.cbfacademy.apiassessment.fitnessPlanner.*;
import com.cbfacademy.apiassessment.fitnessPlanner.HarrisBenedictCalculator.ActivityLevel;
import com.cbfacademy.apiassessment.fitnessPlanner.HarrisBenedictCalculator.Gender;
import com.cbfacademy.apiassessment.json.ReadAndWriteToJson;
import org.springframework.stereotype.Service;
import com.cbfacademy.apiassessment.fitnessPlanner.MealPlanner.MealType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class PersonalFitnessServices {

    private final ReadAndWriteToJson readAndWriteToJson = new ReadAndWriteToJson();
    private final PersonalisedFitnessPlan personalisedFitnessPlan = new PersonalisedFitnessPlan(readAndWriteToJson);

    public long getBMR(String gender, double weight, double height, int age) {
        Gender userGender = Gender.fromString(gender);
        return personalisedFitnessPlan.calculateBMR(userGender, weight, height, age);

    }

    public long getTDEE(String gender, double weight,
            double height, int age, String activityLevel) {
        ActivityLevel activeLevel = ActivityLevel.fromString(activityLevel);
        Gender userGender = Gender.fromString(gender);
        return personalisedFitnessPlan.calculateTDEE(userGender, weight,
                height, age, activeLevel);
    }

    public Idea getMealPlan(String mealType) {

        try {
            MealType meal = MealType.fromString(mealType);
            return personalisedFitnessPlan.generateMealIdea(meal);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public HashMap<String, Idea> getDailyMeal() {
        try {
            return personalisedFitnessPlan.generateFullDayMealIdea();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Workout> getWorkoutPlan(String goal) {
        try {

            String userGoal = goal.replace(" ", "_").toLowerCase();
            return personalisedFitnessPlan.generateWorkout(userGoal);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
