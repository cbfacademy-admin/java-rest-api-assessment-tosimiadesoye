package com.cbfacademy.apiassessment.json;

import com.cbfacademy.apiassessment.CustomTypes.*;
import com.cbfacademy.apiassessment.userData.UserData;

import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.util.*;

import static com.cbfacademy.apiassessment.json.ReadAndWriteToJson.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "I/O")
public class ReadAndWriteToJsonTest {

    public static Logger logger = LoggerFactory.getLogger(ReadAndWriteToJsonTest.class);
    GsonBuilder gsonBuilder;

    File file;


    @BeforeEach

    public void setUp() {

        gsonBuilder = new GsonBuilder();
        file = new File("src/main/resources/userData.json");
    }


    @DisplayName(value = "readJsonFile() returns user data")
    @Test
    public void readJsonFile_ReturnsUserData() throws IOException {
        List<UserData> result = readJsonFile(file, UserData.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }




    @DisplayName(value = "readJsonFile() throws FileNotFoundException if it can't find the file")
    @Test
    public void readJsonFile_FileNotFoundException() throws IOException {

        Exception exception = assertThrows(FileNotFoundException.class, () -> {

            readJsonFile(new File("file.json"), UserData.class);
        });

        String expectedMessage = "File not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    @DisplayName(value = "writeToJsonFile() writes content")
    public void writeToJsonFile_writes() throws IOException {

        UserData newUserData = new UserData("392726", 38, "male", 80, 180, Goal.Glute, DietPreference.Gluten_free, Allergic.Yes);
        writeToJsonFile(newUserData, file, UserData.class);

        assertTrue(file.exists());

        // Read the content from the file
        String fileContent = Files.readString(Path.of("src/main/resources/userData.json"));

        // Deserialize the JSON content back into a list of UserData objects
        UserData[] updateEntries = gsonBuilder.create().fromJson(fileContent, UserData[].class);

        assertTrue(Arrays.stream(updateEntries).anyMatch(user -> user.getId().equals(newUserData.getId())));

    }

    @Test
    @DisplayName(value = "writeToJsonFile() identify that user exist")
    public void writeToJsonFile_dontWriteBecauseUserExist() throws IOException {

        UserData newUserData = new UserData("443293", 28, "male", 80, 180, Goal.Cardiovascular_fitness, DietPreference.Lactose_free, Allergic.No);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            writeToJsonFile(newUserData, file, UserData.class);
        });

        String expectedMessage = "User already exist; update user data instead.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    @DisplayName(value = "readJsonObjById() returns user data with Id: 74693")
    public void readJsonObjById_ReturnsIdContent() throws IOException {

        List<UserData> result = readJsonObjById("74693", file, UserData.class);
        List<UserData> data = readJsonFile(file, UserData.class);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, data.size());
        assertEquals(result.get(0).getId(), "74693");
    }


    @Test
    @DisplayName(value = "updateJsonObjById() updates content")
    public void updateJsonObjById_UpdatedContent() throws IOException {
        UserData newUserData = new UserData("443293", 18, "female", 45, 160, Goal.Lower_body, DietPreference.Non, Allergic.Yes);
        List<UserData> data = readJsonFile(file, UserData.class);

        updateUserDataId("443293", newUserData, file);

        assertTrue(file.exists());

        String fileContent = Files.readString(Path.of("src/main/resources/userData.json"));

        UserData[] updateEntries = gsonBuilder.create().fromJson(fileContent, UserData[].class);
        assertEquals(3, data.size());
        assertTrue(Arrays.asList(updateEntries).get(1).getGender() != "male");
        assertTrue(Arrays.asList(updateEntries).get(1).getAge() == 19);
        assertTrue(Arrays.asList(updateEntries).get(1).getHeight() == 150.0);

    }


    @Test
    @DisplayName(value = "deleteJsonObjById() deletes content")
    public void deleteJsonObjById_DeletesContent() throws IOException {

        deleteJsonObjById("443293", file, UserData.class);

        assertTrue(file.exists());

        String fileContent = Files.readString(Path.of("src/main/resources/userData.json"));

        UserData[] updateEntries = gsonBuilder.create().fromJson(fileContent, UserData[].class);
        assertTrue(!Arrays.asList(updateEntries).contains("443293"));

    }

}
