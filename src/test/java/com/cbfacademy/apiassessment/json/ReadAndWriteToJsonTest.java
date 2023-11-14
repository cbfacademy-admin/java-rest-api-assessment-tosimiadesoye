package com.cbfacademy.apiassessment.json;

import com.cbfacademy.apiassessment.userData.UserData;

import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.util.*;

import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.*;

@Description(value = "I/O")
public class ReadAndWriteToJsonTest {

    @TempDir
    Path tempDir;

    Path tempFile;

    UserData userData = new UserData("392726", 38, "male", 80, 180, "Glute", "Gluten_free");

    ReadAndWriteToJson readAndWriteToJson;

    GsonBuilder gsonBuilder;

    @BeforeEach

    public void setUp() {
        readAndWriteToJson = new ReadAndWriteToJson();
        gsonBuilder = new GsonBuilder();
        try {
            tempFile = Files.createFile(tempDir.resolve("temp.json"));
            Files.writeString(tempFile, "[" + gsonBuilder.setPrettyPrinting().create().toJson(userData) + "]");
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    @Description(value = "readJsonFile() returns contents")
    @Test
    public void readJsonFile_ReturnsFileContent() throws IOException {
        List<UserData> result = readAndWriteToJson.readJsonFile(tempFile.toFile(), UserData.class);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Description(value = "readJsonFile() throws FileNotFoundException if it can't find the file")
    @Test
    public void readJsonFile_FileNotFoundException() throws IOException {

        Exception exception = assertThrows(FileNotFoundException.class, () -> {

            readAndWriteToJson.readJsonFile(new File("file.json"), UserData.class);
        });

        String expectedMessage = "File not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Description(value = "readJsonObjById() returns content")
    public void readJsonObjById_ReturnsIdContent() throws IOException {
        List<UserData> result = readAndWriteToJson.readJsonObjById("392726", tempFile.toFile(), UserData.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(result.get(0).getId(), userData.getId());
    }

    @Test
    @Description(value = "writeToJsonFile() writes content")
    public void writeToJsonFile_writes() throws IOException {

        UserData newUserData = new UserData("748493", 48, "male", 75, 180, "Muscle", "Gluten_free");

        readAndWriteToJson.writeToJsonFile(newUserData, tempFile.toFile(), UserData.class);

        // Verify that the file has been written
        assertTrue(tempFile.toFile().exists());

        // Read the content from the file
        String fileContent = Files.readString(tempFile);

        // Deserialize the JSON content back into a list of OrderEntry objects
        UserData[] updateUserData = gsonBuilder.create().fromJson(fileContent, UserData[].class);

        assertEquals(Arrays.asList(updateUserData).get(1).getId(), newUserData.getId());
        assertEquals(Arrays.asList(updateUserData).get(1).getAge(), newUserData.getAge());


    }


    @Test
    @Description(value = "updateJsonObjById() updates content")
    public void updateJsonObjById_UpdatedContent() throws IOException {
        UserData newUserData = new UserData("392726", 18, "female", 45, 160, "Lower_body", "");
        readAndWriteToJson.updateUserDataId("392726", newUserData, tempFile.toFile());

        assertTrue(tempFile.toFile().exists());

        String fileContent = Files.readString(tempFile);

        UserData[] updateUser = gsonBuilder.create().fromJson(fileContent, UserData[].class);
        assertNotSame("male", Arrays.asList(updateUser).get(0).getGender());
        assertEquals(18, Arrays.asList(updateUser).get(0).getAge());
        assertEquals(160, Arrays.asList(updateUser).get(0).getHeight());
    }

    @Test
    @Description(value = "deleteJsonObjById() deletes content")
    public void deleteJsonObjById_DeletesContent() throws IOException {
        readAndWriteToJson.deleteJsonObjById("443293", tempFile.toFile(), UserData.class);

        assertTrue(tempFile.toFile().exists());

        String fileContent = Files.readString(tempFile);

        UserData[] updateUserData = gsonBuilder.create().fromJson(fileContent, UserData[].class);
        assertFalse(Arrays.asList(updateUserData).contains("392726"));

    }

}