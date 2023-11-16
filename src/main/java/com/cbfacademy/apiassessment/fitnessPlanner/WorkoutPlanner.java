package com.cbfacademy.apiassessment.fitnessPlanner;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface WorkoutPlanner {
   List<Workout> generateWorkout(String goal) throws IOException;
   void addWorkout(Workout workout) throws IOException;

}