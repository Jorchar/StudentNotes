package com.jkucharski.studentnotes;

import android.media.Image;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "subject_list")
public class SubjectDC implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    private String name;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SubjectDC() {
    }
}
