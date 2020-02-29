package com.example.healthmaster.model;

import java.sql.Timestamp;

class Task
{
    private Integer id;
    private Timestamp deadline;
    private String name;
    private String description;
    private Status status;
    byte[] image;

    public int getId()
    {
        return id;
    }

    public void confirm(boolean accepted)
    {
        if(accepted)
            status = Status.COMPLETED;
        else
            status = Status.FAILED;
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
}
