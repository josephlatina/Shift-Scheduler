package com.example.shiftscheduler.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_export);
        selectedMonthYear = findViewById(R.id.selectedMonth);
        alertDialogBuilder = new AlertDialog.Builder(this);
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

    public void exportPDF(View view) throws FileNotFoundException {




        String [] txt_MonthYear;
        String month = "", year = "";

        txt_MonthYear = selectedMonthYear.getText().toString().split("-");
        if (txt_MonthYear.length == 1) {
            alertDialogBuilder.setTitle("Empty Field");
            alertDialogBuilder.setMessage("Please select a month and a year.");

            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    }).create().show();
            return;
        } else {
            month += txt_MonthYear[0];
            year += txt_MonthYear[1];
        }

        //get the exportList contains all shifts for that given month
        exportList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(ScheduleExport.this);
        exportList = (ArrayList) dbHelper.getExportInfoOfOneMonth(year, month);
//------------------------------------------------------------------------------------------------
// For my test
//        try {
//            FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "export.txt"));
//            for (ExportModel export : exportList) {
//                fos.write((export.getEmpID()+" ").getBytes());
//                fos.write((export.getfName()+" ").getBytes());
//                fos.write((export.getlName()+" ").getBytes());
//                fos.write((export.getCity()+" ").getBytes());
//                fos.write((export.getStreet()+" ").getBytes());
//                fos.write((export.getProvince()+" ").getBytes());
//                fos.write((export.getPostal()+" ").getBytes());
//                fos.write((export.getDOB()+" ").getBytes());
//                fos.write((export.getPhoneNum()+" ").getBytes());
//                fos.write((export.getEmail()+" ").getBytes());
//                fos.write((export.isActive()+" ").getBytes());
//                fos.write((export.getShiftID()+" ").getBytes());
//                fos.write((export.getShiftType()+" ").getBytes());
//                fos.write((export.getDate()+" ").getBytes());
//                fos.write("\n".getBytes());
//            }
//            fos.flush();
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        }
//--------------------------------------------------------------------------------------------------
        //create object of Document class
        Document mDoc = new Document();
        //pdf file name
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        mFileName = "SchedulesFor"+ year + "-" + month + "(Generated on " + mFileName + ")";
        //pdf file path
        String mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mFileName + ".pdf";

        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            //open the document for writing
            mDoc.open();
            //get text from EditText i.e. mTextEt
            String mText = "Schedules For Year: " + year + ", Month: " + month + "\n";

            //add author of the document (optional)
            mDoc.addAuthor("TEAM Borg");

            //add paragraph to the document
            mDoc.add(new Paragraph(mText));

            mDoc.add( Chunk.NEWLINE );

            String[] headers = new String[]{"First Name", "Last Name", "Shift Type", "Date"};

            List<String[]> rows = new ArrayList<String[]>();

            for (ExportModel export: exportList ){
                String[] strArray = {
                        export.getfName(), export.getlName(), export.getShiftType(), export.getDate()
                };
                rows.add(strArray);
            }

            PdfPTable table = new PdfPTable(headers.length);
            for (String header : headers) {
                PdfPCell cell = new PdfPCell();
                cell.setGrayFill(0.9f);
                cell.setPhrase(new Phrase(header.toUpperCase()));
                table.addCell(cell);
            }
            table.completeRow();

            for (String[] row : rows) {
                for (String data : row) {
                    Phrase phrase = new Phrase(data);
                    table.addCell(new PdfPCell(phrase));
                }
                table.completeRow();
            }

            mDoc.addTitle("Shift Schedulers For This Month");
            mDoc.add(table);

            //close the document
            mDoc.close();
            //show message that file is saved, it will show file name and file path too
            Toast.makeText(this, mFileName +".pdf\nis saved to\n"+ mFilePath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            //if any thing goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}



