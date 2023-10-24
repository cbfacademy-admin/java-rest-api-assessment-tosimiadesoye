package com.cbfacademy.apiassessment.userData;

import com.cbfacademy.apiassessment.CustomTypes.*;

public class UserData {
    private String id;
    private int age;
    private String gender;
    private double weight;
    private double height;
    private Goal fitness_goal;
    private DietPreference dietary_preference;
    private Allergic allergy;

    public UserData(String id, int age, String gender, double weight, double height, Goal fitness_goal,
                    DietPreference dietary_preference, Allergic allergy) {
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.fitness_goal = fitness_goal;
        this.allergy = allergy;
        this.dietary_preference = dietary_preference;

    }

    public UserData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Goal getFitness_goal() {
        return fitness_goal;
    }

    public void setFitness_goal(Goal fitness_goal) {
        this.fitness_goal = fitness_goal;
    }

    public DietPreference getDietary_preference() {
        return dietary_preference;
    }

    public void setDietary_preference(DietPreference dietary_preference) {
        this.dietary_preference = dietary_preference;
    }

    public Allergic getAllergy() {
        return allergy;
    }

    public void setAllergy(Allergic allergy) {
        this.allergy = allergy;
    }

    @Override
    public String toString() {
        return "userData{" +
                "id='" + id + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", fitness_goal=" + fitness_goal +
                ", dietary_preference=" + dietary_preference +
                '}';
    }

}
