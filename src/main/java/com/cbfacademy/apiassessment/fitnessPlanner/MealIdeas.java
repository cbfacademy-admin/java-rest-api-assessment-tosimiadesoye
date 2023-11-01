package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.Identifier;

import java.util.List;


public class MealIdeas implements Identifier {
    private String mealType;
    private List<Ideas> ideas;

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public List<Ideas> getIdeas() {
        return ideas;
    }

    public MealIdeas(String mealType, List<Ideas> ideas) {
        this.mealType = mealType;
        this.ideas = ideas;
    }

    public void setIdeas(List<Ideas> ideas) {
        this.ideas = ideas;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

}

