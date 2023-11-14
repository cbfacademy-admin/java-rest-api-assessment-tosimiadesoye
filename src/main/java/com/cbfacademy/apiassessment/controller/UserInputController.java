package com.cbfacademy.apiassessment.controller;

import com.cbfacademy.apiassessment.response.WebResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cbfacademy.apiassessment.service.UserInputService;
import com.cbfacademy.apiassessment.userData.UserData;

import java.util.List;

@RestController
@OpenAPIDefinition
@RequestMapping("/api/v1/userProfile")
public class UserInputController {

    @Autowired
    private final UserInputService userDataService;

    public UserInputController(
            UserInputService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping
    public ResponseEntity<List<UserData>> readUserData()  {
            List<UserData> entries = userDataService.getUserInput();
            return ResponseEntity.ok(entries);


    }

    @GetMapping("/userId")
    public ResponseEntity<List<UserData>> readUserDataById(@RequestParam String id) {

            List<UserData> entry = userDataService.getUserInputById(id);
            if (entry.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(entry);
            }


    }

    @PostMapping
    @Operation(
            description = "User post service",
            responses = {
                    @ApiResponse(responseCode = "201", ref = "badRequestAPI"),
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully created user!",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"Successfully created user!\"}"
                                            ),
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<WebResponse> createUserData(@RequestBody UserData userData) {


            userDataService.createUserInput(userData);
            return new ResponseEntity<>(new WebResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "Successfully created user!"), HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<WebResponse> updateUserDataById(@RequestParam String id, @RequestBody UserData userData) {


            userDataService.updateUserInputById(id, userData);
            return ResponseEntity.noContent().build();

    }

    @DeleteMapping
    public ResponseEntity<WebResponse> deleteUserDataById(@RequestParam String id) {

            userDataService.deleteUserInputById(id);
            return ResponseEntity.noContent().build();

    }

}
