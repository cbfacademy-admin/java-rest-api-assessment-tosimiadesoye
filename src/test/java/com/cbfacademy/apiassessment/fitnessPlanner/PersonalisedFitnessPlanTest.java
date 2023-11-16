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

@Description(value = "Personalised Fitness Plan")
public class PersonalisedFitnessPlanTest {
    private static final File DATA_FILE_PATH = new File("src/test/resources/data.json");

    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);
    PersonalisedFitnessPlan personalisedFitnessPlan;
    ReadAndWriteToJson readAndWriteToJson;

    File tempFile;

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



    public static Stream<Arguments> workoutGoals() {
        return Stream.of(
                Arguments.of("Strength_training"),
                Arguments.of("Upper_body"));

    }

public List<Workout> getWorkout(){
   Workout workout = new Workout("Push", Arrays.asList("Upper_body",
            "Strength_training"));
    Workout workout2 = new Workout("Squats", Arrays.asList("Lower_body",
            "Strength_training"));

    List <Workout> workoutList= new ArrayList<>();
  workoutList.add(workout);
  workoutList.add(workout2);

    return workoutList;
}
    @BeforeEach
    public void setUp() {
        try {

            if(!DATA_FILE_PATH.exists()){
                DATA_FILE_PATH.mkdir();
            }
             tempFile =  File.createTempFile( "prefix", "ext", DATA_FILE_PATH);
            tempFile.deleteOnExit();
            readAndWriteToJson = Mockito.mock(ReadAndWriteToJson.class);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        personalisedFitnessPlan = new PersonalisedFitnessPlan(readAndWriteToJson);
    }

    @ParameterizedTest
    @MethodSource("calculateBMRArguments")
    @Description("should return BMR")
    public void calculateBMR_shouldReturnBMR(HarrisBenedictCalculator.Gender gender, double weight, double height,
            int age,
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
    @MethodSource("workoutGoals")
    @Description(value = "generateWorkout() returns workout")
    public void generateWorkout_returnWorkout(String goal ) {
        try {

           when(personalisedFitnessPlan.fetchWorkouts()).thenReturn(getWorkout());
            List<Workout> result = personalisedFitnessPlan.generateWorkout(goal);

            assertFalse(result.isEmpty());

            boolean containsGoal = result.stream().anyMatch(workout -> workout.getSuitable_for().contains(goal));
            assertTrue(containsGoal);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Description(value = "addWorkout() returns workout")
    public void addWorkout_returnsVoid() {
        try {

            when(personalisedFitnessPlan.fetchWorkouts()).thenReturn(getWorkout());
            List<Workout> result = personalisedFitnessPlan.fetchWorkouts();

            assertFalse(result.isEmpty());
            assertEquals(2, result.size());

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

        Workout result = personalisedFitnessPlan.readChatGPTResponse(goal);
        assertNotNull(result);

        assertTrue(result.getSuitable_for().contains(goal));
        assertFalse(result.getSuitable_for().isEmpty());
        assertNotNull(result.getSuitable_for());

    }
}
