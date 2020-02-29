package com.example.helathhero.model;

import java.sql.Timestamp;

class Task
{
    private Integer id;
    private Timestamp deadline;
    private String name;
    private String description;
    private Status status;
    private int XP;

    public int getId()
    {
        return id;
    }

    public void completeTask(int taskId, byte[] image)
    {
        // TO DO
        // SEND TO SERVER
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

    public int getXP(){return XP;}
}
