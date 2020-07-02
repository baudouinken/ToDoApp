package com.example.todoapp;

import androidx.room.Room;
import android.content.Context;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    private AppDatabase appDatabase;

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE todos RENAME COLUMN task TO name");
//        }
//    };

    private DatabaseClient(Context mCtx){
        this.mCtx = mCtx;

        //mCtx.deleteDatabase("todos");
        // Database Name : Todos
        appDatabase = Room.databaseBuilder(mCtx,AppDatabase.class, "todos").build();

        //appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "Todos")
        //        .addMigrations(MIGRATION_1_2).build();
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
