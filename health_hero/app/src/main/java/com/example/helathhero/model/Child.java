package com.example.helathhero.model;

import java.util.List;

public class Child
{
    private String id;

    private List<Task> tasks;

    private int level;
    private int XP;

    private String characterName;
    private byte[] characterImage; // CAN BE CHANGED TO ANOTHER FILE FORMAT

    public byte[] getCharacterImage(){return characterImage;}

    public String getCharacterName(){return characterName;}

    public List<Task> getTasks(){return tasks;}

    public int getXP(){return XP;}

    public int getLevel(){return level;}

}