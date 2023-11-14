package com.cbfacademy.apiassessment.fitnessPlanner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface MealPlanner {

   public enum MealType{
           BREAKFAST,
           LUNCH,
           DINNER;
   }

    public List<Ideas> getMealsFromType(String mealType, File mealDataFile) throws IOException;
    public Ideas generateMealIdea(String mealType, File mealDataFile) throws IOException;
    public HashMap<String, Ideas> generateFullDayMealIdea(File mealDataFile) throws IOException;
}
