package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HRHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrhome_page);
    }

    // Method to open Add Employee Activity
    public void openAddEmployee(android.view.View view) {
        startActivity(new Intent(this, CreateEmployee.class));
    }

    // Method to open View Employees Activity
    public void openViewEmployees(android.view.View view) {
        startActivity(new Intent(this, ViewEmployeesActivity.class));
    }

    // Method to open Employee Logs Activity
    public void openEmployeeLogs(android.view.View view) {
        startActivity(new Intent(this, NfcLogActivity.class));
    }

    // Method to open Statistics Activity
    public void openStatistics(android.view.View view) {
        startActivity(new Intent(this, ViewLogsAllActivity.class));
    }
}
