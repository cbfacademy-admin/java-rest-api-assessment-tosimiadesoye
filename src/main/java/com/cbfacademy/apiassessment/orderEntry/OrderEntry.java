package com.cbfacademy.apiassessment.orderEntry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


@Entity
public class OrderEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long order_id;

    @NotBlank(message = "Security symbol is mandatory")
    private String security_symbol;

    @NotBlank(message = "Quantity is mandatory")
    @Min(value = 1, message = "Quantity cannot be less than 1")
    private long quantity;

    @NotBlank(message = "Price is mandatory")
    @Min(value = 1, message = "Price cannot be less than 1")
    private long price;

    @NotBlank(message = "Side is mandatory")
    private Side orderSide;

    @NotBlank(message = "Status is mandatory")
    private Status status;

    //    todo - this should validate that the date is Uk format "dd/MM/yyyy HH:mm:ss"
    @NotNull(message = "Timestamp must not be null")
    private LocalDateTime timestamp;


    public OrderEntry(long order_id, String security_symbol, long quantity, long price, Side orderSide,
                      Status status, LocalDateTime timestamp) {
        this.order_id = order_id;
        this.security_symbol = security_symbol;
        this.quantity = quantity;
        this.price = price;
        this.orderSide = orderSide;
        this.status = status;
        this.timestamp = timestamp;
    }

    public long getOrder_id() {
        return order_id;
    }

    public String getSecurity_symbol() {
        return security_symbol;
    }

    public void setSecurity_symbol(String security_symbol) {
        this.security_symbol = security_symbol;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(LocalDateTime timeStamp) {
        this.timestamp = timeStamp;
    }

    public Side getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(Side orderSide) {
        this.orderSide = orderSide;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("OrderEntry{order_id=%d, security_symbol='%s', quantity=%d, price=%d, orderSide='%s', status='%s', timestamp=%s}",
                order_id, security_symbol, quantity, price, orderSide, status, timestamp);
    }

    public enum Side {
        BUY,
        SEll
    }

    public enum Status {
        OPEN,
        PENDING,
        FILLED,
        CANCELED

    }
}
