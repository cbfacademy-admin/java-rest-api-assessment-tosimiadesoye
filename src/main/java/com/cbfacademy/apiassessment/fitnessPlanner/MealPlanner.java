package com.cbfacademy.apiassessment.fitnessPlanner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface MealPlanner {

    public List<Ideas> getMealType(String type) throws IOException;
    public Ideas generateMealPlan(String meal) throws IOException;

    public HashMap<String, Ideas> generateFullDayMeal() throws IOException;
    public List<MealIdeas> addMeal()throws IOException;
}
