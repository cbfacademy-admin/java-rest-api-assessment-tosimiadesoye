package com.cbfacademy.apiassessment.controller;

import com.cbfacademy.apiassessment.fitnessPlanner.CalculateCalories.ActivityLevel;
import com.cbfacademy.apiassessment.fitnessPlanner.Ideas;
import com.cbfacademy.apiassessment.fitnessPlanner.Workout;
import com.cbfacademy.apiassessment.service.PersonalFitnessServices;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@OpenAPIDefinition
@RequestMapping("/api/v1/personalisedFitness")
public class PersonalFitnessController {

    @Autowired
    private final PersonalFitnessServices personalFitnessServices;

    public PersonalFitnessController(PersonalFitnessServices personalFitnessServices) {
        this.personalFitnessServices = personalFitnessServices;
    }

    @GetMapping("/{gender}/{weight}/{height}/{age}")
    public ResponseEntity<Double> restingKcal(@PathVariable String gender,
                                              @PathVariable double weight,
                                              @PathVariable double height, @PathVariable int age) {
        try {
            Double restingKcal = personalFitnessServices.getRestingCalories(gender, weight, height, age);
         return  ResponseEntity.ok(restingKcal);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{gender}/{weight}/{height}/{age}/{activityLevel}")
    public ResponseEntity<Double> readTDEE(@PathVariable String gender, @PathVariable double weight,
                                           @PathVariable double height, @PathVariable int age, @PathVariable ActivityLevel activityLevel) {
        try {
            Double kcalConsumption = personalFitnessServices.getDailyKcalConsumption(gender, weight,
                    height, age, activityLevel);
            return ResponseEntity.ok(kcalConsumption);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/meal/type/{mealType}")
    public ResponseEntity<Ideas> readMealPlan(@PathVariable String mealType) {
        try {
            Ideas ideas = personalFitnessServices.getMealPlan(mealType);
            return ResponseEntity.ok(ideas);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // todo - test in postman once all issues are resolved
    @GetMapping("/dailyMeal")
    public ResponseEntity<HashMap<String, Ideas>> readDailyMealPlan() {
        try {
            HashMap<String, Ideas> dailyMeal = personalFitnessServices.getDailyMeal();
            return ResponseEntity.ok(dailyMeal);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{goal}")
    public ResponseEntity<List<Workout>> readWorkoutPlan(@PathVariable String goal) {
        try {
            List<Workout> workout = personalFitnessServices.getWorkoutPlan(goal);
            return ResponseEntity.ok(workout);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
