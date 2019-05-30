package com.example.assignment;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Steps {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "steps")
    public int steps;

    @ColumnInfo(name = "time")
    public String time;

    public Steps(int steps, String time) {
        this.steps = steps;
        this.time = time;
    }

    public int getId() {
        return this.id;
    }

    public int getSteps() {
        return this.steps;
    }

    public String getTime() {
        return this.time;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
