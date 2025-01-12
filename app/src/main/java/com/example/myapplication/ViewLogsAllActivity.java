package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.directory.LogDao;
import com.example.myapplication.entity.Log;

import java.util.List;

public class ViewLogsAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LogAdapter logAdapter;
    private LogDao logDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs_all);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_logs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database and DAO
        AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "employee_database").build();
        logDao = database.logDao();

        // Fetch logs from the database
        fetchLogs();
    }

    private void fetchLogs() {
        new Thread(() -> {
            List<Log> logs = logDao.getAllLogs();
            runOnUiThread(() -> {
                if (logs != null && !logs.isEmpty()) {
                    logAdapter = new LogAdapter(logs);
                    recyclerView.setAdapter(logAdapter);
                } else {
                    Toast.makeText(ViewLogsAllActivity.this, "No logs available.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
