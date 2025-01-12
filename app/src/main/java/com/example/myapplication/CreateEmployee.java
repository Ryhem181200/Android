package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class CreateEmployee extends AppCompatActivity {

    private EditText etName, etPhone, etDob;
    private ImageView ivCalendar;
    private Button btnRfidVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);

        // Initialize Views
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob);
        ivCalendar = findViewById(R.id.iv_calendar);
        btnRfidVerification = findViewById(R.id.btn_rfid_verification);

        // Set OnClickListener for Calendar Icon
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        // Set OnClickListener for RFID Verification Button
        btnRfidVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRfidVerification();
            }
        });
    }

    private void openDatePicker() {
        // Get Current Date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                etDob.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void handleRfidVerification() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        // Validate Input
        if (name.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass Data to RFID Verification Activity (Optional)
        Intent intent = new Intent(this, RfidVerificationActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("dob", dob);
        startActivity(intent);

        // Show Success Message
        Toast.makeText(this, "Proceed to RFID Verification", Toast.LENGTH_SHORT).show();
    }
}
