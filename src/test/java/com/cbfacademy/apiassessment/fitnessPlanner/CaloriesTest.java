package com.cbfacademy.apiassessment.fitnessPlanner;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.Description;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Description(value = "Calories")
public class CaloriesTest {

    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);
    PersonalisedFitnessPlan personalisedFitnessPlan;


    public static Stream<Arguments> calculateBMRArguments() {
        return Stream.of(
                Arguments.of("female", 50, 160, 21, 1314.693),
                Arguments.of("male", 50, 160, 21, 1406.837)
        );
    }


    public static Stream<Arguments> calcDailyKcalConsumption() {
        return Stream.of(
                Arguments.of("female", 50, 160, 21, "SEDENTARY",1577.6316),
                Arguments.of("male", 50, 160, 21, "LIGHTLY_ACTIVE", 1934.400875),
                Arguments.of("female", 45, 160, 18, "MODERATELY_ACTIVE",1986.2443999999998),
                Arguments.of("male", 70, 180, 25, "VERY_ACTIVE",3015.3845250000004),
                Arguments.of("female", 60, 170, 35, "SUPER_ACTIVE",2617.2936999999997)
        );
    }

    @BeforeEach
    public void setUp() {
        personalisedFitnessPlan = new PersonalisedFitnessPlan();
    }

    @ParameterizedTest
    @MethodSource("calculateBMRArguments")
    @Description("should return BMR")
    public void calculateBMR_shouldReturnBMR(String gender, double weight, double height, int age, double expected) {
        double actual = personalisedFitnessPlan.calculateBMR(gender, weight, height, age);
        assertEquals(expected, actual);

    }

    @ParameterizedTest
    @MethodSource("calcDailyKcalConsumption")
    @Description("should return TDEE")
    public void calcDailyKcalConsumption_shouldReturnTDEE(String gender, double weight, double height, int age, CalculateCalories.ActivityLevel activityLevel, double expected) {
        double actual = personalisedFitnessPlan.calcDailyKcalConsumption(gender, weight, height, age, activityLevel);
        assertEquals(expected, actual);
    }

}