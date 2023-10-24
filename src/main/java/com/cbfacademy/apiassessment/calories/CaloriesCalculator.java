package com.cbfacademy.apiassessment.calories;

import com.cbfacademy.apiassessment.userData.UserData;


public class CaloriesCalculator {
    int age;
    String gender;
    double weight;
    double height;

    Double basalMetabolicRate;

    ActivityLevel activityLevel;

    public CaloriesCalculator(UserData userData, ActivityLevel activityLevel) {
        this.age = userData.getAge();
        this.gender = userData.getGender();
        this.weight = userData.getWeight();
        this.height = userData.getHeight();
        this.activityLevel = activityLevel;
    }

    public double calcBMR() {

        if ("female".equalsIgnoreCase(gender)) {
            basalMetabolicRate = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        } else if ("male".equalsIgnoreCase(gender)) {
            basalMetabolicRate = 88.364 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            throw new IllegalArgumentException("Invalid gender: " + gender);
        }
        return basalMetabolicRate;
    }

    public double calculateDailyKcalConsumption() {
        return basalMetabolicRate * activityLevel.getMultiplier();
    }

}
