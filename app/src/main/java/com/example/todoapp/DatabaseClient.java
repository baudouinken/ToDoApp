package com.example.todoapp;

import androidx.room.Room;
import android.content.Context;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx){
        this.mCtx = mCtx;

        // Database Name : Todos
        appDatabase = Room.databaseBuilder(mCtx,AppDatabase.class, "Todos").build();
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
