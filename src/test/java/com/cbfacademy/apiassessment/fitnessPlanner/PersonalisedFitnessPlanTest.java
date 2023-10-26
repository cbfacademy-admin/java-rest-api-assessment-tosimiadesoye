package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.userData.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

@DisplayName(value = "PersonalisedFitnessPlan")
public class PersonalisedFitnessPlanTest {

    public static Logger logger = LoggerFactory.getLogger(PersonalisedFitnessPlan.class);
    PersonalisedFitnessPlan personalisedFitnessPlan;
    File file;
    UserData user;
    // Create an ArrayList to store ideas
    List<Ideas> ideas = new ArrayList<>();

    @BeforeEach

    public void setUp() {
        file = new File("src/main/resources/meals.json");
        personalisedFitnessPlan = new PersonalisedFitnessPlan(user, CalculateCalories.ActivityLevel.VERY_ACTIVE);
        ideas.addAll(List.of(
                        new Ideas("Oatmeal with Berries",
                                List.of("Vegetarian", "Vegan", "Pescatarian", "Keto", "Gluten_free", "Lactose_free", "Low_carb", "Mediterranean"),
                                "1. Cook 1/2 cup of rolled oats in 1 cup of water or milk.\n2. Top with a handful of fresh mixed berries.\n3. Drizzle honey or maple syrup for sweetness.\n4. Sprinkle with chopped nuts for extra crunch.\n5. Enjoy!"),

                        new Ideas("Greek Yogurt Parfait",
                                List.of("Vegetarian", "Pescatarian", "Gluten_free", "Lactose_free", "Mediterranean"),
                                "1. Layer Greek yogurt, granola, and fresh fruit in a glass or bowl.\n2. Repeat the layers as desired.\n3. Top with a drizzle of honey or agave nectar.\n4. Serve and enjoy!")
                )
        );
    }

    @DisplayName(value = "getMealType() returns meal list")
    @Test
    public void getMealType_ReturnsListOfMeaL() throws IOException {
//        var actual = personalisedFitnessPlan.getMealType("breakfast");
        List<Ideas> expected = ideas;

    }

    @DisplayName(value = "generateMealPlan() returns one meal idea")
    @Test
    public void generateMealPlan_ReturnsIdea() throws IOException {
//        var actual = personalisedFitnessPlan.generateMealPlan("breakfast");
    }
}
