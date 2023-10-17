package com.cbfacademy.apiassessment.json;

import com.cbfacademy.apiassessment.Json.LocalDateTimeUKTypeAdapter;
import com.cbfacademy.apiassessment.orderEntry.OrderEntry.Status;
import com.cbfacademy.apiassessment.orderEntry.OrderEntry.Side;
import com.cbfacademy.apiassessment.Json.ReadAndWriteToJson;
import com.cbfacademy.apiassessment.orderEntry.OrderEntry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "I/O")
public class ReadAndWriteToJsonTest {

    @TempDir
    Path tempDir;

    Path tempFile;

    OrderEntry orderEntry = new OrderEntry(91780, "GOOG", 100, 99, Side.BUY, Status.PENDING,
            LocalDateTime.of(2023, 05, 03, 10, 0));

    ReadAndWriteToJson readAndWriteToJson;

    GsonBuilder gsonBuilder = new GsonBuilder();

    @BeforeEach

    public void setUp() {
        readAndWriteToJson = new ReadAndWriteToJson();

        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeUKTypeAdapter());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        try {
            tempFile = Files.createFile(tempDir.resolve("temp.json"));
            String json = gson.toJson(orderEntry);
            Files.writeString(tempFile, "[" + json + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName(value = "readJsonFile() returns contents")
    @Test
    public void readJsonFile_ReturnsFileContent() throws IOException {
        List<OrderEntry> result = readAndWriteToJson.readJsonFile(tempFile.toFile());

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @DisplayName(value = "readJsonFile() throws FileNotFoundException if it can't find the file")
    @Test
    public void readJsonFile_FileNotFoundException() throws IOException {

        Exception exception = assertThrows(FileNotFoundException.class, () -> {

            readAndWriteToJson.readJsonFile(new File("file.json"));
        });

        String expectedMessage = "File not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName(value = "writeToJsonFile() writes content")
    public void writeToJsonFile_writes() throws IOException {
        // Create a new OrderEntry to be written
        OrderEntry newOrderEntry = new OrderEntry(9833, "Apple", 300, 90, Side.SEll,
                Status.OPEN, LocalDateTime.of(2023, 9, 14, 13, 00));

        // Call the writeToJsonFile method
        readAndWriteToJson.writeToJsonFile(newOrderEntry, tempFile.toFile());

        // Verify that the file has been written
        assertTrue(tempFile.toFile().exists());

        // Read the content from the file
        String fileContent = Files.readString(tempFile);

        // Deserialize the JSON content back into a list of OrderEntry objects
        OrderEntry[] updateEntries = gsonBuilder.create().fromJson(fileContent, OrderEntry[].class);

        assertEquals(Arrays.asList(updateEntries).get(1).getOrder_id(), newOrderEntry.getOrder_id());
        assertEquals(Arrays.asList(updateEntries).get(1).getTimestamp(), newOrderEntry.getTimestamp());


    }

    @Test
    @DisplayName(value = "readJsonObjById() returns content")
    public void readJsonObjById_ReturnsIdContent() throws IOException {

        List<OrderEntry> result = readAndWriteToJson.readJsonObjById(91780, tempFile.toFile());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(result.get(0).getOrder_id(), orderEntry.getOrder_id());
    }


    @Test
    @DisplayName(value = "updateJsonObjById() updates content")
    public void updateJsonObjById_UpdatedContent() throws IOException {
        OrderEntry newOrderEntry = new OrderEntry(91780, "Apple", 300, 90, Side.SEll,
                Status.OPEN, LocalDateTime.of(2023, 9, 14, 5, 0));
        readAndWriteToJson.updateJsonObjById(91780, newOrderEntry, tempFile.toFile());

        assertTrue(tempFile.toFile().exists());

        String fileContent = Files.readString(tempFile);

        OrderEntry[] updateEntries = gsonBuilder.create().fromJson(fileContent, OrderEntry[].class);
        assertTrue(Arrays.asList(updateEntries).get(0).getSecurity_symbol() != "GOOG");

    }

    @Test
    @DisplayName(value = "deleteJsonObjById() deletes content")
    public void deleteJsonObjById_DeletesContent() throws IOException {

        readAndWriteToJson.deleteJsonObjById(91780, tempFile.toFile());

        assertTrue(tempFile.toFile().exists());

        String fileContent = Files.readString(tempFile);

        OrderEntry[] updateEntries = gsonBuilder.create().fromJson(fileContent, OrderEntry[].class);
        assertTrue(Arrays.asList(updateEntries).isEmpty());

    }

}
