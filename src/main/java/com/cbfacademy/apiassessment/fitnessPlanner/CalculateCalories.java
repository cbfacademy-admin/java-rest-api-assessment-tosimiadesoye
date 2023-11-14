package com.cbfacademy.apiassessment.fitnessPlanner;

public interface CalculateCalories {
    public double calculateBMR(Gender gender, double weight, double height, int age);

    public double calculateTDEE(Gender gender, double weight, double height, int age,
                                ActivityLevel activityLevel);

    public static enum ActivityLevel {
        SEDENTARY(1.2), // little or no exercise
        LIGHTLY_ACTIVE(1.375), // light exercise or sports 1-3 days a week)
        MODERATELY_ACTIVE(1.55), // moderate exercise or sports 3-5 days a week
        VERY_ACTIVE(1.725), // hard exercise or sports 6-7 days a week
        SUPER_ACTIVE(1.9); // very hard exercise, physical job, or training twice a day

        private final double multiplier;

        ActivityLevel(double multiplier) {
            this.multiplier = multiplier;
        }

        public double getMultiplier() {
            return multiplier;
        }
    }

    public static enum Gender{
        FEMALE,
        MALE;
    }

}
