package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.directory.EmpDao;
import com.example.myapplication.directory.LogDao;
import com.example.myapplication.entity.Employee;
import com.example.myapplication.entity.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NfcLogActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private EmpDao empDao;
    private LogDao logDao;
    private AppDatabase database;

    // Flag to prevent duplicate handling
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_log);

        // Initialize NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Room Database and DAOs
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "employee_database").build();
        empDao = database.empDao();
        logDao = database.logDao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableNfcForegroundDispatch();
        handleNfcIntent(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableNfcForegroundDispatch();
    }

    private void enableNfcForegroundDispatch() {
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    private void disableNfcForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Update the intent
        handleNfcIntent(intent);
    }

    private void handleNfcIntent(Intent intent) {
        if (intent != null && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()) && !isProcessing) {
            isProcessing = true; // Set flag to prevent duplicate handling
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag != null) {
                String cardId = bytesToHex(tag.getId());
                if (!TextUtils.isEmpty(cardId)) {
                    verifyAndLogEmployee(cardId);
                } else {
                    Toast.makeText(this, "Invalid card detected", Toast.LENGTH_SHORT).show();
                    isProcessing = false; // Reset flag
                }
            } else {
                isProcessing = false; // Reset flag
            }
        }
    }

    private void verifyAndLogEmployee(String cardId) {
        new Thread(() -> {
            try {
                // Fetch employee from database
                Employee employee = empDao.getEmployeeByCardId(cardId);

                if (employee != null) {
                    // Check if a log already exists for this employee and timestamp
                    String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    Log existingLog = logDao.getLogByEmployeeAndTimestamp(employee.getId(), currentTimestamp);

                    if (existingLog == null) {
                        logEmployeeAction(employee);
                    } else {
                        runOnUiThread(() -> Toast.makeText(NfcLogActivity.this, "Log already exists for this employee", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(NfcLogActivity.this, "No employee found for this card", Toast.LENGTH_SHORT).show());
                }
            } finally {
                isProcessing = false; // Reset flag
            }
        }).start();
    }

    private void logEmployeeAction(Employee employee) {
        Log log = new Log();
        log.setEmployeeId(employee.getId());
        log.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        new Thread(() -> {
            logDao.insertLog(log);
            runOnUiThread(() -> Toast.makeText(
                    NfcLogActivity.this,
                    "Log entry added for employee: " + employee.getName(),
                    Toast.LENGTH_SHORT
            ).show());
        }).start();
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
