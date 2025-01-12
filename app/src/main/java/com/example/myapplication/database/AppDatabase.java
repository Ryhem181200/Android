package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.directory.EmpDao;
import com.example.myapplication.directory.LogDao;
import com.example.myapplication.entity.Employee;
import com.example.myapplication.entity.Log;


@Database(entities = {Employee.class ,  Log.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract EmpDao empDao();
    public abstract LogDao logDao();


    // Singleton pattern to ensure that only one instance of the database exists
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "employee_database")
                            .fallbackToDestructiveMigration() // Handle migration if schema changes
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
