package com.example.shiftscheduler.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.models.TimeoffModel;

import java.util.ArrayList;

public class TimeoffListAdapter extends RecyclerView.Adapter<TimeoffListAdapter.TimeoffListViewHolder> {
    private ArrayList<TimeoffModel> timeoffList;
    private OnTimeoffClickListener listener;

    public interface OnTimeoffClickListener {
        void onTimeoffClick(int position);
    }

    public void setOnTimeoffClickListener(OnTimeoffClickListener listener) {
        this.listener = listener;
    }

    public static class TimeoffListViewHolder extends RecyclerView.ViewHolder {
        public TextView employeeName;
        public TextView dateFrom;
        public TextView dateTo;
        public ImageButton deletebtn;

        public TimeoffListViewHolder(@NonNull View itemView, final OnTimeoffClickListener listener) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.employeeNameRequest);
            dateFrom = itemView.findViewById(R.id.dateFromList);
            dateTo = itemView.findViewById(R.id.dateToList);
            deletebtn = itemView.findViewById(R.id.deleteTimeoff);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onTimeoffClick(position);
                        }
                    }
                }
            });
        }
    }

    public TimeoffListAdapter(ArrayList<TimeoffModel> timeoffList) {
        this.timeoffList = timeoffList;
    }

    @NonNull
    @Override
    public TimeoffListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_timeoff_object, parent, false);
        TimeoffListViewHolder tlvh = new TimeoffListViewHolder(v, listener);
        return tlvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeoffListViewHolder holder, int position) {
        TimeoffModel currentTimeoff = timeoffList.get(position);

        holder.employeeName.setText(String.format("%s %s", currentTimeoff.getFName(), currentTimeoff.getLName()));
        holder.dateFrom.setText(currentTimeoff.getDateFrom().toString());
        holder.dateTo.setText(currentTimeoff.getDateTo().toString());
    }

    @Override
    public int getItemCount() {
        return timeoffList.size();
    }
}