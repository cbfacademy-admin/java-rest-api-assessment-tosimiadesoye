package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.userData.UserData;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;



import static com.cbfacademy.apiassessment.json.ReadAndWriteToJson.*;

public class PersonalisedFitnessPlan implements MealPlanner, CalculateCalories, WorkoutPlanner {
    private static final File DATA_FILE_PATH = new File("src/main/resources/meals.json");
    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);
    UserData user;
    ActivityLevel activityLevel;
    Double basalMetabolicRate;

    public PersonalisedFitnessPlan(UserData user, ActivityLevel activityLevel) {
        this.user = user;
        this.activityLevel = activityLevel;
    }

    public static void main(String args[]) {
        UserData user = null;
        try {
            HashMap<String, Ideas> meal = new PersonalisedFitnessPlan(user, CalculateCalories.ActivityLevel.VERY_ACTIVE).generateFullDayMeal();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public List<Ideas> getMealType(String type) throws IOException {
        List<MealIdeas> allMeals = readJsonFile(DATA_FILE_PATH, MealIdeas.class);

        List<Ideas> ideas = allMeals.stream().filter(meal -> meal.getMealType()
                        .equalsIgnoreCase(type)).findFirst()
                .map(MealIdeas::getIdeas).orElse(Collections.emptyList());

        logger.info(ideas.toString());
        return ideas;
    }

    @Override
    public Ideas generateMealPlan(String meal) throws IOException {
        int min = 0;
        int max = getMealType(meal).size();
        int randomNum = randomNumber(max, min);
        return getMealType(meal).get(randomNum);
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
    public String generateWorkout() {
//        logger.info(String.valueOf(getMealType(meal).get(randomNum)));
        return null;
    }

}
