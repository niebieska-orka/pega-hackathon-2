package com.example.healthmaster;

import com.example.healthmaster.model.Status;
import com.example.healthmaster.model.Task;

import java.util.List;

public class ParentSession {
    private ParentSession() {
    }

    private static ParentSession parentSession;
    public static ParentSession getInstance()
    {
        if(parentSession == null)
        {
            //CREATE SESSION
        }
        return parentSession;
    }
    private String id;
    private List<Task> childTasks;
    private List<Task> exampleTasks;

    public void confirm(Task task, boolean accepted) {
        if (accepted)
            task.setStatus(Status.COMPLETED);
        else
            task.setStatus(Status.FAILED);
        // TO DO
        // SEND TO SERVER
    }

    public void addTask(Task task) {
        // TO DO
        // SEND TO SERVER
    }

    public void logIn(String parentUsername) {
        //GET DATA FROM SERVER
    }

    public void register(String childUsername, String parentUsername) {
        //REGISTER AND GET DATA FROM SERVER
    }
}
