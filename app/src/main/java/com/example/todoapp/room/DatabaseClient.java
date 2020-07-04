package com.example.todoapp.room;

import androidx.room.Room;
import android.content.Context;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx){
        this.mCtx = mCtx;

        mCtx.deleteDatabase("todos");
        // scDatabase Name : Todos
        appDatabase = Room.databaseBuilder(mCtx,AppDatabase.class, "todos").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx){
        if (mInstance==null){
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase(){
        return appDatabase;
    }

}
