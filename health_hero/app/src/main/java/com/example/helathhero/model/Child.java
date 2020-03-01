package com.example.helathhero.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Child {
    private String id;

    private Map<String, Task> tasks = new HashMap<>();

    private int level;
    private int XP;

    private String characterName;
    private byte[] characterImage; // CAN BE CHANGED TO ANOTHER FILE FORMAT

    public byte[] getCharacterImage() {
        return characterImage;
    }

    public String getCharacterName() {
        return characterName;
    }

    public synchronized void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public synchronized int getXP() {
        return XP;
    }

    public synchronized int getLevel() {
        return level;
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized void setId(String id) {
        this.id = id;
    }

    public synchronized void addTask(String id, Timestamp deadline, String name, String description, int xp, Status status) {
        Task task = new Task(id, deadline, name, description, xp, status);
        tasks.put(id, task);
    }

    public synchronized Task getTask(String id) {
        return tasks.get(id);
    }

    public void setLevel(int newLevel) {
        this.level = newLevel;
    }

    public void setXp(int xp) {
        this.XP = xp;
    }
}