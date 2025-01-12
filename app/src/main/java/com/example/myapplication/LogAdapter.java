package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entity.Log;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private List<Log> logList;

    public LogAdapter(List<Log> logList) {
        this.logList = logList;
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        Log log = logList.get(position);
        holder.tvEmployeeId.setText("Employee ID: " + log.getEmployeeId());
        holder.tvTimestamp.setText("Timestamp: " + log.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmployeeId;
        TextView tvTimestamp;

        public LogViewHolder(View itemView) {
            super(itemView);
            tvEmployeeId = itemView.findViewById(R.id.tv_employee_id);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }
}
