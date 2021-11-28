package com.example.shiftscheduler.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.EmployeeModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<EmployeeModel> employeeList;
    private OnEmployeeClickListener listener;
    private int layoutType;
    private DatabaseHelper dbHelper;
    private LocalDate date;

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
            qualification = itemView.findViewById(R.id.qualificationAvailableDetails);

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
            qualification = itemView.findViewById(R.id.qualificationScheduledDetails);

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

    public static class EmployeeListCalViewHolder extends RecyclerView.ViewHolder {
        public TextView employeeName;
        public TextView qualification;
        public TextView qualificationDetails;
        public TextView shiftLabel;
        public TextView shiftLabelDetails;

        public EmployeeListCalViewHolder(@NonNull View itemView, final OnEmployeeClickListener listener) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.calEmployeeLabel);
            qualification = itemView.findViewById(R.id.calQualifications);
            qualificationDetails = itemView.findViewById(R.id.calQualificationsDetails);
            shiftLabel = itemView.findViewById(R.id.calShiftLabel);
            shiftLabelDetails = itemView.findViewById(R.id.calShiftDetails);

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

    public EmployeeListAdapter(ArrayList<EmployeeModel> employeeList, DatabaseHelper dbHelper, LocalDate date, int layoutType) {
        this.employeeList = employeeList;
        this.layoutType = layoutType;
        this.dbHelper = dbHelper;
        this.date = date;
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
            case 3:
                View employeeCalObject = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_cal_object, parent, false);
                EmployeeListCalViewHolder elcvh = new EmployeeListCalViewHolder(employeeCalObject, listener);
                return elcvh;
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
                if (!currentEmployee.getStatus()) elvh.itemView.setAlpha((float)0.5);
                else elvh.itemView.setAlpha((float)1);
                break;
            case 1:
                AvailableWeekdayViewHolder awvh = (AvailableWeekdayViewHolder) holder;
                awvh.employeeName.setText(String.format("%s, %s", currentEmployee.getLName(), currentEmployee.getFName()));
                String qualificationDetails = " ";
                List<Boolean> qualifications = currentEmployee.getQualifications();
                if (qualifications != null && qualifications.get(0)) {
                    qualificationDetails += "Opening ";
                }
                if (qualifications != null && qualifications.get(1)) {
                    qualificationDetails += "Closing";
                }
                if (qualifications != null && qualifications.get(1) == false && qualifications.get(0) == false) {
                    qualificationDetails += "None";
                }
                awvh.qualification.setText(qualificationDetails);
                break;
            case 2:
                ScheduledWeekdayViewHolder swvh = (ScheduledWeekdayViewHolder) holder;
                swvh.employeeName.setText(String.format("%s, %s", currentEmployee.getLName(), currentEmployee.getFName()));
                String qualificationDetails2 = " ";
                List<Boolean> qualifications2 = currentEmployee.getQualifications();
                if (qualifications2 != null && qualifications2.get(0)) {
                    qualificationDetails2 += "Opening ";
                }
                if (qualifications2 != null && qualifications2.get(1)) {
                    qualificationDetails2 += "Closing";
                }
                if (qualifications2 != null && qualifications2.get(1) == false && qualifications2.get(0) == false) {
                    qualificationDetails2 += "None";
                }
                swvh.qualification.setText(qualificationDetails2);
                break;
            case 3:
                EmployeeListCalViewHolder elcvh = (EmployeeListCalViewHolder) holder;
                //Employee Name
                elcvh.employeeName.setText(String.format("%s, %s", currentEmployee.getLName(), currentEmployee.getFName()));
                if (!currentEmployee.getStatus()) elcvh.itemView.setAlpha((float)0.5);
                else elcvh.itemView.setAlpha((float)1);
                //Qualifications
                String qualificationDetails3 = " ";
                List<Boolean> qualifications3 = currentEmployee.getQualifications();
                if (qualifications3 != null && qualifications3.get(0)) {
                    qualificationDetails3 += "Opening ";
                }
                if (qualifications3 != null && qualifications3.get(1)) {
                    qualificationDetails3 += "Closing";
                }
                if (qualifications3 != null && qualifications3.get(1) == false && qualifications3.get(0) == false) {
                    qualificationDetails3 += "None";
                }
                elcvh.qualificationDetails.setText(qualificationDetails3);
                //Shift
                String shiftType = dbHelper.getShiftType(currentEmployee.getEmployeeID(), date);
                elcvh.shiftLabelDetails.setText(shiftType.toUpperCase());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }
}
