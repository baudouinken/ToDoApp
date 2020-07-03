package com.example.todoapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Todo implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "desc")
    private String desc;

    @ColumnInfo(name = "dueDate")
    private long dueDate;

    @ColumnInfo(name = "finished")
    private boolean finished;

    @ColumnInfo(name = "favorite")
    private boolean favorite;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getDueDate() {  return dueDate;  }

    public void setDueDate(long dueDate) {  this.dueDate = dueDate;  }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean getFavorite() { return favorite;  }

    public void setFavorite(boolean favorite) {  this.favorite = favorite;  }
}
