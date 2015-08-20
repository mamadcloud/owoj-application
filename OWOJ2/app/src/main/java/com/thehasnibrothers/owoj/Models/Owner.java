package com.thehasnibrothers.owoj.Models;

/**
 * Created by user on 16/4/2015.
 */
public class Owner {
    String name;
    int id;
    int juz;

    public Owner(){}

    public void setName(String value)
    {
        name = value;
    }

    public String getName()
    {
        return name;
    }

    public void setID(int value) { id = value; }

    public int getID() { return id; }

    public void setJuz(int value) { juz = value; }

    public int getJuz() { return juz; }
}
