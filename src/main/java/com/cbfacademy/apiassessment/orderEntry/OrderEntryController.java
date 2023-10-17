package com.cbfacademy.apiassessment.orderEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orderEntry")
public class OrderEntryController {

    @Autowired
    private final OrderEntryService orderEntryService;

    public OrderEntryController(OrderEntryService orderEntryService) {
        this.orderEntryService = orderEntryService;
    }

    @GetMapping
    public ResponseEntity<List<OrderEntry>> readOrderEntryApi() throws IOException {
        try {
            List<OrderEntry> entries = orderEntryService.getOrderEntry();
            return ResponseEntity.ok(entries);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderEntry>> readOrderEntryByIdApi(@PathVariable long id) {
        try {
            List<OrderEntry> entry = orderEntryService.getOrderEntryById(id);
            if (entry.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(entry);
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping
    public ResponseEntity<Void> createOrderEntryApi(@RequestBody OrderEntry orderEntry) {

        try {
            orderEntryService.createOrderEntry(orderEntry);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrderEntryByIdApi(@PathVariable long id, @RequestBody OrderEntry orderEntry) {

        try {
            orderEntryService.updateOrderEntryById(id, orderEntry);
            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderEntryByIdApi(@PathVariable long id) {
        try {
            orderEntryService.deleteOrderEntryById(id);
            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
