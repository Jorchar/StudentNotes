package com.jkucharski.studentnotes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "note_list")
public class NoteDC implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    String name;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NoteDC(String name) {
        super();
        this.name = name;
    }
}
