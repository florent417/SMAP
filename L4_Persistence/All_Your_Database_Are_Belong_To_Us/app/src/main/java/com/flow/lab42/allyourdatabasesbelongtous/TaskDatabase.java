package com.flow.lab42.allyourdatabasesbelongtous;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 3)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase dbInstance;
    private static final String DATABASE_NAME = "task-database";

    public abstract TaskDAO taskDAO();

    public synchronized TaskDatabase getDbInstance(Context context){
        if (dbInstance == null){
            dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return dbInstance;
    }
}
