package com.example.healthmaster.model;

import java.util.List;

public class Child
{
    private String id;
    private List<Task> tasks;


    public void completeTask(int id, byte[] image)
    {
        // SEND REQUEST TO SERVER
    }

    public List<Task> getTasks(){return tasks;}
}