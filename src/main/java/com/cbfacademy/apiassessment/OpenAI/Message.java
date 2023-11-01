package com.cbfacademy.apiassessment.OpenAI;


import com.cbfacademy.apiassessment.fitnessPlanner.Workout;

public class Message {
    private String role;

    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}

