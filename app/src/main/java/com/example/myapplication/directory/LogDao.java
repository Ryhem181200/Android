package com.example.myapplication.directory;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.entity.Log;

import java.util.List;

@Dao
public interface LogDao {

    // Insert a new log entry
    @Insert
    void insertLog(Log log);
    @Query("SELECT * FROM logs")
    List<Log> getAllLogs();

    // Get all logs for an employee
    @Query("SELECT * FROM logs WHERE employeeId = :employeeId")
    List<Log> getLogsByEmployeeId(int employeeId);

    @Query("SELECT * FROM logs WHERE employeeId = :employeeId AND timestamp = :timestamp LIMIT 1")
    Log getLogByEmployeeAndTimestamp(int employeeId, String timestamp);
}
