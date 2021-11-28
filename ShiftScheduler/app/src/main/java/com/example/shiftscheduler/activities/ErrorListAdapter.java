package com.example.shiftscheduler.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.models.ErrorModel;

import java.util.ArrayList;

public class ErrorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ErrorModel> errorList;
    private OnErrorClickListener listener;

    public interface OnErrorClickListener {
        void onErrorClick(int position);
    }

    public void setOnErrorClickListener(ErrorListAdapter.OnErrorClickListener listener) {
        this.listener = listener;
    }

    public static class ErrorListViewHolder extends RecyclerView.ViewHolder {
        public TextView warningLabel;
        public TextView warningDetails;

        public ErrorListViewHolder(@NonNull View itemView, final ErrorListAdapter.OnErrorClickListener listener) {
            super(itemView);
            warningLabel = itemView.findViewById(R.id.calWarningLabel);
            warningDetails = itemView.findViewById(R.id.calWarningDetails);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onErrorClick(position);
                        }
                    }
                }
            });
        }
    }


    public ErrorListAdapter(ArrayList<ErrorModel> errorList) {
        this.errorList = errorList;
    }

    @NonNull
    @Override
    public ErrorListAdapter.ErrorListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.error_cal_object, parent, false);
        ErrorListAdapter.ErrorListViewHolder evh = new ErrorListAdapter.ErrorListViewHolder(v, listener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ErrorModel currentError = errorList.get(position);

        ErrorListViewHolder evh = (ErrorListViewHolder) holder;
        evh.warningDetails.setText(currentError.getDetails());
    }

    @Override
    public int getItemCount() {
        return errorList.size();
    }

}
