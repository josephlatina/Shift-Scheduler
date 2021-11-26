package com.example.shiftscheduler.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.shiftscheduler.models.AvailabilityModel;
import com.example.shiftscheduler.models.EmployeeModel;
import com.example.shiftscheduler.models.ExportModel;
import com.example.shiftscheduler.models.TimeoffModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //declare constants
    public static final String EMPLOYEE_TABLE = "EMPLOYEE_TABLE";
    public static final String SHIFT_TABLE = "SHIFT_TABLE";
    public static final String AVAILABILITY_TABLE = "AVAILABILITY_TABLE";
    public static final String QUALIFICATIONS_TABLE = "QUALIFICATIONS_TABLE";
    public static final String TIMEOFF_TABLE = "TIMEOFF_TABLE";
    public static final String WORK_TABLE = "WORK_TABLE";
    public static final String COL_EMPID = "EMPID";
    public static final String COL_SHIFTID = "SHIFTID";
    public static final String COL_QUALIFICATIONID = "QUALIFICATIONID";
    public static final String COL_AVAILABILITYID = "AVAILABILITYID";
    public static final String COL_TIMEOFFID = "TIMEOFFID";
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
    public static final String COL_DATEFROM = "DATEFROM";
    public static final String COL_DATETO = "DATETO";
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
            COL_SUNSHIFT + " INTEGER DEFAULT 0," + COL_MONSHIFT + " INTEGER DEFAULT 0," +
            COL_TUESHIFT + " INTEGER DEFAULT 0," + COL_WEDSHIFT + " INTEGER DEFAULT 0," +
            COL_THURSSHIFT + " INTEGER DEFAULT 0," + COL_FRISHIFT + " INTEGER DEFAULT 0," +
            COL_SATSHIFT + " INTEGER DEFAULT 0)";
    //Create the Qualifications Table
    private String createQualificationsTable = "CREATE TABLE " + QUALIFICATIONS_TABLE + "(" +
            COL_QUALIFICATIONID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_MORNING + " INTEGER," +
            COL_EVENING + " INTEGER)";
    //Create TimeOff Table
    private String createTimeOffTable = "CREATE TABLE " + TIMEOFF_TABLE + "(" +
            COL_TIMEOFFID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_EMPID + " INTEGER," +
            COL_DATEFROM + " DATE," +
            COL_DATETO + " DATE," +
            "FOREIGN KEY (" + COL_EMPID + ") REFERENCES " + EMPLOYEE_TABLE + "(" + COL_EMPID + "))";

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
        //create the TimeOff table
        db.execSQL(createTimeOffTable);
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
        cv.put(COL_ISACTIVE, "1");

        String addQualifications = "INSERT INTO " + QUALIFICATIONS_TABLE + " DEFAULT VALUES";
        String addAvailability = "INSERT INTO " + AVAILABILITY_TABLE + " DEFAULT VALUES";

        db.execSQL(addQualifications);
        db.execSQL(addAvailability);

        String getID = "SELECT MAX(" + COL_AVAILABILITYID + ") FROM " + AVAILABILITY_TABLE;
        Cursor cur = db.rawQuery(getID, null);
        cur.moveToFirst();
        int avaID = cur.getInt(0);
        cur.close();
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

    //Inserts new TimeOff entry into the database
    public boolean addTimeOff(int empID, LocalDate dateFrom, LocalDate dateTo) {
        //Retrieve the database already created and create an instance of database to hold it
        SQLiteDatabase db = this.getWritableDatabase(); // open the database from db
        ContentValues cv = new ContentValues();
        //get data from the database
        String queryString = "SELECT " + COL_TIMEOFFID + " FROM " + TIMEOFF_TABLE + " WHERE DATE(" + COL_DATEFROM +
                ") = ? AND " + COL_DATETO + " = ? AND " + COL_EMPID + " = ? ";
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(Date.valueOf(dateFrom.toString())),
                String.valueOf(Date.valueOf(dateTo.toString())), String.valueOf(empID)});
        //check if the result successfully brought back from the database
        if (cursor.moveToFirst()) {
            //this indicates that the given timeoff entry already exists. Exit to prevent duplication of data
            return false;
        } else {
            //Fill in the data for each column
            cv.put(COL_EMPID, String.valueOf(empID));
            cv.put(COL_DATEFROM, String.valueOf(Date.valueOf(dateFrom.toString())));
            cv.put(COL_DATETO, String.valueOf(Date.valueOf(dateTo.toString())));

            //check if inserting into the database was successful or not
            long success = db.insert(TIMEOFF_TABLE,null,cv);
            db.close();
            if (success == -1) {
                return false;
            } else {
                return true;
            }
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
        //get qualifications
        List<Boolean> qualifications = getQualifications(employeeID);

        EmployeeModel employee = new EmployeeModel(employeeID,
                fName,lName, city, street, province, postal, dateOfBirth, phone, email, isActive, qualifications);
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
        //check for employees that have timeoffs
        queryString += " AND E." + COL_EMPID + " NOT IN ( SELECT E." + COL_EMPID + " FROM " + TIMEOFF_TABLE +
                " AS T WHERE E." + COL_EMPID + " = T." + COL_EMPID + " AND DATE(T." + COL_DATEFROM + ") <= ? AND DATE(T." + COL_DATETO + ") >= ? )";
        //access database
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(Date.valueOf(date.toString())),
                String.valueOf(Date.valueOf(date.toString())),String.valueOf(Date.valueOf(date.toString()))});
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

    public void clearScheduledEmployees(LocalDate date, String time) {
        //Retrieve scheduled Employees
        List<EmployeeModel> scheduledEmployees = getScheduledEmployees(date, time);

        //Deschedule every employee in the scheduled Employees List
        for (EmployeeModel employee : scheduledEmployees) {
            descheduleEmployee(employee.getEmployeeID(), date, time);
        }
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

                //get Qualifications
                List<Boolean> qualifications = getQualifications(employeeID);

                EmployeeModel newEmployee = new EmployeeModel(employeeID,
                        fName,lName, city, street, province, postal, dateOfBirth, phone, email, isActive, qualifications);
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

    //retrieve data from the Timeoff table
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<TimeoffModel> getTimeoffs(int empID) {
        List<TimeoffModel> returnList = new ArrayList<>();

        //get data from the database
        String queryString = "SELECT T." + COL_TIMEOFFID + ", T." + COL_EMPID + ", " + COL_FNAME + ", " + COL_LNAME + ", " + COL_DATEFROM +
                ", " + COL_DATETO + " FROM " + TIMEOFF_TABLE + " AS T, " + EMPLOYEE_TABLE + " AS E WHERE T." +
                COL_EMPID + " = E." + COL_EMPID + " AND T." + COL_EMPID + " = " + empID;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, null);
        //check if the result successfully brought back from the database
        if (cursor.moveToFirst()){ //move it to the first of the result set
            //loop through the results
            do{
                int timeoffID = cursor.getInt(0);
                int employeeID = cursor.getInt(1);
                String fName = cursor.getString(2);
                String lName = cursor.getString(3);
                LocalDate dateFrom = LocalDate.parse(cursor.getString(4), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate dateTo = LocalDate.parse(cursor.getString(5), DateTimeFormatter.ISO_LOCAL_DATE);

                TimeoffModel entry = new TimeoffModel(timeoffID, employeeID, fName, lName, dateFrom, dateTo);
                returnList.add(entry);
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
        int availabilityID = 0, sunShift = 0, monShift = 0, tueShift = 0,
                wedShift = 0, thurShift = 0, friShift = 0, satShift = 0;

        if (cursor.moveToFirst()) {
            //retrieve availability information from this cursor.
            availabilityID = cursor.getInt(0);
            sunShift = cursor.getInt(1);
            monShift = cursor.getInt(2);
            tueShift = cursor.getInt(3);
            wedShift = cursor.getInt(4);
            thurShift = cursor.getInt(5);
            friShift = cursor.getInt(6);
            satShift = cursor.getInt(7);
        }

        // the availabilityID to be returned is:
        AvailabilityModel availability = new AvailabilityModel(availabilityID, sunShift,
                    monShift, tueShift, wedShift, thurShift, friShift, satShift);

        // close both db and cursor for others to access
        cursor.close();
        db.close();
        return availability;
    }

    public int getShiftID(LocalDate date, String time) {
        int shiftID;
        //get data from the database
        String queryString = "SELECT " + COL_SHIFTID + " FROM " + SHIFT_TABLE + " WHERE DATE(" + COL_DATE +
                ") = ? AND " + COL_SHIFTTYPE + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(Date.valueOf(date.toString())), time});
        //If shift exists, return shiftID. Otherwise, return 0.
        if (cursor.moveToFirst()) {
            shiftID = cursor.getInt(0);
        } else {
            shiftID = 0;
        }
        return shiftID;
    }

    public List<ExportModel> getExportInfoOfOneMonth(String year, String month) {
        List<ExportModel> returnList = new ArrayList<>();

        String firstName = "", lastName = "", shiftType = "", date = "";

        //get data from the database
        String queryString = "SELECT DISTINCT" + COL_FNAME + COL_LNAME + COL_SHIFTTYPE + COL_DATE +
                " FROM " + SHIFT_TABLE + " JOIN " + WORK_TABLE + " JOIN " + EMPLOYEE_TABLE +
                " WHERE " + COL_DATE + " LIKE '%" + year + "-" + month + "%')";

        SQLiteDatabase db = this.getWritableDatabase();

        //Cursor is the [result] set from SQL statement
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                firstName = cursor.getString(1);
                lastName = cursor.getString(2);
                shiftType = cursor.getString(3);
                date = cursor.getString(4);

                ExportModel newExportInfo = new ExportModel(firstName, lastName, shiftType, date);
                returnList.add(newExportInfo);
            } while (cursor.moveToNext());
        } else {

        }
        cursor.close();
        db.close();
        return returnList;
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

    //Removes timeoff entry from the database
    public boolean removeTimeOff(int timeOffID) {
        //Retrieve the database already created and create an instance of database to hold it
        SQLiteDatabase db = this.getWritableDatabase(); // open the database from db

        //Remove timeoff entry from database
        long success = db.delete(TIMEOFF_TABLE, COL_TIMEOFFID + " = ? ",
                new String[] {String.valueOf(timeOffID)});

        //close db
        db.close();
        if (success == -1) {
            return false;
        } else {
            return true;
        }
    }
}
