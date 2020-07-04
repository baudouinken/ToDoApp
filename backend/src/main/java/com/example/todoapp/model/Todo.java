package com.example.todoapp.model;

import java.io.Serializable;
import java.util.Objects;

public class Todo implements Serializable{

    private long id;
    private String name;
    private String desc;
    private long dueDate;
    private boolean finished;
    private boolean favorite;


    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Todo updateFrom(Todo todo) {
        this.setName(todo.getName());
        this.setDesc(todo.getDesc());
        this.setDueDate(todo.getDueDate());
        this.setFavorite(todo.getFavorite());
        this.setFinished(todo.getFinished());

        return this;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", dueDate=" + dueDate +
                ", finished=" + finished +
                ", favorite=" + favorite +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id == todo.id &&
                dueDate == todo.dueDate &&
                finished == todo.finished &&
                favorite == todo.favorite &&
                Objects.equals(name, todo.name) &&
                Objects.equals(desc, todo.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, dueDate, finished, favorite);
    }
}
