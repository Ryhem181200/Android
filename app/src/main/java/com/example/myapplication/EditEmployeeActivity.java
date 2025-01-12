package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.directory.EmpDao;
import com.example.myapplication.entity.Employee;

public class EditEmployeeActivity extends AppCompatActivity {

    private EditText etName, etPhone, etDob, etCardId;
    private Button btnSave, btnCancel;
    private EmpDao empDao;
    private int employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        // Initialize UI components
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob);
        etCardId = findViewById(R.id.et_card_id);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        // Initialize database and DAO
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "employee_database").allowMainThreadQueries().build();
        empDao = database.empDao();

        // Get the employee ID passed from the previous activity
        employeeId = getIntent().getIntExtra("employee_id", -1);

        // Load employee data
        loadEmployeeData();

        // Save button click listener
        btnSave.setOnClickListener(v -> saveEmployeeData());

        // Cancel button click listener
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadEmployeeData() {
        if (employeeId != -1) {
            Employee employee = empDao.getEmployeeById(employeeId);
            if (employee != null) {
                etName.setText(employee.getName());
                etPhone.setText(employee.getPhone());
                etDob.setText(employee.getDob());
                etCardId.setText(employee.getCardId());
            } else {
                Toast.makeText(this, "Employee not found!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid Employee ID!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveEmployeeData() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String cardId = etCardId.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || dob.isEmpty() || cardId.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName(name);
        employee.setPhone(phone);
        employee.setDob(dob);
        employee.setCardId(cardId);

        empDao.updateEmployee(employee);
        Toast.makeText(this, "Employee updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
