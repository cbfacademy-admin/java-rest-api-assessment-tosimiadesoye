package com.cbfacademy.apiassessment.Json;

import com.cbfacademy.apiassessment.orderEntry.OrderEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReadAndWriteToJson {
  private final Gson gson;

    public ReadAndWriteToJson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeUKTypeAdapter())
                .create();
    }

    public List<OrderEntry> readJsonFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File not found");
        }
        try (FileReader reader = new FileReader(file)) {

            TypeToken<List<OrderEntry>> entryType = new TypeToken<>() {
            };

            return gson.fromJson(reader, entryType);
        }
    }

    public void writeToJsonFile(OrderEntry reqBody, File file) throws IOException {
        List<OrderEntry> orderEntries = readJsonFile(file);
        orderEntries.add(reqBody);

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(orderEntries, writer);
        }

    }

    public List<OrderEntry> readJsonObjById(long id, File file) throws IOException {
        List<OrderEntry> data = readJsonFile(file);
        return data.stream()
                .filter(item -> item.getOrder_id() == id)
                .collect(Collectors.toList());
    }

    public void updateJsonObjById(long id, OrderEntry reqBody, File file) throws IOException {

        List<OrderEntry> orderEntries = readJsonFile(file);

        // Find and update the entry with the matching order_id
        for (OrderEntry item : orderEntries) {
            if (item.getOrder_id() == id) {
                item.setSecurity_symbol(reqBody.getSecurity_symbol());
                item.setQuantity(reqBody.getQuantity());
                item.setOrderSide(reqBody.getOrderSide());
                item.setPrice(reqBody.getPrice());
                item.setStatus(reqBody.getStatus());
                item.setTimestamp(reqBody.getTimestamp());
            }
        }

        try (Writer writer = new FileWriter(file)) {
            // Write the entire List (with updated data) back to the file
            gson.toJson(orderEntries, writer);
        }
    }

    public void deleteJsonObjById(long id, File file) throws IOException {

        List<OrderEntry> orderEntries = readJsonFile(file);

        List<OrderEntry> updateOrderEntries = orderEntries.stream()
                .filter(entry -> entry.getOrder_id() != id)
                .collect(Collectors.toList());

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(updateOrderEntries, writer);
        }

    }

}
