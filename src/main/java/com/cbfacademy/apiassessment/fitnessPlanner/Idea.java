package com.cbfacademy.apiassessment.fitnessPlanner;

import java.util.List;

public class Idea {

    String food;

    String type;

    List<String> suitable_for;
    String recipe;

    public Idea(String food, String type, List<String> suitable_for, String recipe) {
        this.food = food;
        this.suitable_for = suitable_for;
        this.recipe = recipe;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
