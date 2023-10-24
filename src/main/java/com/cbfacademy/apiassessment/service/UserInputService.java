package com.cbfacademy.apiassessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbfacademy.apiassessment.NotFoundException;
import com.cbfacademy.apiassessment.json.ReadAndWriteToJson;
import com.cbfacademy.apiassessment.userData.UserData;

import java.io.File;

import java.io.IOException;
import java.util.List;

@Service
public class UserInputService {
    private static final String DATA_FILE_PATH = "src/main/resources/userData.json";

    @Autowired
    private final ReadAndWriteToJson jsonUtil;

    public UserInputService(ReadAndWriteToJson jsonUtil) {
        this.jsonUtil = jsonUtil;
    }

    public List<UserData> getUserInput() throws IOException {

        return jsonUtil.readJsonFile(new File(DATA_FILE_PATH));
    }

    public List<UserData> getUserInputById(String id) throws IOException {

        List<UserData> entry = jsonUtil.readJsonObjById(id, new File(DATA_FILE_PATH));
        if (entry.isEmpty()) {
            throw new NotFoundException("User not found with ID: " + id);
        }
        return entry;

    }

    public void createUserInput(UserData UserInput) throws IOException {
        jsonUtil.writeToJsonFile(UserInput, new File(DATA_FILE_PATH));
    }

    public void updateUserInputById(String id, UserData UserInput) throws IOException {
        jsonUtil.updateJsonObjById(id, UserInput, new File(DATA_FILE_PATH));

    }

    public void deleteUserInputById(String id) throws IOException {
        jsonUtil.deleteJsonObjById(id, new File(DATA_FILE_PATH));
    }

}
