package com.example.shiftscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiftscheduler.R;
import com.example.shiftscheduler.database.DatabaseHelper;
import com.example.shiftscheduler.models.ExportModel;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleExport extends AppCompatActivity {
    TextView selectedMonthYear;

    private ArrayList<ExportModel> exportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_export);
        selectedMonthYear = findViewById(R.id.selectedMonth);
    }

    public void selectMonth(View view) {
        Calendar today = Calendar.getInstance();

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ScheduleExport.this,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        // on date set
                        selectedMonthYear.setText(Integer.toString(selectedMonth+1) + "-" + Integer.toString(selectedYear));
                    }
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.NOVEMBER)
                .setMinYear(2020)
                .setActivatedYear(2021)
                .setMaxYear(2030)
                .setTitle("Select month year")
                .build().show();
    }



}



