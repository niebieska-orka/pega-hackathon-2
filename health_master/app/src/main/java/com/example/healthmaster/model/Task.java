package com.example.healthmaster.model;

import java.sql.Timestamp;

public class Task {
    private String id;
    private Timestamp deadline;
    private String name;
    private String description;
    private Status status;

    public void setXp(int xp) {
        this.xp = xp;
    }

    private int xp;
    byte[] image;

    public String getId() {
        return id;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setContent(byte[] contents) {
        image = contents;
    }

    public int getXp() {
        return xp;
    }
}
