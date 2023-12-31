package com.cbfacademy.apiassessment.json;

import com.cbfacademy.apiassessment.Identifier;
import com.cbfacademy.apiassessment.userData.UserData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReadAndWriteToJson implements JsonDataStore {
    private final Gson gson = new Gson();

    @Override
    public <T> List<T> readJsonFile(File file, Class<T> clazz) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("File not found");
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Identifier> void writeToJsonFile(T reqBody, File file, Class<T> clazz)
            throws FileNotFoundException {
        List<T> dataEntries = readJsonFile(file, clazz);

        dataEntries.add(reqBody);
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(dataEntries, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public <T extends Identifier> List<T> readJsonObjById(String id, File file, Class<T> clazz)
            throws FileNotFoundException {
        List<T> data = readJsonFile(file, clazz);
        return data.stream()
                .filter(item -> item.getId().equals(id))
                .collect(Collectors.toList());
    }

    public void updateUserDataId(String id, UserData reqBody, File file) throws FileNotFoundException {

        List<UserData> userData = readJsonFile(file, UserData.class);
        boolean found = false;

        for (UserData data : userData) {
            if (data.getId().equals(id)) {
                data.setAge(reqBody.getAge());
                data.setGender(reqBody.getGender());
                data.setHeight(reqBody.getHeight());
                data.setWeight(reqBody.getWeight());
                data.setDietary_preference(reqBody.getDietary_preference());
                data.setFitness_goal(reqBody.getFitness_goal());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("user with ID: " + id + ". Not found");
        }
        try (Writer writer = new FileWriter(file)) {
            // Write the entire List (with updated data) back to the file
            gson.toJson(userData, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Identifier> void deleteJsonObjById(String id, File file, Class<T> clazz)
            throws FileNotFoundException {

        List<T> orderEntries = readJsonFile(file, clazz);

        List<T> updateOrderEntries = orderEntries.stream()
                .filter(entry -> !entry.getId().equals(id))
                .collect(Collectors.toList());

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(updateOrderEntries, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}