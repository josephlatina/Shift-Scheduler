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

public class EmployeeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    public static class AvailableWeekdayViewHolder extends RecyclerView.ViewHolder {
        public ImageView addBtn;
        public TextView employeeName;
        public TextView qualification;

        public AvailableWeekdayViewHolder(@NonNull View itemView, final OnEmployeeClickListener listener) {
            super(itemView);
            addBtn = itemView.findViewById(R.id.availableAddButton);
            employeeName = itemView.findViewById(R.id.weekdayAvailable);
            qualification = itemView.findViewById(R.id.qualificationAvailable);

            addBtn.setOnClickListener(new View.OnClickListener() {
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

    public static class ScheduledWeekdayViewHolder extends RecyclerView.ViewHolder {
        public ImageView deleteBtn;
        public TextView employeeName;
        public TextView qualification;

        public ScheduledWeekdayViewHolder(@NonNull View itemView, final OnEmployeeClickListener listener) {
            super(itemView);
            deleteBtn = itemView.findViewById(R.id.scheduledDeleteButton);
            employeeName = itemView.findViewById(R.id.weekdayScheduled);
            qualification = itemView.findViewById(R.id.qualificationScheduled);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (layoutType) {
            case 0:
                View employeeObject = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_object, parent, false);
                EmployeeListViewHolder elvh = new EmployeeListViewHolder(employeeObject, listener);
                return elvh;
            case 1:
                View weekdayObject = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_available_object, parent, false);
                AvailableWeekdayViewHolder awvh = new AvailableWeekdayViewHolder(weekdayObject, listener);
                return awvh;
            case 2:
                View weekdaySchedObject = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_scheduled_object, parent, false);
                ScheduledWeekdayViewHolder swvh = new ScheduledWeekdayViewHolder(weekdaySchedObject, listener);
                return swvh;
        }
        return null;
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EmployeeModel currentEmployee = employeeList.get(position);

        switch (layoutType) {
            case 0:
                EmployeeListViewHolder elvh = (EmployeeListViewHolder) holder;
                elvh.imageView.setImageResource(R.drawable.ic_account_circle); //could become profile pic
                elvh.employeeName.setText(String.format("%s, %s", currentEmployee.getLName(), currentEmployee.getFName()));
                break;
            case 1:
                AvailableWeekdayViewHolder awvh = (AvailableWeekdayViewHolder) holder;
                awvh.employeeName.setText(String.format("%s, %s", currentEmployee.getLName(), currentEmployee.getFName()));
                break;
            case 2:
                ScheduledWeekdayViewHolder swvh = (ScheduledWeekdayViewHolder) holder;
                swvh.employeeName.setText(String.format("%s, %s", currentEmployee.getLName(), currentEmployee.getFName()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }
}
