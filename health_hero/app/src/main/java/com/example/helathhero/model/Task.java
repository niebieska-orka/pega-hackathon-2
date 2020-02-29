package com.example.helathhero.model;

import java.sql.Timestamp;

public class Task {
    private String id;
    private Timestamp deadline;
    private String name;
    private String description;
    private Status status;
    private int xp;

    Task(String id, Timestamp deadline, String name, String description, int xp, Status status) {
        this.id = id;
        this.deadline = deadline;
        this.status = Status.TO_DO;
        this.name = name;
        this.description = description;
        this.xp = xp;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void completeTask(byte[] image) {
        // TO DO
        // SEND TO SERVER
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public synchronized void setStatus(Status status) {
        this.status = status;
    }

    public int getXp() {
        return xp;
    }
}
