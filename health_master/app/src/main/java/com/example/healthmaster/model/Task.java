package com.example.healthmaster.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Task implements Serializable {
    private String id;
    private Timestamp deadline;
    private String name;
    private String description;
    private Status status;

    public void setXp(int xp) {
        this.xp = xp;
    }

    public Task(String id, Timestamp deadline, String name, String description, int xp) {
        this.id = id;
        this.deadline = deadline;
        this.name = name;
        this.description = description;
        this.xp = xp;
    }

    public Task(Timestamp deadline, String name, String description, int xp) {
        this.deadline = deadline;
        this.name = name;
        this.description = description;
        this.xp = xp;
        this.id = String.valueOf(deadline) + name;
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

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getContent() {
        return this.image;
    }
}
