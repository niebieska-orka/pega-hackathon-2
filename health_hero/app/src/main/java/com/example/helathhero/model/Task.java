package com.example.helathhero.model;

import java.sql.Timestamp;

class Task
{
    private Integer id;
    private Timestamp deadline;
    private String contents;
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

    public String getContents()
    {
        return contents;
    }

    public Status getStatus() {
        return status;
    }

    public int getXP(){return XP;}
}
