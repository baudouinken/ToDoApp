package com.example.todoapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.todoapp.model.Todo;

@Database(entities = {Todo.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase{
    public abstract TodoDao todoDao();
}
