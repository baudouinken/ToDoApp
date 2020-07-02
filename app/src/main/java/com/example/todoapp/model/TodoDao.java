package com.example.todoapp.model;

import androidx.room.*;
import com.example.todoapp.model.Todo;

import java.util.List;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM todo")
    List<Todo> getAll();

    @Insert
    void insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Update
    void update(Todo todo);

}
