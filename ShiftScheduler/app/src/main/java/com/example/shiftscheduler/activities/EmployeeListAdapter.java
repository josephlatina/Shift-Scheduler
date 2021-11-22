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
    private OnEmployeeClickListener listener;
    private int layoutType;

    public interface OnEmployeeClickListener {
        void onEmployeeClick(int position);
    }

    public void setOnEmployeeClickListener(OnEmployeeClickListener listener) {
        this.listener = listener;
    }

    public static class EmployeeListViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView employeeName;

        public EmployeeListViewHolder(@NonNull View itemView, final OnEmployeeClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.employeeAvatar_iv);
            employeeName = itemView.findViewById(R.id.employeeName_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEmployeeClick(position);
                        }
                    }
                }
            });
        }
    }

    public EmployeeListAdapter(ArrayList<EmployeeModel> employeeList, int layoutType) {
        this.employeeList = employeeList;
        this.layoutType = layoutType;
    }

    @NonNull
    @Override
    public EmployeeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (layoutType) {
            case 0:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_object, parent, false);
                EmployeeListViewHolder elvh = new EmployeeListViewHolder(v, listener);
                return elvh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListViewHolder holder, int position) {
        switch (layoutType) {
            case 0:
                EmployeeModel currentEmployee = employeeList.get(position);

                holder.imageView.setImageResource(R.drawable.ic_account_circle); //could become profile pic
                holder.employeeName.setText(String.format("%s, %s", currentEmployee.getLName(), currentEmployee.getFName()));
        }
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }
}
