package myapps.myvolley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeSheet extends AppCompatActivity implements View.OnClickListener {

    private static final String UPDATE_URL = "http://www.antoinjudge.hol.es/myVolley/updateTimesheet.php";
    public static final String KEY_BASIC = "basic";
    public static final String KEY_OVERTIME = "overtime";
    public static final String KEY_MEAL = "meals";
    public static final String KEY_EMPID = "empid";
    public static final String KEY_MILEAGE = "mileage";
    public static final String KEY_PWORD = "password";
    public static final String KEY_DATE = "date";

    private SQLiteDatabase db;
    private static final String SELECT_SQL = "SELECT * FROM times";
    private Cursor c;

    private EditText editTBasic;
    private EditText editTOT;
    private EditText editTMeals;
    private  EditText editTEmpID;
    private TextView dateTV;
    private TextView empID;
    private Button viewMileage;
    private Button btnAdd;
    //private EditText editTMiles;
    private  Button btnView;
    private Button btnViewWeekly;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_sheet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        createDatabase();

        editTBasic = (EditText) findViewById(R.id.editTextBasic);
        editTOT = (EditText) findViewById(R.id.editTextOverTime);
        editTMeals = (EditText) findViewById(R.id.editTextMeals);
        viewMileage=(Button) findViewById(R.id.btnViewMileage);
        //editTMiles = (EditText) findViewById(R.id.editTextMileage);
        empID=(TextView) findViewById(R.id.textViewEmpID);
        empID.setVisibility(View.INVISIBLE);
       // editTEmpID =(EditText) findViewById(R.id.editTextEmpID);
        //editTEmpID.setVisibility(View.INVISIBLE);
        dateTV=(TextView) findViewById(R.id.textView3);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        dateTV.setText(dayOfTheWeek);


        btnAdd = (Button) findViewById(R.id.buttonAddToTimeSheet);
        btnView = (Button) findViewById(R.id.buttonViewTimeSheet);
        btnViewWeekly=(Button) findViewById(R.id.buttonViewWeeklySheet);
        btnViewWeekly.setVisibility(View.INVISIBLE);


        btnAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewWeekly.setOnClickListener(this);
        viewMileage.setOnClickListener(this);






        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    protected void createDatabase(){
        db=openOrCreateDatabase("CurrentDailyTS", Context.MODE_PRIVATE, null);
       // db.execSQL("CREATE TABLE IF NOT EXISTS times(empid INTEGER , basic INTEGER  NOT NULL DEFAULT 0,overtime INTEGER NOT NULL DEFAULT 0, meals INTEGER NOT NULL DEFAULT 0, mileage INTEGER NOT NULL DEFAULT 0, date TEXT PRIMARY KEY  NOT NULL, sent INTEGER NOT NULL DEFAULT 0);");
        db.execSQL("CREATE TABLE IF NOT EXISTS times(empid INTEGER , basic INTEGER  NOT NULL DEFAULT 0,overtime INTEGER NOT NULL DEFAULT 0, meals INTEGER NOT NULL DEFAULT 0, date TEXT PRIMARY KEY  NOT NULL, sent INTEGER NOT NULL DEFAULT 0);");
        db.execSQL("CREATE TABLE IF NOT EXISTS journey(journeyid INTEGER PRIMARY KEY AUTOINCREMENT, startloc TEXT , endloc TEXT, dist INTEGER, date TEXT);");

    }


    protected void insertIntoDB() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String myempid = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);

        String empID = myempid.toString().trim();
        int myEmpId = Integer.parseInt(empID);
        String basic = editTBasic.getText().toString().trim();
        int mybasic = Integer.parseInt(basic);
        String overtime = editTOT.getText().toString().trim();
        int myOT = Integer.parseInt(overtime);
        String meals = editTMeals.getText().toString().trim();
        int myMeals = Integer.parseInt(meals);
        String date = thisDate.toUpperCase().trim();

        if (basic.equals("") || String.valueOf(myOT).equals(" ") || meals.equals("")  || empID.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        //if(isEntry(date)){
        //  String myQuery = "UPDATE times SET empid = '"+empID+"', basic ='"+(basic + basic)+"', overtime = '"+(overtime+overtime)+"', meals = '"+(meals+meals)+"', mileage ='"+(mileage+mileage)+"', date ='"+date+"';";
        //db.execSQL(myQuery);
        //}
        else {
            String query = "INSERT OR IGNORE INTO times (empid ,  date) VALUES('" + myEmpId + "',  '" + date + "' );";// UPDATE times SET( empId = '"+empID+"',basic = '"+(basic+ 100)+" WHERE date = '"+date+");";
            db.execSQL(query);

           // String query2 = "UPDATE times SET basic = basic +'" + mybasic + "',overtime= overtime+ '" + myOT + "', meals = meals +'" + myMeals + "', mileage = mileage + '" + myMileage + "' WHERE date = '" + date + "' AND empid = '" + myEmpId + "'";
            String query2 = "UPDATE times SET basic = basic +'" + mybasic + "',overtime= overtime+ '" + myOT + "', meals = meals +'" + myMeals + "'  WHERE date = '" + date + "' AND empid = '" + myEmpId + "'";
            db.execSQL(query2);
            Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
            //}
        }

    }





    @Override
    public void onClick(View v) {
        if(v == btnAdd){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeSheet.this);
            // Setting Dialog Title
            alertDialog.setTitle("Confirm Submission...");
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.color_time);
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you to add this to Timesheet?");
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    // Write your code here to invoke YES event
                    sendTimesheet();
                }
            });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    Toast.makeText(getApplicationContext(), "Data not Added", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
        if(v==btnView){
            showPeoples();
        }
        if(v==viewMileage){
            Intent intent = new Intent(this,showAll.class);
            startActivity(intent);
            finish();
        }
        if(v==btnViewWeekly){
            Intent intent = new Intent(this,ViewWeekly.class);
            startActivity(intent);
            finish();

        }

    }

    private void showPeoples(){
        Intent intent = new Intent(this,ViewTimeSheet.class);
        startActivity(intent);
        finish();
    }
    private void sendTimesheet() {
        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String email = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        final String pword =sharedPreferences.getString(LoginActivity.PASSWORD_SHARED_PREF, "Not Available");
        final String myempid =sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");

        final String basic = editTBasic.getText().toString().trim();
        final String overtime = editTOT.getText().toString().trim();
        final String meals = editTMeals.getText().toString().trim();

        // final String empid = editTextEmpid.getText().toString().trim();
        // final String mileage = editTextMileage.getText().toString().trim();
        // final String password = pword.toString().trim();
        final String empid = myempid.toString().trim();

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
        Date todayDate = new Date();
        final String thisDate = currentDate.format(todayDate);

        if (basic.equals("") || overtime.equals("") || meals.equals("") ||  thisDate.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(TimeSheet.this, response, Toast.LENGTH_LONG).show();
                        insertIntoDB();
                        Toast.makeText(getApplicationContext(), "Submitted, and updated your records", Toast.LENGTH_LONG).show();


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TimeSheet.this, error.toString(), Toast.LENGTH_LONG).show();
                        if(error instanceof NoConnectionError) {
                            Toast.makeText(TimeSheet.this, "No Internet Connection, Timesheet Not Sent!!!", Toast.LENGTH_LONG).show();
                        }
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMPID, empid);
                params.put(KEY_DATE, thisDate);
                params.put(KEY_BASIC, basic);
                params.put(KEY_OVERTIME, overtime);
                params.put(KEY_MEAL, meals);
                // params.put(KEY_MILEAGE, mileage);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        String sql = "UPDATE times SET sent='1' WHERE empid = '"+empid+"' AND date ='"+thisDate+"'";
        db.execSQL(sql);

        recreate();

    }

}
