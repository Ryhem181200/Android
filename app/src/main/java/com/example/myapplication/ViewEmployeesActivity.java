package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.directory.EmpDao;
import com.example.myapplication.entity.Employee;

import java.util.ArrayList;
import java.util.List;

public class ViewEmployeesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private EditText filterName;
    private ImageButton btnSearch;
    private EmpDao empDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employees);

        recyclerView = findViewById(R.id.recycler_view_employees);
        filterName = findViewById(R.id.et_filter_name);
        btnSearch = findViewById(R.id.btn_search);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database and DAO
        empDao = AppDatabase.getInstance(this).empDao();

        // Load all employees
        loadEmployees();

        // Filter employees by name
        btnSearch.setOnClickListener(v -> {
            String name = filterName.getText().toString();
            new Thread(() -> {
                List<Employee> employees = empDao.getAllEmployees();
                List<Employee> filteredEmployees = filterByName(employees, name);
                runOnUiThread(() -> {
                    employeeAdapter.updateData(filteredEmployees);
                });
            }).start();
        });
    }

    private void loadEmployees() {
        new Thread(() -> {
            List<Employee> employees = empDao.getAllEmployees();
            runOnUiThread(() -> {
                if (employees != null && !employees.isEmpty()) {
                    employeeAdapter = new EmployeeAdapter(this, employees, new EmployeeAdapter.OnEmployeeClickListener() {
                        @Override
                        public void onDeleteClick(Employee employee) {
                            new Thread(() -> {
                                empDao.deleteEmployee(employee);
                                runOnUiThread(() -> {
                                    Toast.makeText(ViewEmployeesActivity.this, "Employee deleted", Toast.LENGTH_SHORT).show();
                                    loadEmployees();  // Reload employees after delete
                                });
                            }).start();
                        }

                        @Override
                        public void onEditClick(Employee employee) {
                            Intent intent = new Intent(ViewEmployeesActivity.this, EditEmployeeActivity.class);
                            intent.putExtra("employee_id", employee.getId());
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(employeeAdapter);
                } else {
                    Log.e("ViewEmployeesActivity", "No employees found in the database");
                }
            });
        }).start();
    }

    private List<Employee> filterByName(List<Employee> employees, String name) {
        List<Employee> filteredEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getName().toLowerCase().contains(name.toLowerCase())) {
                filteredEmployees.add(employee);
            }
        }
        return filteredEmployees;
    }
}
