package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.json.ReadAndWriteToJson;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import java.util.*;
import java.util.stream.Stream;

import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@Description(value = "Meal Planner Test")
public class MealPlannerTest {
    private static final File DATA_FILE_PATH = new File("src/test/resources/data.json");

    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);
    MealPlanner mealPlanner;
    ReadAndWriteToJson readAndWriteToJson;

    File tempFile;

    public static Stream<Arguments> mealTypeString() {
        return Stream.of(
                Arguments.of("BREAKFAST"),
                Arguments.of("LUNCH"),
                Arguments.of("DINNER"));
    }

    public List<MealIdea> getMealIdea() {
        Idea idea = new Idea("Oatmeal with Berries", "breakfast", Arrays.asList("Vegetarian",
                "Vegan",
                "Pescatarian",
                "Keto"), "");
        Idea idea2 = new Idea("Oatmeal with Berries", "lunch", Arrays.asList("Vegetarian",
                "Vegan",
                "Pescatarian",
                "Keto"), "");
        Idea idea3 = new Idea("Oatmeal with Berries", "dinner", Arrays.asList("Vegetarian",
                "Vegan",
                "Pescatarian",
                "Keto"), "");

        List<Idea> ideaListBreakfast = new ArrayList<>();
        ideaListBreakfast.add(idea);
        List<Idea> ideaListLunch = new ArrayList<>();
        ideaListLunch.add(idea2);
        List<Idea> ideaListDinner = new ArrayList<>();
        ideaListDinner.add(idea3);
        MealIdea mealIdea = new MealIdea("breakfast", ideaListBreakfast);
        MealIdea mealIdea1 = new MealIdea("lunch", ideaListLunch);
        MealIdea mealIdea2 = new MealIdea("dinner", ideaListDinner);
        List<MealIdea> mealIdeaList = new ArrayList<>();
        mealIdeaList.add(mealIdea);
        mealIdeaList.add(mealIdea1);
        mealIdeaList.add(mealIdea2);
        return mealIdeaList;
    }

    @BeforeEach
    public void setUp() {
        try {

            if (!DATA_FILE_PATH.exists()) {
                DATA_FILE_PATH.mkdir();
            }

            tempFile = File.createTempFile("prefix", "ext", DATA_FILE_PATH);
            tempFile.deleteOnExit();
            readAndWriteToJson = Mockito.mock(ReadAndWriteToJson.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mealPlanner = new MealPlanner(readAndWriteToJson) {
        };
    }

    @ParameterizedTest
    @MethodSource("mealTypeString")
    @Description(value = "getMealsFromType() returns a list of given meal")
    public void getMealsFromType_ReturnsListOfMeal(MealPlanner.MealType meal) {
        try {

            when(mealPlanner.fetchAllMeals()).thenReturn(getMealIdea());
            List<Idea> actual = mealPlanner.getMealsFromType(meal);

            assertFalse(actual.isEmpty(), "The actual list of meals should not be empty");

            // assert that result type is the same as the meal
            actual.forEach(idea2 -> assertEquals(meal.name().toLowerCase(), idea2.getType().toLowerCase()));

            assertEquals(1, actual.size(),
                    "The size of the actual list should be the same as the expected list");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @MethodSource("mealTypeString")
    @Description(value = "generateMealIdea() returns a meal idea for breakfast, lunch and dinner")
    public void generateMealIdea_ReturnsIdea(MealPlanner.MealType meal) {
        try {

            when(mealPlanner.fetchAllMeals()).thenReturn(getMealIdea());
            Idea result = mealPlanner.generateMealIdea(meal);
            // assert that the return value is not null
            assertNotNull(result);

            // assert that result type is the same as the meal
            assertEquals(result.getType().toLowerCase(), meal.name().toLowerCase());

            assertTrue(meal.name().toLowerCase().contains(result.getType().toLowerCase()));
            // assert that getSuitable_for is not null
            assertNotNull(result.getSuitable_for());
            // assert that getSuitable_for is not empty
            assertFalse(result.getSuitable_for().isEmpty());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @MethodSource("mealTypeString")
    @Description(value = "generateFullDayMealIdea() returns breakfast, meal and dinner")
    public void generateFullDayMealIdea_ReturnsIdeaForFullDayMeal(String meal) {
        try {
            when(mealPlanner.fetchAllMeals()).thenReturn(getMealIdea());
            HashMap<String, Idea> result = mealPlanner.generateFullDayMealIdea();
            logger.info(meal + result);

            assertFalse(result.isEmpty(), "The result of meal Idea is not empty");

            // assert that result contains breakfast, lunch and dinner
            assertTrue(result.containsKey(meal.toLowerCase()));

            // assert that suitable_for is not empty
            assertFalse(result.get(meal.toLowerCase()).getSuitable_for().isEmpty());

            // assert that the content of the hashmap is not null
            assertNotNull(result.get(meal.toLowerCase()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Description(value = "fetchAllMeals() returns breakfast, meal and dinner")
    public void fetchAllMeals_ReturnsMeals() {
        try {
            when(mealPlanner.fetchAllMeals()).thenReturn(getMealIdea());
            List<MealIdea> result = mealPlanner.fetchAllMeals();
            logger.info(result.toString());

            assertFalse(result.isEmpty());
            assertEquals(3, result.size());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
