package com.cbfacademy.apiassessment.service;

import com.cbfacademy.apiassessment.fitnessPlanner.CalculateCalories.ActivityLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ActivityLevelService {

    public static void main(String[] args) {
        List<String> activityLevelService = new ActivityLevelService().getActivityLevelAsAList();
        System.out.println(activityLevelService);
    }


    public List<String> getActivityLevelAsAList() {

        return Stream.of(ActivityLevel.values())
                .map(ActivityLevel::name)
                .toList();

    }

}
