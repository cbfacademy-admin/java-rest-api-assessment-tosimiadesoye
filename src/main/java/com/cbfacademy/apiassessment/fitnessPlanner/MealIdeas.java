package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.Identifier;

import java.util.List;

public class MealIdeas implements Identifier {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Idea> getIdeas() {
        return ideas;
    }

    public void setIdeas(List<Idea> ideas) {
        this.ideas = ideas;
    }

    private List<Idea> ideas;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String toString() {
        return "MealIdeas{" +
                "name='" + name + '\'' +
                ", ideas=" + ideas +
                '}';
    }
}

