package com.cbfacademy.apiassessment.controller;

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
@RequestMapping("/api/v1/userProfile")
public class UserInputController {

    @Autowired
    private final UserInputService userDataService;

    public UserInputController(
            UserInputService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping
    public ResponseEntity<List<UserData>> readUserDataApi() throws IOException {
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
    public ResponseEntity<Void> createUserDataApi(@RequestBody UserData userData) {

        try {
            userDataService.createUserInput(userData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserDataById(@PathVariable String id, @RequestBody UserData userData) {

        try {
            userDataService.updateUserInputById(id, userData);
            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteuserDataById(@PathVariable String id) {
        try {
            userDataService.deleteUserInputById(id);
            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
