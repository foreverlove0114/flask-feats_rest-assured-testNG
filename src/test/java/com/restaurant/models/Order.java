package com.restaurant.models;

import java.time.LocalDateTime;
import java.util.Map;

public class Order {
    private int id;
    private String orderList;
    private LocalDateTime orderTime;
    private int userId;
    private String status;

    public Order() {}

    public Order(String orderList, int userId) {
        this.orderList = orderList;
        this.userId = userId;
        this.orderTime = LocalDateTime.now();
        this.status = "pending";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getOrderList() { return orderList; }
    public void setOrderList(String orderList) { this.orderList = orderList; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}