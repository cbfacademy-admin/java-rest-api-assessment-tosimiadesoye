package com.cbfacademy.apiassessment.fitnessPlanner;

import java.util.List;

public class Ideas{

    String food;
    List<String> suitable_for;
    String recipe;

    public Ideas(String food, List<String> suitable_for, String recipe) {
        this.food = food;
        this.suitable_for = suitable_for;
        this.recipe = recipe;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public List<String> getSuitable_for() {
        return suitable_for;
    }

    public void setSuitable_for(List<String> suitable_for) {
        this.suitable_for = suitable_for;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

}
