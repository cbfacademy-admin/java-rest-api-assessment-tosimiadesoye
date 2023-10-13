package com.cbfacademy.apiassessment.orderEntry;

import java.time.Instant;
import java.util.UUID;

public class OrderEntry {

    private UUID order_id;
    private String security_symbol;
    private long quantity;
    private long price;
    private String orderSide;
    private String status;
    private Instant timestamp;


    public OrderEntry(UUID order_id, String security_symbol, long quantity, long price, String orderSide, String status, Instant timestamp) {
        this.order_id = order_id;
        this.security_symbol = security_symbol;
        this.quantity = quantity;
        this.price = price;
        this.orderSide = orderSide;
        this.status = status;
        this.timestamp = timestamp;
    }

    public UUID getOrder_id() {
        return order_id;
    }

    public void setOrder_id(UUID order_id) {
        this.order_id = order_id;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timeStamp) {
        this.timestamp = timeStamp;
    }

    public String getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(String orderSide) {
        this.orderSide = orderSide;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderEntry{" +
                "order_id=" + order_id +
                ", security_symbol='" + security_symbol + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", orderSide='" + orderSide + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
