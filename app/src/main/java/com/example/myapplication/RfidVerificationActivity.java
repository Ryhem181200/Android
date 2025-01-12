package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.entity.Employee;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RfidVerificationActivity extends AppCompatActivity {

    private TextView tvRfidStatus, tvRfidCode;
    private Button btnSkipRfid;

    private NfcAdapter nfcAdapter;
    private String employeeName, employeePhone, employeeDob;
    private String rfidCode = "00000"; // Default if skipped

    private AppDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid_verification);

        // Initialize Views
        tvRfidStatus = findViewById(R.id.tv_rfid_status);
        tvRfidCode = findViewById(R.id.tv_rfid_code);
        btnSkipRfid = findViewById(R.id.btn_skip_rfid);

        // Retrieve Data from Intent
        Intent intent = getIntent();
        employeeName = intent.getStringExtra("name");
        employeePhone = intent.getStringExtra("phone");
        employeeDob = intent.getStringExtra("dob");

        // Initialize Room Database and ExecutorService
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "employee_database").build();
        executorService = Executors.newSingleThreadExecutor();

        // Initialize NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Skip Button Listener
        btnSkipRfid.setOnClickListener(v -> {
            Toast.makeText(this, "RFID skipped, using default code", Toast.LENGTH_SHORT).show();
            addEmployeeToDatabase(rfidCode); // Add employee with default RFID code
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Enable NFC Foreground Dispatch
        enableNfcForegroundDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Disable NFC Foreground Dispatch
        disableNfcForegroundDispatch();
    }

    private void enableNfcForegroundDispatch() {
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    private void disableNfcForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handle NFC Tag Detection
        handleNfcIntent(intent);
    }

    private void handleNfcIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            // Get RFID Code (Tag ID)
            rfidCode = bytesToHex(tag.getId());
            tvRfidStatus.setText("RFID Detected!");
            tvRfidCode.setText("RFID Code: " + rfidCode);

            // Add Employee with Scanned RFID Code
            addEmployeeToDatabase(rfidCode);
        } else {
            tvRfidStatus.setText("RFID Not Detected. Try Again.");
        }
    }

    private void addEmployeeToDatabase(String rfidCode) {
        // Create Employee Object
        Employee employee = new Employee();
        employee.setName(employeeName);
        employee.setPhone(employeePhone);
        employee.setDob(employeeDob);
        employee.setCardId(rfidCode);

        // Insert Employee into Database
        executorService.execute(() -> {
            database.empDao().insertUser(employee);
            runOnUiThread(() -> {
                Toast.makeText(this, "Employee added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
