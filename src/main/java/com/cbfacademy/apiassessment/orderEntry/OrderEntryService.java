package com.cbfacademy.apiassessment.orderEntry;

import com.cbfacademy.apiassessment.Json.ReadAndWriteToJson;
import org.springframework.stereotype.Service;

import java.io.File;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;


@Service
public class OrderEntryService {
    private static final String DATA_FILE_PATH = "src/main/java/com/cbfacademy/apiassessment/resources/data.json";
    private final ReadAndWriteToJson jsonUtil;

    public OrderEntryService(ReadAndWriteToJson jsonUtil) {
        this.jsonUtil = jsonUtil;
    }

    public Collection<OrderEntry> getOrderEntry() throws IOException {
        return jsonUtil.readJsonFile(new File(DATA_FILE_PATH));
    }

    public Collection<OrderEntry> getOrderEntryById(UUID id) throws IOException {

        Collection<OrderEntry> entry = jsonUtil.readJsonObjById(id, new File(DATA_FILE_PATH));
        if (entry.isEmpty()) {
            throw new NotFoundException("OrderEntry not found with ID: " + id);
        }
        return entry;

    }

    public void createOrderEntry(OrderEntry orderEntry) throws IOException {
        jsonUtil.writeToJsonFile(orderEntry, new File(DATA_FILE_PATH));
    }

    public void updateOrderEntryById(UUID id, OrderEntry orderEntry) throws IOException {
        jsonUtil.updateJsonObjById(id, orderEntry, new File(DATA_FILE_PATH));

    }

    public void deleteOrderEntryById(UUID id) throws IOException {
        jsonUtil.deleteJsonObjById(id, new File(DATA_FILE_PATH));
    }

}
