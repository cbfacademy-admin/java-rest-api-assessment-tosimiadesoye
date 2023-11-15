package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.json.ReadAndWriteToJson;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class MealPlanner {
   private final ReadAndWriteToJson readAndWriteToJson;
    private static final String BREAKFAST  = "breakfast";
    private static final String LUNCH = "lunch";
    private static final String DINNER = "dinner";

    public MealPlanner(ReadAndWriteToJson readAndWriteToJson) {
        this.readAndWriteToJson = readAndWriteToJson;
    }

    /**
     * Retrieves a list of meal ideas for a specific meal type (e.g., breakfast, lunch, or dinner).
     *
     * @param mealType The type of meal (e.g., breakfast, lunch, or dinner)
     * @return A list of meal ideas corresponding to the specified meal type
     * @throws IOException if there is a problem reading the input file
     */

    public List<Ideas> getMealsFromType(MealType mealType, File mealDataFile) throws IOException {

        return fetchAllMeals(mealDataFile).stream().filter(meal -> meal.getMealType()
                        .equalsIgnoreCase(mealType.name())).findFirst()
                .map(MealIdeas::getIdeas).orElse(Collections.emptyList());

    }

    public List<MealIdeas> fetchAllMeals(File mealDataFile) throws IOException {
        return readAndWriteToJson.readJsonFile(mealDataFile, MealIdeas.class);
    }

    /**
     * Generates a meal idea for a specific meal type (e.g., Grilled Chicken Salad for lunch).
     *
     * @param mealType The type of meal (e.g., breakfast, lunch, or dinner)
     * @return A randomly selected meal idea for the specified meal type
     * @throws IOException if there is a problem reading the input file
     */


    public Ideas generateMealIdea(MealType mealType, File mealDataFile) throws IOException {
        int min = 0;
        int max = getMealsFromType(mealType, mealDataFile).size() - 1;
        int randomNum = getRandomNumber(max, min);
        return getMealsFromType(mealType, mealDataFile).get(randomNum);
    }

    /**
     * Generates a full-day meal plan consisting of breakfast, lunch, and dinner.
     *
     * @return A HashMap containing meal ideas for breakfast, lunch, and dinner
     * @throws IOException if there is a problem reading the input file
     */

    public HashMap<String, Ideas> generateFullDayMealIdea(File mealDataFile) throws IOException {
        Ideas breakfast = generateMealIdea(MealType.BREAKFAST, mealDataFile);
        Ideas lunch = generateMealIdea(MealType.LUNCH, mealDataFile);
        Ideas dinner = generateMealIdea(MealType.DINNER, mealDataFile);
        HashMap<String, Ideas> fullDaysMeal = new HashMap<>();
        fullDaysMeal.put(BREAKFAST, breakfast);
        fullDaysMeal.put(LUNCH, lunch);
        fullDaysMeal.put(DINNER, dinner);
        return fullDaysMeal;
    }

    public int getRandomNumber(int max, int min) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public enum MealType {
        BREAKFAST,
        LUNCH,
        DINNER;

        public static MealType fromString(String value){
            try {
                return MealType.valueOf(value.toUpperCase());
            }catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Invalid meal type. Accepted values are 'BREAKFAST' or 'LUNCH' or 'DINNER'. Received: '" + value + "'");
            }
        }
        
    }

}


