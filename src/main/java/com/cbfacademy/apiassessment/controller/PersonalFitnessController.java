package com.cbfacademy.apiassessment.controller;

import com.cbfacademy.apiassessment.fitnessPlanner.Idea;
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
        public ResponseEntity<Long> readBMR(@RequestParam String gender,
                        @RequestParam double weight,
                        @RequestParam double height,
                        @RequestParam int age) {

                Long restingKcal = personalFitnessServices.getBMR(gender, weight, height, age);
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
        public ResponseEntity<Idea> readMealPlan(@RequestParam String mealType) {

                Idea Idea = personalFitnessServices.getMealPlan(mealType);
                return ResponseEntity.ok(Idea);

        }

        @GetMapping("/dailyMeal")
        public ResponseEntity<HashMap<String, Idea>> readDailyMealPlan() {
                HashMap<String, Idea> dailyMeal = personalFitnessServices.getDailyMeal();
                return ResponseEntity.ok(dailyMeal);

        }

        @GetMapping("/fitnessGoal")
        public ResponseEntity<List<Workout>> readWorkoutPlan(@RequestParam String goal) {
                List<Workout> workout = personalFitnessServices.getWorkoutPlan(goal);
                return ResponseEntity.ok(workout);

        }

}
