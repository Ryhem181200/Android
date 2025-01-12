package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employees;
    private Context context;
    private OnEmployeeClickListener listener;

    public interface OnEmployeeClickListener {
        void onDeleteClick(Employee employee);
        void onEditClick(Employee employee);
    }

    public EmployeeAdapter(Context context, List<Employee> employees, OnEmployeeClickListener listener) {
        this.context = context;
        this.employees = employees;
        this.listener = listener;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.name.setText(employee.getName());
        holder.phone.setText(employee.getPhone());
        holder.dob.setText(employee.getDob());
        holder.cardId.setText(employee.getCardId());

        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(employee));
        holder.editButton.setOnClickListener(v -> listener.onEditClick(employee));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    // Method to update data in the adapter
    public void updateData(List<Employee> newEmployees) {
        this.employees = newEmployees;
        notifyDataSetChanged();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, dob, cardId;
        Button deleteButton, editButton;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_employee_name);
            phone = itemView.findViewById(R.id.tv_employee_phone);
            dob = itemView.findViewById(R.id.tv_employee_dob);
            cardId = itemView.findViewById(R.id.tv_employee_card_id);
            deleteButton = itemView.findViewById(R.id.btn_delete_employee);
            editButton = itemView.findViewById(R.id.btn_edit_employee);
        }
    }
}
