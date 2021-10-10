package com.example.shiftscheduler.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.models.EmployeeModel;

import java.util.ArrayList;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeListViewHolder> {
    private ArrayList<EmployeeModel> employeeList;

    public static class EmployeeListViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView employeeName;

        public EmployeeListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.employeeAvatar_iv);
            employeeName = itemView.findViewById(R.id.employeeName_tv);
        }
    }

    public EmployeeListAdapter(ArrayList<EmployeeModel> employeeList) {
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_object, parent, false);
        EmployeeListViewHolder elvh = new EmployeeListViewHolder(v);
        return elvh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListViewHolder holder, int position) {
        EmployeeModel currentEmployee = employeeList.get(position);

        holder.imageView.setImageResource(R.drawable.ic_account_circle); //could become profile pic
        holder.employeeName.setText(String.format("%s, %s", currentEmployee.getLName(), currentEmployee.getFName()));
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }
}
