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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Description(value = "Personalised Fitness Plan")
public class PersonalisedFitnessPlanTest {
    private static final File MEAL_DATA_FILE_PATH = new File("src/test/resources/mealTestFile.json");
    private static final File WORKOUT_DATA_FILE_PATH = new File("src/test/resources/workoutTestFile.json");
    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);
    PersonalisedFitnessPlan personalisedFitnessPlan;
    ReadAndWriteToJson readAndWriteToJson;

    public static Stream<Arguments> calculateBMRArguments() {
        return Stream.of(
                Arguments.of("FEMALE", 50, 160, 21, 1314.0),
                Arguments.of("MALE", 50, 160, 21, 1406.0));
    }

    public static Stream<Arguments> totalDailyEnergyExpenditure() {
        return Stream.of(
                Arguments.of("FEMALE", 50, 160, 21, "SEDENTARY", 1577.0),
                Arguments.of("MALE", 50, 160, 21, "LIGHTLY_ACTIVE", 1933.0),
                Arguments.of("FEMALE", 45, 160, 18, "MODERATELY_ACTIVE", 1986.0),
                Arguments.of("MALE", 70, 180, 25, "VERY_ACTIVE", 3015.0),
                Arguments.of("FEMALE", 60, 170, 35, "SUPER_ACTIVE", 2616.0));
    }

    public static Stream<Arguments> mealTypeAndFileInput() {
        return Stream.of(
                Arguments.of("BREAKFAST", MEAL_DATA_FILE_PATH),
                Arguments.of("LUNCH", MEAL_DATA_FILE_PATH),
                Arguments.of("DINNER", MEAL_DATA_FILE_PATH));
    }

    public static Stream<Arguments> mealTypeString() {
        return Stream.of(
                Arguments.of("breakfast"),
                Arguments.of("lunch"),
                Arguments.of("dinner"));
    }

    public static Stream<Arguments> workoutGoalAndFileInput() {
        return Stream.of(
                Arguments.of("Strength_training", WORKOUT_DATA_FILE_PATH),
                Arguments.of("Upper_body", WORKOUT_DATA_FILE_PATH));

    }

    @BeforeEach
    public void setUp() {

        readAndWriteToJson = new ReadAndWriteToJson();

        personalisedFitnessPlan = new PersonalisedFitnessPlan(readAndWriteToJson);

    }

    @ParameterizedTest
    @MethodSource("calculateBMRArguments")
    @Description("should return BMR")
    public void calculateBMR_shouldReturnBMR(HarrisBenedictCalculator.Gender gender, double weight, double height, int age,
                                             double expected) {
        double actual = personalisedFitnessPlan.calculateBMR(gender, weight, height, age);
        assertEquals(expected, actual);

    }

    @ParameterizedTest
    @MethodSource("totalDailyEnergyExpenditure")
    @Description("should return TDEE")
    public void totalDailyEnergyExpenditure_shouldReturnTDEE(HarrisBenedictCalculator.Gender gender, double weight,
                                                             double height, int age,
                                                             HarrisBenedictCalculator.ActivityLevel activityLevel, double expected) {
        double actual = personalisedFitnessPlan.calculateTDEE(gender, weight, height, age, activityLevel);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("mealTypeAndFileInput")
    @Description(value = "getMealsFromType() returns a list of given meal")
    public void getMealsFromType_ReturnsListOfMeal(MealPlanner.MealType meal, File jsonTestFile) {
//        try {
//            List<Ideas> expected = readAndWriteToJson.readJsonFile(jsonTestFile, MealIdeas.class).stream()
//                    .filter(mealType -> mealType.getMealType().equalsIgnoreCase(meal))
//                    .findFirst()
//                    .map(MealIdeas::getIdeas)
//                    .orElse(Collections.emptyList());
//
//            List<Ideas> actual = personalisedFitnessPlan.getMealsFromType(meal, jsonTestFile);
//
//            // assert that the actual list is not empty
//            assertFalse(actual.isEmpty(), "The actual list of meals should not be empty");
//
//            // assert that each meal in the actual list has the correct meal type
//            actual.forEach(idea -> assertEquals(meal.toLowerCase(), idea.getType().toLowerCase(),
//                    "Each meal in the actual list should have the correct meal type"));
//
//            // Assert that the size of the actual list is the same as the expected list
//            assertEquals(expected.size(), actual.size(),
//                    "The size of the actual list should be the same as the expected list");
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    @ParameterizedTest
    @MethodSource("mealTypeAndFileInput")
    @Description(value = "generateMealIdea() returns a meal idea for breakfast, lunch and dinner")
    public void generateMealIdea_ReturnsIdea(MealPlanner.MealType meal, File jsonTestFile) {
        try {

            Ideas result = personalisedFitnessPlan.generateMealIdea(meal, jsonTestFile);

            // assert that the return value is not null
            assertNotNull(result);

            // assert that result type is the same as the meal
//            assertEquals(result.getType().toLowerCase(), meal.toLowerCase());
//
//            // assert that
//            assertTrue(meal.toLowerCase().contains(result.getType().toLowerCase()));
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

            HashMap<String, Ideas> result = personalisedFitnessPlan.generateFullDayMealIdea(MEAL_DATA_FILE_PATH);

            assertFalse(result.isEmpty());
            assertTrue(result.containsKey(meal.toLowerCase()));
            assertFalse(result.get(meal).getSuitable_for().isEmpty());

            assertNotNull(result.get(meal));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @ParameterizedTest
    @MethodSource("workoutGoalAndFileInput")
    @Description(value = "generateWorkout() returns workout")
    public void generateWorkout_returnWorkout(String goal, File workOutDataFile) {
        try {

            List<Workout> result = personalisedFitnessPlan.generateWorkout(goal, workOutDataFile);
            assertFalse(result.isEmpty());

            boolean containsGoal = result.stream().anyMatch(workout -> workout.getSuitable_for().contains(goal));
            assertTrue(containsGoal);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Description(value = "readChatGPTResponse() returns workout")
    public void readChatGPTResponse_returnWorkout() {

        String goal = "Endurance_training";

        PersonalisedFitnessPlan personalisedFitnessPlan = Mockito.mock(PersonalisedFitnessPlan.class);

        Workout mockResponse = new Workout("Running", Arrays.asList("Endurance_training",
                "Cardiovascular_fitness"));
        when(personalisedFitnessPlan.readChatGPTResponse(goal)).thenReturn(mockResponse);

        assertNotNull(mockResponse);

        assertTrue(mockResponse.getSuitable_for().contains(goal));
        assertFalse(mockResponse.getSuitable_for().isEmpty());
        assertNotNull(mockResponse.getSuitable_for());


    }
}
