package com.cbfacademy.apiassessment.json;

import com.cbfacademy.apiassessment.userData.UserData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReadAndWriteToJson {
    private static final Logger logger = LoggerFactory.getLogger(ReadAndWriteToJson.class);
    private final Gson gson = new Gson();

    public List<UserData> readJsonFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File not found");
        }
        logger.info(String.valueOf(file));
        try (FileReader reader = new FileReader(file)) {

            TypeToken<List<UserData>> userData = new TypeToken<>() {
            };
            logger.info(String.valueOf(userData));

            return gson.fromJson(reader, userData);
        }
    }

    public boolean userExists(List<UserData> userEntries, String userId) {
        for (UserData data : userEntries) {
            if (data.getId().equals(userId)) {
                return true;

            }
        }
        return false;
    }

    public void writeToJsonFile(UserData reqBody, File file) throws IOException {
        List<UserData> orderEntries = readJsonFile(file);


        if (userExists(orderEntries, reqBody.getId())) {
            throw new RuntimeException("User already exist; update user data instead.");
        }
        orderEntries.add(reqBody);
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(orderEntries, writer);
        }

    }

    public List<UserData> readJsonObjById(String id, File file) throws IOException {
        List<UserData> data = readJsonFile(file);
        return data.stream()
                .filter(item -> item.getId().equals(id))
                .collect(Collectors.toList());
    }

    public void updateJsonObjById(String id, UserData reqBody, File file) throws IOException {

        List<UserData> userData = readJsonFile(file);
        boolean found = false;

        for (UserData data : userData) {
            if (data.getId().equals(id)) {
                data.setAge(reqBody.getAge());
                data.setGender(reqBody.getGender());
                data.setHeight(reqBody.getHeight());
                data.setWeight(reqBody.getWeight());
                data.setDietary_preference(reqBody.getDietary_preference());
                data.setFitness_goal(reqBody.getFitness_goal());
                data.setAllergy(reqBody.getAllergy());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("user with ID:" + id + " Not found");
        }
        try (Writer writer = new FileWriter(file)) {
            // Write the entire List (with updated data) back to the file
            gson.toJson(userData, writer);
        }
    }

    public void deleteJsonObjById(String id, File file) throws IOException {

        List<UserData> orderEntries = readJsonFile(file);

        List<UserData> updateOrderEntries = orderEntries.stream()
                .filter(entry -> !entry.getId().equals(id))
                .collect(Collectors.toList());

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(updateOrderEntries, writer);
        }

    }

}
