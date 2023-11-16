/**
 * The MealPlanner abstract class serves as a base for meal planning functionalities.
 * It contains methods to generate meal ideas for breakfast, lunch, and dinner, as well as to create full-day meal
 * plans based on stored meal data.
 *<p>
 * Key Features:
 * Meal Generation: Generates meal ideas for specific meal types (breakfast, lunch, dinner)
 * and full-day meal plans using stored meal data.
 *
 * @author Tosimi
 * @version 1.0
 */
package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.json.ReadAndWriteToJson;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class MealPlanner {
    private final ReadAndWriteToJson readAndWriteToJson;
    private static final String BREAKFAST = "breakfast";
    private static final String LUNCH = "lunch";
    private static final String DINNER = "dinner";

    private static final File MEAL_DATA_FILE_PATH = new File("src/main/resources/meals.json");

    public MealPlanner(ReadAndWriteToJson readAndWriteToJson) {
        this.readAndWriteToJson = readAndWriteToJson;
    }

    /**
     * Retrieves a list of meal Idea for a specific meal type (e.g., breakfast,
     * lunch, or dinner).
     *
     * @param mealType The type of meal (e.g., breakfast, lunch, or dinner)
     * @return A list of meal Idea corresponding to the specified meal type
     * @throws IOException if there is a problem reading the input file
     */

    public List<Idea> getMealsFromType(MealType mealType) throws IOException {

        return fetchAllMeals().stream().filter(meal -> meal.getMealType()
                .equalsIgnoreCase(mealType.name())).findFirst()
                .map(MealIdea::getIdea).orElse(Collections.emptyList());

    }

    /**
     * Retrieves a list of meal Idea
     * @return A list of meal Idea
     * @throws IOException if there is a problem reading the input file
     */
    public List<MealIdea> fetchAllMeals() throws IOException {
        return readAndWriteToJson.readJsonFile(MEAL_DATA_FILE_PATH, MealIdea.class);
    }

    /**
     * Generates a meal idea for a specific meal type (e.g., Grilled Chicken Salad
     * for lunch).
     *
     * @param mealType The type of meal (e.g., breakfast, lunch, or dinner)
     * @return A randomly selected meal idea for the specified meal type
     * @throws IOException if there is a problem reading the input file
     */

    public Idea generateMealIdea(MealType mealType) throws IOException {
        int min = 0;
        int max = getMealsFromType(mealType).size() - 1;
        int randomNum = getRandomNumber(max, min);
        return getMealsFromType(mealType).get(randomNum);
    }

    /**
     * Generates a full-day meal plan consisting of breakfast, lunch, and dinner.
     *
     * @return A HashMap containing meal Idea for breakfast, lunch, and dinner
     * @throws IOException if there is a problem reading the input file
     */

    public HashMap<String, Idea> generateFullDayMealIdea() throws IOException {
        Idea breakfast = generateMealIdea(MealType.BREAKFAST);
        Idea lunch = generateMealIdea(MealType.LUNCH);
        Idea dinner = generateMealIdea(MealType.DINNER);
        HashMap<String, Idea> fullDaysMeal = new HashMap<>();
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

        public static MealType fromString(String value) {
            try {
                return MealType.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Invalid meal type. Accepted values are 'BREAKFAST' or 'LUNCH' or 'DINNER'. Received: '" + value
                                + "'");
            }
        }

    }

}
