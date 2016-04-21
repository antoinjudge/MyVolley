package myapps.myvolley;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antoin on 18/04/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MyTimesheet";

    // Timesheet table name
    private static final String TABLE_TIMESHEET = "timesheet";

    // Contacts Table Columns names
    private static final String KEY_DATE = "Date";
    private static final String KEY_EMPID = "id";
    private static final String KEY_BASIC = "Basic";
    private static final String KEY_OVERTIME = "Overtime";
    private static final String KEY_MEALS = "Meals";
    private static final String KEY_MILEAGE = "Mileage";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TIMESHEET_TABLE = "CREATE TABLE " + TABLE_TIMESHEET + "("
                + KEY_EMPID + " INTEGER PRIMARY KEY," + KEY_BASIC + " TEXT,"
                + KEY_OVERTIME + " TEXT" + KEY_DATE +"DATETIME" + ")";
        db.execSQL(CREATE_TIMESHEET_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMESHEET);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addContact(WeeklyTS weeklyTS) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BASIC, weeklyTS.getBasic()); // Contact Name
        values.put(KEY_OVERTIME, weeklyTS.getOvertime()); // Contact Phone


        // Inserting Row
        db.insert(TABLE_TIMESHEET, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    WeeklyTS getWeeklyTS(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TIMESHEET, new String[] { KEY_EMPID,
                        KEY_BASIC, KEY_OVERTIME, KEY_DATE }, KEY_EMPID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        WeeklyTS weeklyTS = new WeeklyTS(cursor.getInt(0),
                cursor.getInt(1), cursor.getInt(2));
        // return contact
        return weeklyTS;
    }

    // Getting All Contacts
    public List<WeeklyTS> getAllContacts() {
        List<WeeklyTS> myList = new ArrayList<WeeklyTS>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TIMESHEET;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WeeklyTS weeklyTS = new WeeklyTS();
                weeklyTS.setID(Integer.parseInt(cursor.getString(0)));
                weeklyTS.setBasic(cursor.getInt(1));
                weeklyTS.setOvertime(cursor.getInt(2));
                // Adding contact to list
                myList.add(weeklyTS);
            } while (cursor.moveToNext());
        }

        // return contact list
        return myList;
    }

    // Updating single contact
    public int updateContact(WeeklyTS weeklyTS) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BASIC, weeklyTS.getBasic());
        values.put(KEY_OVERTIME, weeklyTS.getOvertime());

        // updating row
        return db.update(TABLE_TIMESHEET, values, KEY_EMPID + " = ?",
                new String[] { String.valueOf(weeklyTS.getID()) });
    }

    // Deleting single contact
    public void deleteContact(WeeklyTS weeklyTS) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIMESHEET, KEY_EMPID + " = ?",
                new String[] { String.valueOf(weeklyTS.getID()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TIMESHEET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }



}
