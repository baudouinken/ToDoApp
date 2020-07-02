package com.example.todoapp;

import androidx.room.*;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

}
