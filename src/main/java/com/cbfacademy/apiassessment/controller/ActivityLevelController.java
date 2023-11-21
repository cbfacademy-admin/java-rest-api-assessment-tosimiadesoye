package com.cbfacademy.apiassessment.controller;

import com.cbfacademy.apiassessment.service.ActivityLevelService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("/api/v1/activityLevel")
public class ActivityLevelController {

    @Autowired
    private final ActivityLevelService activityLevelService;

    public ActivityLevelController(ActivityLevelService activityLevelService) {
        this.activityLevelService = activityLevelService;
    }

    @GetMapping
    public ResponseEntity<List<String>> readActivityLevel() {

        List<String> activityLevel = activityLevelService.getActivityLevelAsAList();
        return ResponseEntity.ok(activityLevel);

    }
}
