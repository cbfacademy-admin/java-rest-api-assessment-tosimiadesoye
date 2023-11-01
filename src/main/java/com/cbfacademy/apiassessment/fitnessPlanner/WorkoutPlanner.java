package com.cbfacademy.apiassessment.fitnessPlanner;

import java.io.IOException;
import java.util.List;

public interface WorkoutPlanner {
    public List<Workout> generateWorkout(String value) throws IOException;

    public void addWorkout(Workout workout) throws IOException;

}