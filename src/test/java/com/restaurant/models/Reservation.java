package com.restaurant.models;

import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private String tableType;
    private LocalDateTime timeStart;
    private int userId;

    public Reservation() {}

    public Reservation(String tableType, LocalDateTime timeStart, int userId) {
        this.tableType = tableType;
        this.timeStart = timeStart;
        this.userId = userId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTableType() { return tableType; }
    public void setTableType(String tableType) { this.tableType = tableType; }

    public LocalDateTime getTimeStart() { return timeStart; }
    public void setTimeStart(LocalDateTime timeStart) { this.timeStart = timeStart; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}