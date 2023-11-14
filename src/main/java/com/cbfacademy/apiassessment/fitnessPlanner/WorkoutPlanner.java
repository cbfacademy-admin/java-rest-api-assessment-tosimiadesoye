package com.cbfacademy.apiassessment.fitnessPlanner;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface WorkoutPlanner {
    public List<Workout> generateWorkout(String goal, File workOutDataFile) throws IOException;
    public void addWorkout(Workout workout, File workOutDataFile) throws IOException;

}