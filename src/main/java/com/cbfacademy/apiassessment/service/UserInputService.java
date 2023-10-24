package com.cbfacademy.apiassessment.service;

import org.springframework.stereotype.Service;

import com.cbfacademy.apiassessment.NotFoundException;
import com.cbfacademy.apiassessment.userData.UserData;

import java.io.File;

import java.io.IOException;
import java.util.List;

import static com.cbfacademy.apiassessment.json.ReadAndWriteToJson.*;

@Service
public class UserInputService {
    private static final String DATA_FILE_PATH = "src/main/resources/userData.json";


    public List<UserData> getUserInput() throws IOException {

        return readJsonFile(new File(DATA_FILE_PATH), UserData.class);

    }

    public List<UserData> getUserInputById(String id) throws IOException {

        List<UserData> entry = readJsonObjById(id, new File(DATA_FILE_PATH), UserData.class);
        if (entry.isEmpty()) {
            throw new NotFoundException("User not found with ID: " + id);
        }
        return entry;

    }

    public void createUserInput(UserData UserInput) throws IOException {
        writeToJsonFile(UserInput, new File(DATA_FILE_PATH), UserData.class);
    }

    public void updateUserInputById(String id, UserData UserInput) throws IOException {
        updateUserDataId(id, UserInput, new File(DATA_FILE_PATH));

    }

    public void deleteUserInputById(String id) throws IOException {
        deleteJsonObjById(id, new File(DATA_FILE_PATH), UserData.class);
    }

}
