package com.jkucharski.studentnotes.model;

import android.graphics.Color;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "subject_list")
public class SubjectDC implements Serializable {

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private Integer color = Color.parseColor("#339933");
    private Boolean active = true;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public SubjectDC() {
    }
}
