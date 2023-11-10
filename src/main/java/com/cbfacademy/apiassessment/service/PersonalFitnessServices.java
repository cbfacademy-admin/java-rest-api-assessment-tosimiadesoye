package com.cbfacademy.apiassessment.service;

import com.cbfacademy.apiassessment.fitnessPlanner.CalculateCalories.ActivityLevel;
import com.cbfacademy.apiassessment.fitnessPlanner.Ideas;
import com.cbfacademy.apiassessment.fitnessPlanner.PersonalisedFitnessPlan;
import com.cbfacademy.apiassessment.fitnessPlanner.Workout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class PersonalFitnessServices {

    private final PersonalisedFitnessPlan personalisedFitnessPlan;

    @Autowired
    public PersonalFitnessServices(PersonalisedFitnessPlan personalisedFitnessPlan) {
        this.personalisedFitnessPlan = personalisedFitnessPlan;
    }

    public double getRestingCalories(String gender, double weight, double height, int age) throws RuntimeException {
        return personalisedFitnessPlan.calculateBMR(gender, weight, height, age);
    }

    public Double getDailyKcalConsumption(String gender, double weight,
                                          double height, int age, ActivityLevel activityLevel) throws RuntimeException {
        return personalisedFitnessPlan.calcDailyKcalConsumption(gender, weight,
                height, age, activityLevel);
    }


    public Ideas getMealPlan(String mealType) throws IOException {
        return personalisedFitnessPlan.generateMealPlan(mealType);
    }

    public HashMap<String, Ideas> getDailyMeal() throws IOException {
        return personalisedFitnessPlan.generateFullDayMeal();
    }

    public List<Workout> getWorkoutPlan(String goal) throws IOException {
        return personalisedFitnessPlan.generateWorkout(goal);
    }


}
