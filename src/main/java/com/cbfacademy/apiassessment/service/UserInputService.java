package com.cbfacademy.apiassessment.service;

import com.cbfacademy.apiassessment.json.ReadAndWriteToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbfacademy.apiassessment.NotFoundException;
import com.cbfacademy.apiassessment.userData.UserData;

import java.io.File;

import java.io.IOException;
import java.util.List;


@Service
public class UserInputService {
    private static final String DATA_FILE_PATH = "src/main/resources/userData.json";
    ReadAndWriteToJson readAndWriteToJson;

    @Autowired
    public UserInputService() {
        readAndWriteToJson = new ReadAndWriteToJson();
    }

    public List<UserData> getUserInput() {

        try {
            return readAndWriteToJson.readJsonFile(new File(DATA_FILE_PATH), UserData.class);
        }catch (IOException e){
            throw new RuntimeException(e);
        }


    }

    public List<UserData> getUserInputById(String id) {

        try {
            List<UserData> entry = readAndWriteToJson.readJsonObjById(id, new File(DATA_FILE_PATH), UserData.class);
            if (entry.isEmpty()) {
                throw new NotFoundException("User not found with ID: " + id);
            }
            return entry;
        }catch (IOException e){
            throw new RuntimeException(e);
        }


    }

    public void createUserInput(UserData UserInput) {

        try {
            readAndWriteToJson.writeToJsonFile(UserInput, new File(DATA_FILE_PATH), UserData.class);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    public void updateUserInputById(String id, UserData UserInput) {

        try {
            readAndWriteToJson.updateUserDataId(id, UserInput, new File(DATA_FILE_PATH));

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteUserInputById(String id){

        try {
            readAndWriteToJson.deleteJsonObjById(id, new File(DATA_FILE_PATH), UserData.class);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

}
