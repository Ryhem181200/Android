package com.example.myapplication.directory;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entity.Employee;

import java.util.List;

@Dao
public interface EmpDao {
    @Insert
    void insertUser(Employee employee);
    @Query("SELECT * FROM employees WHERE id = :employeeId")
    Employee getEmployeeById(int employeeId);
    @Query("SELECT * FROM employees WHERE cardId = :cardId")
    Employee getEmployeeByCardId(String cardId);

    @Query("SELECT * FROM employees")
    List<Employee> getAllEmployees();
    @Delete
    void deleteEmployee(Employee employee);

    @Update
    void updateEmployee(Employee employee);
}
