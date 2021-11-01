package com.example.shiftscheduler.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.shiftscheduler.models.AvailabilityModel;
import com.example.shiftscheduler.models.EmployeeModel;
import com.example.shiftscheduler.models.ShiftModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //declare constants
    public static final String EMPLOYEE_TABLE = "EMPLOYEE_TABLE";
    public static final String SHIFT_TABLE = "SHIFT_TABLE";
    public static final String AVAILABILITY_TABLE = "AVAILABILITY_TABLE";
    public static final String QUALIFICATIONS_TABLE = "QUALIFICATIONS_TABLE";
    public static final String WORK_TABLE = "WORK_TABLE";
    public static final String COL_EMPID = "EMPID";
    public static final String COL_SHIFTID = "SHIFTID";
    public static final String COL_QUALIFICATIONID = "QUALIFICATIONID";
    public static final String COL_AVAILABILITYID = "AVAILABILITYID";
    public static final String COL_SHIFTTYPE = "SHIFTTYPE";
    public static final String COL_MORNING = "OPENING";
    public static final String COL_EVENING = "CLOSING";
    public static final String COL_DATE = "DATE";
    public static final String COL_FNAME = "FNAME";
    public static final String COL_LNAME = "LNAME";
    public static final String COL_CITY = "CITY";
    public static final String COL_STREET = "STREET";
    public static final String COL_PROVINCE = "PROVINCE";
    public static final String COL_POSTAL = "POSTALCODE";
    public static final String COL_DOB = "DATEOFBIRTH";
    public static final String COL_PHONENUM = "PHONENUM";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_ISACTIVE = "ISACTIVE";
    public static final String COL_SUNSHIFT = "SUNSHIFT";
    public static final String COL_MONSHIFT = "MONSHIFT";
    public static final String COL_TUESHIFT = "TUESHIFT";
    public static final String COL_WEDSHIFT = "WEDSHIFT";
    public static final String COL_THURSSHIFT = "THURSSHIFT";
    public static final String COL_FRISHIFT = "FRISHIFT";
    public static final String COL_SATSHIFT = "SATSHIFT";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");

    /***********************************************************************************
     QUERY STRINGS TO CREATE TABLES
     **********************************************************************************/

    //Create Employee Table
    private String createEmployeeTable = "CREATE TABLE " + EMPLOYEE_TABLE + "(" +
            COL_EMPID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_FNAME + " TEXT," + COL_LNAME + " TEXT," +
            COL_CITY + " TEXT," + COL_STREET + " TEXT," + COL_PROVINCE + " TEXT," + COL_POSTAL + " TEXT," +
            COL_DOB + " DATE," +
            COL_PHONENUM + " TEXT," + COL_EMAIL + " TEXT," + COL_ISACTIVE + " INTEGER," +
            COL_QUALIFICATIONID + " INTEGER," + COL_AVAILABILITYID + " INTEGER," +
            "FOREIGN KEY (" + COL_QUALIFICATIONID + ") REFERENCES " + QUALIFICATIONS_TABLE + "(" + COL_QUALIFICATIONID + "), " +
            "FOREIGN KEY (" + COL_AVAILABILITYID + ") REFERENCES " + AVAILABILITY_TABLE + "(" + COL_AVAILABILITYID + "))";
    //Create Shift Table
    private String createShiftTable = "CREATE TABLE " + SHIFT_TABLE + "(" +
            COL_SHIFTID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_SHIFTTYPE + " TEXT," +
            COL_DATE + " TEXT)";
    //Create WorkedBy Table
    private String createWorkTable = "CREATE TABLE " + WORK_TABLE + "(" +
            COL_EMPID + " INTEGER," +
            COL_SHIFTID + " INTEGER," +
            "FOREIGN KEY (" + COL_EMPID + ") REFERENCES " + EMPLOYEE_TABLE + "(" + COL_EMPID + "), " +
            "FOREIGN KEY (" + COL_SHIFTID + ") REFERENCES " + SHIFT_TABLE + "(" + COL_SHIFTID + "), " +
            "PRIMARY KEY (" + COL_EMPID + ", " + COL_SHIFTID + "))";
    //Create Availability Table
    private String createAvailabilityTable = "CREATE TABLE " + AVAILABILITY_TABLE + "(" +
            COL_AVAILABILITYID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_SUNSHIFT + " INTEGER," + COL_MONSHIFT + " INTEGER," +
            COL_TUESHIFT + " INTEGER," + COL_WEDSHIFT + " INTEGER," +
            COL_THURSSHIFT + " INTEGER," + COL_FRISHIFT + " INTEGER," +
            COL_SATSHIFT + " INTEGER)";
    //Create the Qualifications Table
    private String createQualificationsTable = "CREATE TABLE " + QUALIFICATIONS_TABLE + "(" +
            COL_QUALIFICATIONID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_MORNING + " INTEGER," +
            COL_EVENING + " INTEGER)";

    //constructor method that will set the name of the database
        //context is the reference to the app, name is the name of database
    public DatabaseHelper(@Nullable Context context) {
        super(context, "shiftscheduler.db", null, 1);
    }

    /*********************************************************************************************
     When App launches, method called to create a new database. Also automatically called
     when app requests or inputs new data
   **********************************************************************************************/

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create the Employee table
        db.execSQL(createEmployeeTable);
        //create the Shift table
        db.execSQL(createShiftTable);
        //create the Availability table
        db.execSQL(createAvailabilityTable);
        //create the Qualifications table
        db.execSQL(createQualificationsTable);
        //create the WorkedBy table
        db.execSQL(createWorkTable);
    }
    //called to modify the schema for the database. Used when the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*********************************************************************************************
     Add/Insert methods
     *********************************************************************************************/

    //Inserts new Employee entry into the database.
    public boolean addEmployee(EmployeeModel employeeModel) {
        //Create empty entry for Qualifications and Availability Tables corresponding to new employee
        AvailabilityModel availability = new AvailabilityModel(0,1,3,3,3,3,3,1);
        addAvailability(availability);
        String addQualifications = "INSERT INTO " + QUALIFICATIONS_TABLE + " DEFAULT VALUES";

        SQLiteDatabase db = this.getWritableDatabase(); // open the database from db
        db.execSQL(addQualifications);
        String getID = "SELECT MAX(" + COL_AVAILABILITYID + ") FROM " + AVAILABILITY_TABLE;
        Cursor cur = db.rawQuery(getID, null);
        cur.moveToFirst();
        int avaID = cur.getInt(0);
        cur.close();

        //Retrieve the database already created and create an instance of database to hold it

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
        cv.put(COL_EMAIL, employeeModel.getEmail());
        cv.put(COL_ISACTIVE, "1");
        cv.put(COL_QUALIFICATIONID, String.valueOf(avaID));
        cv.put(COL_AVAILABILITYID, String.valueOf(avaID));

        //check if inserting into the database was successful or not
        long success = db.insert(EMPLOYEE_TABLE,null,cv);

        db.close();
        if (success == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Inserts new Shift entry into the database
    public boolean addShift(LocalDate date, String time) {
        //Retrieve the database already created and create an instance of database to hold it
        SQLiteDatabase db = this.getWritableDatabase(); // open the database from db
        ContentValues cv = new ContentValues();
        //get data from the database
        String queryString = "SELECT " + COL_SHIFTID + " FROM " + SHIFT_TABLE + " WHERE DATE(" + COL_DATE +
                ") = ? AND " + COL_SHIFTTYPE + " = ? ";
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(Date.valueOf(date.toString())), time});
        //check if the result successfully brought back from the database
        if (cursor.moveToFirst()) {
            //this indicates that the given shift already exists. Exit to prevent duplication of data
            return false;
        } else {
            //Fill in the data for each column
            cv.put(COL_DATE, String.valueOf(Date.valueOf(date.toString())));
            cv.put(COL_SHIFTTYPE, time);

            //check if inserting into the database was successful or not
            long success = db.insert(SHIFT_TABLE,null,cv);
            db.close();
            if (success == -1) {
                return false;
            } else {
                return true;
            }
        }


    }

    //Inserts new Availability entry into the database
    public boolean addAvailability(AvailabilityModel availabilityModel){
        //Retrieve the database already created and create an instance of database to hold it
        SQLiteDatabase db = this.getWritableDatabase(); // open the database from db
        ContentValues cv = new ContentValues();

        //Fill in the data for each column
        cv.put(COL_SUNSHIFT, availabilityModel.getSunShift());
        cv.put(COL_MONSHIFT, availabilityModel.getMonShift());
        cv.put(COL_TUESHIFT, availabilityModel.getTueShift());
        cv.put(COL_WEDSHIFT, availabilityModel.getWedShift());
        cv.put(COL_THURSSHIFT, availabilityModel.getThursShift());
        cv.put(COL_FRISHIFT, availabilityModel.getFriShift());
        cv.put(COL_SATSHIFT, availabilityModel.getSatShift());

        //check if inserting into the database was successful or not
        long success = db.insert(AVAILABILITY_TABLE,null,cv);
        db.close();
        if (success == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Inserts new Work entry into the database
    public boolean scheduleEmployee(int empID, LocalDate date, String time) {
        int shiftID = 0;
        //get data from the database
        String queryString = "SELECT " + COL_SHIFTID + " FROM " + SHIFT_TABLE + " WHERE DATE(" + COL_DATE +
                ") = ? AND " + COL_SHIFTTYPE + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(Date.valueOf(date.toString())), time});
        //check if the result successfully brought back from the database
        if (cursor.moveToFirst()) {
            shiftID = cursor.getInt(0);
        } else {
        }

        //Retrieve the database already created and create an instance of database to hold it
        ContentValues cv = new ContentValues();

        cv.put(COL_EMPID, empID);
        cv.put(COL_SHIFTID, shiftID);

        long success = db.insert(WORK_TABLE, null, cv);

        //close both cursor and db
        cursor.close();
        db.close();
        if (success == -1) {
            return false;
        } else {
            return true;
        }
    }

    /*********************************************************************************************
      Update methods
     *********************************************************************************************/
    public boolean updateEmployee(EmployeeModel employeeModel) {
        //Retrieve the database already created and create an instance of database to hold it
        SQLiteDatabase db = this.getWritableDatabase(); // open the database from db
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
        cv.put(COL_EMAIL, employeeModel.getEmail());
        cv.put(COL_ISACTIVE, employeeModel.getStatus());

        int empID = employeeModel.getEmployeeID();

        //check if inserting into the database was successful or not
        long success = db.update(EMPLOYEE_TABLE,cv, COL_EMPID + " = ?", new String[] {String.valueOf(empID)});
        if (success == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Update Qualification Table with the employeeID, and boolean values for morning, evening and fullDay
    public void updateQualification(int employeeID, int morning, int evening) {
        //Retrieve the database already created and create an instance of database to hold it
        SQLiteDatabase db = this.getWritableDatabase(); // open the database from db
        ContentValues cv = new ContentValues();

        String queryString = "UPDATE " + QUALIFICATIONS_TABLE + " SET " + COL_MORNING + " = " + morning +
                ", " + COL_EVENING + " = " + evening +
                " WHERE " + COL_QUALIFICATIONID + " = " + employeeID;

        db.execSQL(queryString);
    }

    //Update Availability Table with the employeeID,
    public void updateAvailability(int employeeID, int sunShift, int monShift,
                                   int tueShift, int wedShift, int thursShift,
                                   int friShift, int satShift) {
        // retrieve the database already created and create an instance if database
        SQLiteDatabase db = this.getWritableDatabase(); //open the database from db
        ContentValues cv = new ContentValues();

        String queryString = "UPDATE " + AVAILABILITY_TABLE + " SET " +
                COL_SUNSHIFT + " = " + sunShift + ", " +
                COL_MONSHIFT + " = " + monShift + ", " +
                COL_TUESHIFT + " = " + tueShift + ", " +
                COL_WEDSHIFT + " = " + wedShift + ", " +
                COL_THURSSHIFT + " = " + thursShift + ", " +
                COL_FRISHIFT + " = " + friShift + ", " +
                COL_SATSHIFT + " = " + satShift +
                " WHERE " + COL_AVAILABILITYID + " = " + employeeID;

        db.execSQL(queryString);
    }

    /*********************************************************************************************
     Get methods
     *********************************************************************************************/
    public List<Boolean> getQualifications(int employeeID) {
        List<Boolean> returnList = new ArrayList<>();

        //get data from the database
        String queryString = "SELECT * FROM " + QUALIFICATIONS_TABLE + " WHERE " + COL_QUALIFICATIONID +
                " = " + employeeID;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, null);
        //check if the result successfully brought back from the database
        if (cursor.moveToFirst()) {
            boolean Opening = cursor.getInt(1) == 1 ? true : false;
            boolean Closing = cursor.getInt(2) == 1 ? true : false;

            returnList.add(Opening);
            returnList.add(Closing);
        }
        //close both db and cursor for others to access
        cursor.close();
        db.close();

        return returnList;
    }

    // retrieve data from the Employee table for one employee
    public EmployeeModel getEmployee(int employeeID) {
        //get data from the database
        String queryString = "SELECT * FROM " + EMPLOYEE_TABLE + " WHERE " + COL_EMPID +
                " = " + employeeID;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, null);
        //check if the result successfully brought back from the database
        String fName = "", lName = "", city = "", street = "", province = "", postal = "",
                dateOfBirth = "", phone = "", email = "";
        boolean isActive = true;
        if (cursor.moveToFirst()){ //move it to the first of the result set
            //retrieve employee information
            fName = cursor.getString(1);
            lName = cursor.getString(2);
            city = cursor.getString(3);
            street = cursor.getString(4);
            province = cursor.getString(5);
            postal = cursor.getString(6);
            dateOfBirth = cursor.getString(7);
            phone = cursor.getString(8);
            email = cursor.getString(9);
            isActive = cursor.getInt(10) == 1 ? true: false;

        } else {
            // error, nothing added to the list
        }
        EmployeeModel employee = new EmployeeModel(employeeID,
                fName,lName, city, street, province, postal, dateOfBirth, phone, email, isActive);
        // close both db and cursor for others to access
        cursor.close();
        db.close();
        return employee;
    }

    // retrieve employees with availability open for the given shift
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<EmployeeModel> getAvailableEmployees(LocalDate date, String time) {
        //Initialize Lists
        List<Integer> employeeIDs = new ArrayList<>();
        List<EmployeeModel> employees = new ArrayList<>();
        //extract day of the week and store result in ShiftDay
        int dayOfWeek = date.getDayOfWeek().getValue();
        List<String> ShiftDay = new ArrayList<>();
        switch (dayOfWeek) {
            case 1: ShiftDay.add(COL_MONSHIFT); break;
            case 2: ShiftDay.add(COL_TUESHIFT); break;
            case 3: ShiftDay.add(COL_WEDSHIFT); break;
            case 4: ShiftDay.add(COL_THURSSHIFT); break;
            case 5: ShiftDay.add(COL_FRISHIFT); break;
            case 6: ShiftDay.add(COL_SATSHIFT); break;
            case 7: ShiftDay.add(COL_SUNSHIFT); break;
        }
        //extract shift time
        int ShiftTime = 0;
        switch (time) {
            case "MORNING": ShiftTime = 1; break;
            case "EVENING": ShiftTime = 2; break;
            case "FULL": ShiftTime = 1; break;
        }
        //create query string
        String queryString = "SELECT E." + COL_EMPID + " FROM " + AVAILABILITY_TABLE + " AS A, " +
                EMPLOYEE_TABLE + " AS E " + " WHERE E." + COL_AVAILABILITYID + " = A." + COL_AVAILABILITYID +
                " AND (A." + ShiftDay.get(0) + " = " + ShiftTime;
        //if it's a weekday, add extra string that checks the scenario of employee being available for both opening and closing
        if (dayOfWeek != 6 && dayOfWeek != 7) {
            queryString += " OR A." + ShiftDay.get(0) + " = 3)";
        } else {
            queryString += ")";
        }
        //check for employees that are already working in the given shift
        queryString += " AND E." + COL_EMPID + " NOT IN ( SELECT E." + COL_EMPID + " FROM " + WORK_TABLE +
                " AS W, " + SHIFT_TABLE + " AS S WHERE E." + COL_EMPID + " = W." + COL_EMPID + " AND W." + COL_SHIFTID + " = S." + COL_SHIFTID + " AND DATE(S." +
                COL_DATE + ") = ? )";
        //access database
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(Date.valueOf(date.toString()))});
        //check if the result successfully brought back from the database
        if (cursor.moveToFirst()) { //move it to the first of the result set
            //loop through the results
            do {
                int employeeID = cursor.getInt(0);
                employeeIDs.add(employeeID);    //add employeeID to the list
            } while (cursor.moveToNext());
        }
        //loop to fill in resulting list of the retrieved employees
        for (int i=0; i < employeeIDs.size(); i++) {
            int empID = employeeIDs.get(i);
            employees.add(getEmployee(empID));
        }

        return employees;
    }

    //retrieve employees that work in a specific shift
    public List<EmployeeModel> getScheduledEmployees(LocalDate date, String time) {
        //Initialize List
        List<EmployeeModel> employees = new ArrayList<>();

        //get data from the database
        String queryString = "SELECT E." + COL_EMPID + "," + COL_FNAME + "," + COL_LNAME + "," +
                COL_CITY + "," + COL_STREET + "," + COL_PROVINCE + "," + COL_POSTAL + "," +
                COL_DOB + "," + COL_PHONENUM + "," + COL_EMAIL + "," + COL_ISACTIVE +
                " FROM " + EMPLOYEE_TABLE + " AS E, " + WORK_TABLE + " AS W, " +
                SHIFT_TABLE + " AS S WHERE E." + COL_EMPID + " = W." + COL_EMPID + " AND W." + COL_SHIFTID +
                " = S." + COL_SHIFTID + " AND DATE(S." + COL_DATE +
                ") = ? AND S." + COL_SHIFTTYPE + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(Date.valueOf(date.toString())), time});
        //check if the result successfully brought back from the database
        if (cursor.moveToFirst()){ //move it to the first of the result set
            //loop through the results
            do{
                int employeeID = cursor.getInt(0);
                String fName = cursor.getString(1);
                String lName = cursor.getString(2);
                String city = cursor.getString(3);
                String street = cursor.getString(4);
                String province = cursor.getString(5);
                String postal = cursor.getString(6);
                String dateOfBirth = cursor.getString(7);
                String phone = cursor.getString(8);
                String email = cursor.getString(9);
                boolean isActive = cursor.getInt(10) == 1 ? true: false;

                EmployeeModel newEmployee = new EmployeeModel(employeeID,
                        fName,lName, city, street, province, postal, dateOfBirth, phone, email, isActive);
                employees.add(newEmployee);
            } while(cursor.moveToNext());
        } else {
            // error, nothing added to the list
        }

        // close both db and cursor for others to access
        cursor.close();
        db.close();
        return employees;

    }

    // retrieve data from the Employee table
    public List<EmployeeModel> getEmployees(){
        List<EmployeeModel> returnList = new ArrayList<>();

        //get data from the database
        String queryString = "SELECT * FROM " + EMPLOYEE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, null);
        //check if the result successfully brought back from the database
        if (cursor.moveToFirst()){ //move it to the first of the result set
            //loop through the results
            do{
                int employeeID = cursor.getInt(0);
                String fName = cursor.getString(1);
                String lName = cursor.getString(2);
                String city = cursor.getString(3);
                String street = cursor.getString(4);
                String province = cursor.getString(5);
                String postal = cursor.getString(6);
                String dateOfBirth = cursor.getString(7);
                String phone = cursor.getString(8);
                String email = cursor.getString(9);
                boolean isActive = cursor.getInt(10) == 1 ? true: false;

                EmployeeModel newEmployee = new EmployeeModel(employeeID,
                        fName,lName, city, street, province, postal, dateOfBirth, phone, email, isActive);
                returnList.add(newEmployee);
            } while(cursor.moveToNext());
        } else {
            // error, nothing added to the list
        }

        // close both db and cursor for others to access
        cursor.close();
        db.close();
        return returnList;
    }

    // retrieve data from availability table
    public AvailabilityModel getAvailability(int employeeID) {
        //get data from the database
        String queryString = "SELECT * FROM " + AVAILABILITY_TABLE + " WHERE " + COL_AVAILABILITYID
                + " = " + employeeID;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, null);

        //retrieve availability information from this cursor.
        int availabilityID = cursor.getInt(0);
        int sunShift = cursor.getInt(1);
        int monShift = cursor.getInt(2);
        int tueShift = cursor.getInt(3);
        int wedShift = cursor.getInt(4);
        int thurShift = cursor.getInt(5);
        int friShift = cursor.getInt(6);
        int satShift = cursor.getInt(7);

        // the availabilityID to be returned is:
        AvailabilityModel availability = new AvailabilityModel(availabilityID, sunShift,
                monShift, tueShift, wedShift, thurShift, friShift, satShift);

        // close both db and cursor for others to access
        cursor.close();
        db.close();
        return availability;
    }

    /*********************************************************************************************
     Remove methods
     *********************************************************************************************/
    //Removes work entry from the database
    public boolean descheduleEmployee(int empID, LocalDate date, String time) {
        int shiftID = 0;
        //Retrieve the shiftID that corresponds to given date and time
        String queryString = "SELECT " + COL_SHIFTID + " FROM " + SHIFT_TABLE + " WHERE DATE(" + COL_DATE +
                ") = ? AND " + COL_SHIFTTYPE + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(Date.valueOf(date.toString())), time});
        //check if the result successfully brought back from the database
        if (cursor.moveToFirst()) {
            shiftID = cursor.getInt(0);
        } else {
        }

        //Remove work entry from database
        long success = db.delete(WORK_TABLE, COL_EMPID + " = ? AND " + COL_SHIFTID +
                " = ? ", new String[] {String.valueOf(empID),String.valueOf(shiftID)});

        //close both cursor and db
        cursor.close();
        db.close();
        if (success == -1) {
            return false;
        } else {
            return true;
        }
    }
}
