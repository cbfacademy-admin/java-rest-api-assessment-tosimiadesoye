package com.cbfacademy.apiassessment.fitnessPlanner;

import com.cbfacademy.apiassessment.Identifier;

import java.util.List;

public class Workout implements Identifier {
    String name;
    List<String> suitable_for;

    public Workout(String name, List<String> suitable_for) {
        this.name = name;
        this.suitable_for = suitable_for;
    }

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

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

}
