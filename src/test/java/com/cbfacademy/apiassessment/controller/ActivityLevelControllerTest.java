package com.cbfacademy.apiassessment.controller;

import com.cbfacademy.apiassessment.App;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;

import java.net.URL;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActivityLevelControllerTest {

    Gson gson;
    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/api/v1/activityLevel");
        gson = new Gson();
    }

    @Test
    @Description("/api/v1/activityLevel endpoint returns response for activity level")
    public void activityLevel_ExpectedResponseWithActivityLevel() {
        ResponseEntity<String> response = restTemplate.getForEntity(base.toString(), String.class);
        assertEquals(200, response.getStatusCode().value());
        String responseBody = response.getBody();

        try {
            String[] actualResult = gson.fromJson(responseBody, String[].class);

            String[] expectedResult = {
                    "SEDENTARY",
                    "LIGHTLY_ACTIVE",
                    "MODERATELY_ACTIVE",
                    "VERY_ACTIVE",
                    "SUPER_ACTIVE",
            };


            assertArrayEquals(expectedResult, actualResult);
        } catch (JsonSyntaxException e) {
            fail("Failed to parse the Json response");
        }

    }
}
