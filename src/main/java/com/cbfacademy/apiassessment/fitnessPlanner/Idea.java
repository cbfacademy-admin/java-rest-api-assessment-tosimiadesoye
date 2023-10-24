package com.cbfacademy.apiassessment.fitnessPlanner;

import java.util.List;

class Idea{

    String name;
    List<String> suitable_for;
    String recipe;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
