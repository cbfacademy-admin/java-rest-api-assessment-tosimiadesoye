package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.Identifier;

import java.util.List;

public class MealIdea implements Identifier {
    private String mealType;
    private List<Idea> Idea;

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public List<Idea> getIdea() {
        return Idea;
    }

    public MealIdea(String mealType, List<Idea> Idea) {
        this.mealType = mealType;
        this.Idea = Idea;
    }

    public void setIdea(List<Idea> Idea) {
        this.Idea = Idea;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

}
