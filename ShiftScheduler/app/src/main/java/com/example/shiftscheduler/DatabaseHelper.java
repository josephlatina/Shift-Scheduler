package com.example.shiftscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //declare constants
    public static final String EMPLOYEE_TABLE = "EMPLOYEE_TABLE";
    public static final String COL_ID = "EMPID";
    public static final String COL_SHIFTTYPEID = "SHIFTTYPEID";
    public static final String COL_AVAILABILITYID = "AVAILABILITYID";
    public static final String COL_FNAME = "FNAME";
    public static final String COL_LNAME = "LNAME";
    public static final String COL_CITY = "CITY";
    public static final String COL_STREET = "STREET";
    public static final String COL_PROVINCE = "PROVINCE";
    public static final String COL_POSTAL = "POSTALCODE";
    public static final String COL_DOB = "DATEOFBIRTH";
    public static final String COL_PHONENUM = "PHONENUM";

    //constructor method that will set the name of the database
        //context is the reference to the app, name is the name of database
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //method called to create a new database. Automatically called when app requests or inputs new data
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creates the table
        String createTableStatement = "CREATE TABLE " + EMPLOYEE_TABLE + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_SHIFTTYPEID + " INTEGER," +
                COL_AVAILABILITYID + " INTEGER," +
                COL_FNAME + " TEXT," + COL_LNAME + " TEXT," +
                COL_CITY + " TEXT," + COL_STREET + " TEXT," + COL_PROVINCE + " TEXT," + COL_POSTAL + " TEXT," +
                COL_DOB + " DATE," +
                COL_PHONENUM + " TEXT)";

        db.execSQL(createTableStatement);
    }
    //called to modify the schema for the database. Used when the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /*
    //Inserts new entry into the database.
        //Note: to check,
        // go to View -> Tool Windows -> Device File Explorer -> data -> edu.shadsluiter.sqldemo3
            // -> databases -> name of database. Export them to DB Browser Sqlite
    public boolean addEntry(EmployeeModel employeeModel) {
        //Retrieve the database already created and create an instance of database to hold it
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //Fill in the data for each column
        cv.put(COL_FNAME, employeeModel.getFName());
        cv.put(COL_LNAME, employeeModel.getLName());
        cv.put(COL_CITY, employeeModel.getCity());
        cv.put(COL_STREET, employeeModel.getStreet());
        cv.put(COL_PROVINCE, employeeModel.getProvince());
        cv.put(COL_POSTAL, employeeModel.getPostal());
        cv.put(COL_DOB, employeeModel.getDOB());
        cv.put(COL_PHONENUM, employeeModel.getPhoneNum());

        //check if inserting into the database was successful or not
        long success = db.insert(EMPLOYEE_TABLE,null,cv);
        if (success == -1) {
            return false;
        } else {
            return true;
        }
    }

     */




}
