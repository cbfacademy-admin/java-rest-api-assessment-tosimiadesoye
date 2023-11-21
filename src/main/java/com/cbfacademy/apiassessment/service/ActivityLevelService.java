package com.cbfacademy.apiassessment.service;

import com.cbfacademy.apiassessment.fitnessPlanner.HarrisBenedictCalculator.ActivityLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ActivityLevelService {

    public List<String> getActivityLevelAsAList() {

        return Stream.of(ActivityLevel.values())
                .map(ActivityLevel::name)
                .toList();

    }

}
