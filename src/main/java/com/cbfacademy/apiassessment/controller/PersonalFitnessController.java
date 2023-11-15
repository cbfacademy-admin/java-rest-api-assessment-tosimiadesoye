package com.cbfacademy.apiassessment.controller;

import com.cbfacademy.apiassessment.fitnessPlanner.Ideas;
import com.cbfacademy.apiassessment.fitnessPlanner.Workout;
import com.cbfacademy.apiassessment.service.PersonalFitnessServices;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/BMR")
    public ResponseEntity<Long> restingKcal(@RequestParam String gender,
                                            @RequestParam double weight,
                                            @RequestParam double height,
                                            @RequestParam int age) {


        Long restingKcal = personalFitnessServices.getRestingCalories(gender, weight, height, age);
            return ResponseEntity.ok(restingKcal);


    }

    @GetMapping("/TDEE")
    public ResponseEntity<Long> readTDEE(@RequestParam String gender, @RequestParam double weight,
                                           @RequestParam double height, @RequestParam int age, @RequestParam String activityLevel) {
            Long kcalConsumption = personalFitnessServices.getTDEE(gender, weight,
                    height, age, activityLevel);
            return ResponseEntity.ok(kcalConsumption);

    }

@GetMapping
    public ResponseEntity<Ideas> readMealPlan(@RequestParam String mealType) {

                Ideas ideas = personalFitnessServices.getMealPlan(mealType);
                return ResponseEntity.ok(ideas);


    }

    @GetMapping("/dailyMeal")
    public ResponseEntity<HashMap<String, Ideas>> readDailyMealPlan() {
            HashMap<String, Ideas> dailyMeal = personalFitnessServices.getDailyMeal();
            return ResponseEntity.ok(dailyMeal);

    }

    @GetMapping("/fitnessGoal")
    public ResponseEntity<List<Workout>> readWorkoutPlan(@RequestParam String goal) {
            List<Workout> workout = personalFitnessServices.getWorkoutPlan(goal);
            return ResponseEntity.ok(workout);

    }

}
