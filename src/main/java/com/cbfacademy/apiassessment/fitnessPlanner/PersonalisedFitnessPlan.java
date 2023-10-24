package com.cbfacademy.apiassessment.fitnessPlanner;


import com.cbfacademy.apiassessment.userData.UserData;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.cbfacademy.apiassessment.json.ReadAndWriteToJson.*;

public class PersonalisedFitnessPlan implements MealPlanner, CalculateCalories, WorkoutPlanner {
    private static final File DATA_FILE_PATH = new File("src/main/resources/meals.json");
    UserData user;
    ActivityLevel activityLevel;
    Double basalMetabolicRate;

    public PersonalisedFitnessPlan(UserData user, ActivityLevel activityLevel) {
        this.user = user;
        this.activityLevel = activityLevel;
    }

    @Override
    public double CalculateBMR() {
        if ("female".equalsIgnoreCase(user.getGender())) {
            basalMetabolicRate = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        } else if ("male".equalsIgnoreCase(user.getGender())) {
            basalMetabolicRate = 88.364 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            throw new RuntimeException("Invalid gender: " + user.getGender());
        }
        return basalMetabolicRate;
    }

    @Override
    public double CalcDailyKcalConsumption() {
        return basalMetabolicRate * activityLevel.getMultiplier();
    }



    public List<MealIdeas> getMealType(String type) throws IOException {
        List<MealIdeas> allMeals = readJsonFile(DATA_FILE_PATH, MealIdeas.class);
        return allMeals.stream().filter(meal -> meal.getName().equalsIgnoreCase(type)).collect(Collectors.toList());
    }

    @Override
    public List<MealIdeas> breakfast(String breakfast) throws IOException {
        return getMealType(breakfast);
    }

    @Override
    public List<MealIdeas> lunch(String lunch) throws IOException {
        return getMealType(lunch);
    }

    @Override
    public List<MealIdeas> dinner(String dinner) throws IOException {
        return getMealType(dinner);
    }

    @Override
    public String generateMealPlan() {
        return null;
    }

    @Override
    public String generateWorkout() {
        return null;
    }
}
