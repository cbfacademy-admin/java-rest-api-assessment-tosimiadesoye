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

import com.cbfacademy.apiassessment.NotFoundException;
import com.cbfacademy.apiassessment.service.UserInputService;
import com.cbfacademy.apiassessment.userData.UserData;

import java.io.IOException;
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
    public ResponseEntity<List<UserData>> readUserDataApi() {
        try {
            List<UserData> entries = userDataService.getUserInput();
            return ResponseEntity.ok(entries);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<UserData>> readUserDataByIdApi(@PathVariable String id) {
        try {
            List<UserData> entry = userDataService.getUserInputById(id);
            if (entry.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(entry);
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping
    @Operation(
            description = "User post service",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "badRequestAPI"),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created user!",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Successfully created user!\"}"
                                            ),
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<WebResponse> createUserDataApi(@RequestBody UserData userData) {

        try {
            userDataService.createUserInput(userData);
            return new ResponseEntity<>(new WebResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Successfully created user!"), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new WebResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(
            description = "User put service",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "badRequestAPI"),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created user!",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Successfully updated user!\"}"
                                            ),
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<WebResponse> updateUserDataById(@PathVariable String id, @RequestBody UserData userData) {

        try {
            userDataService.updateUserInputById(id, userData);
            return new ResponseEntity<>(new WebResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Successfully updated user!"), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new WebResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDataById(@PathVariable String id) {
        try {
            userDataService.deleteUserInputById(id);
            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
