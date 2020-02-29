package com.example.healthmaster.model;

import java.sql.Timestamp;

public class Task
{
    private String id;
    private Timestamp deadline;
    private String name;
    private String description;
    private Status status;
    byte[] image;

    public String getId()
    {
        return id;
    }

    public Timestamp getDeadline()
    {
        return deadline;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName(){return name;}

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
