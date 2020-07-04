package com.example.todoapp.model;

import androidx.room.*;
import java.util.List;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM todo ORDER BY finished, dueDate ASC")
    List<Todo> getAll();

    @Query("SELECT * FROM todo ORDER BY finished, dueDate ASC")
    List<Todo> getAllWithDate();

    @Query("SELECT * FROM todo ORDER BY finished, favorite DESC")
    List<Todo> getAllWithFavorite();

    @Insert
    long insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Update
    void update(Todo todo);

}
