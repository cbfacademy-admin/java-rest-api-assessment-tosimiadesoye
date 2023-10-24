package com.cbfacademy.apiassessment.fitnessPlanner;

import java.io.IOException;
import java.util.List;

public interface MealPlanner {


    List<MealIdeas> breakfast(String breakfast) throws IOException;

    public List<MealIdeas> lunch(String lunch) throws IOException;

    public List<MealIdeas> dinner(String dinner) throws IOException;

    public String generateMealPlan();
}
