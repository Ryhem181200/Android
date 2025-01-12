package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.myapplication.TimestampConverter;

@Entity(
        tableName = "logs",
        foreignKeys = @ForeignKey(
                entity = Employee.class,
                parentColumns = "id",
                childColumns = "employeeId",
                onDelete = ForeignKey.CASCADE
        )
)
@TypeConverters(TimestampConverter.class)
public class Log {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int employeeId; // Foreign key referencing Employee table

    private String timestamp; // Log timestamp

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
