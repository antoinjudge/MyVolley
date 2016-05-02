package myapps.myvolley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class SendTimeSheet extends AppCompatActivity implements View.OnClickListener {
    private static final String REGISTER_URL = "http://www.antoinjudge.hol.es/myVolley/updateTimesheet.php";
    private static final String UPDATE_URL = "http://www.antoinjudge.hol.es/myVolley/updateTimesheet.php";
    public static final String KEY_BASIC = "basic";
    public static final String KEY_OVERTIME = "overtime";
    public static final String KEY_MEAL = "meals";
    public static final String KEY_EMPID = "empid";
    public static final String KEY_MILEAGE = "mileage";
    public static final String KEY_PWORD = "password";
    public static final String KEY_DATE = "date";

    private EditText editTextBasic;
    private EditText editTextOver;
    private EditText editTextMeal;
    private EditText editTextEmpid;
    private EditText editTextMileage;
    private EditText editTextDate;
    private Button buttonUpdate;
    private Button buttonSubmit;
    private Button btnPrev;
    private Button btnNext;
    private Button btnSave;
    private TextView dayView;
    private EditText searchEditText;

    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
    Date todayDate = new Date();
    String thisDate = currentDate.format(todayDate);
    String today = thisDate.toUpperCase().trim();
    String SELECT_SQL = "SELECT * FROM times WHERE sent = '0'";


    private SQLiteDatabase db;

    private Cursor c;
    private Cursor cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_time_sheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        openDatabase();

        editTextBasic = (EditText) findViewById(R.id.editTextBasic);
        editTextOver = (EditText) findViewById(R.id.editTextOverTime);
        editTextEmpid = (EditText) findViewById(R.id.editTextEmpID);
        editTextMeal = (EditText) findViewById(R.id.editTextMeals);
        editTextMileage = (EditText) findViewById(R.id.editTextMileage);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        searchEditText =(EditText)findViewById(R.id.searchDateEditText);
        dayView =(TextView)findViewById(R.id.dayTv);
        btnNext=(Button)findViewById(R.id.buttonnNext);
        btnNext.setOnClickListener(this);
        btnPrev=(Button)findViewById(R.id.buttonPrev);
        btnPrev.setOnClickListener(this);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);
        buttonUpdate= (Button ) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);
        btnSave=(Button)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(this);






        c = db.rawQuery(SELECT_SQL, null);
        if ( c.moveToFirst() ) {
            // start activity a
            c.moveToFirst();
            showRecords();
        }
        else{
            Toast.makeText(getApplicationContext(), "No records in Database", Toast.LENGTH_LONG).show();
            dayView.setText(today);
        }






        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onClick(View v) {
        if(v == buttonSubmit){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SendTimeSheet.this);
            // Setting Dialog Title
            alertDialog.setTitle("Confirm Submission...");
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.common_plus_signin_btn_icon_light_focused);
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure?? Cannot be undone!!");
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

            sendTimesheet();
                }
            });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }




        if (v == btnNext) {
            movePrev();
        }
        if (v == btnPrev) {
            moveNext();
        }

         if(v == buttonUpdate){
             String date= searchEditText.getText().toString().trim();
             String SINGLE_SQL = "SELECT * FROM times WHERE date ='"+date+"' ";
             cur = db.rawQuery(SINGLE_SQL, null);
             if (  cur.moveToFirst() ) {
                 // start activity a
                 cur.moveToFirst();
                 showDaily();
                 btnNext.setVisibility(View.INVISIBLE);
                 btnSave.setVisibility(View.INVISIBLE);
                 btnPrev.setVisibility(View.INVISIBLE);
             }
             else{
                 Toast.makeText(getApplicationContext(), "No records in Database", Toast.LENGTH_LONG).show();
                 dayView.setText(today);
             }
        }
    }
    protected void openDatabase() {
        db = openOrCreateDatabase("ThisDailyTS", Context.MODE_PRIVATE, null);
    }

    protected void showRecords() {
        String emplid = c.getString(0);
        String basic = c.getString(1);
        String overtime = c.getString(2);
        String meals = c.getString(3);
        String mileage = c.getString(4);
        String date =c.getString(5);

        editTextEmpid.setText(emplid);
        editTextBasic.setText(basic);
        editTextOver.setText(overtime);
        editTextMeal.setText(meals);
        editTextMileage.setText(mileage);
        editTextDate.setText(date);
        dayView.setText(date);
    }
    protected void showDaily(){

        String emplid = cur.getString(0);
        String basic = cur.getString(1);
        String overtime = cur.getString(2);
        String meals = cur.getString(3);
        String mileage = cur.getString(4);
        String date =cur.getString(5);

        editTextEmpid.setText(emplid);
        editTextBasic.setText(basic);
        editTextOver.setText(overtime);
        editTextMeal.setText(meals);
        editTextMileage.setText(mileage);
        editTextDate.setText(date);
        dayView.setText(date);
    }


    protected void moveNext() {
        if (!c.isLast())
            c.moveToNext();

        showRecords();
    }

    protected void movePrev() {
        if (!c.isFirst())
            c.moveToPrevious();

        showRecords();

    }


    private void sendTimesheet() {
        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String email = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        final String pword =sharedPreferences.getString(LoginActivity.PASSWORD_SHARED_PREF,"Not Available");
        final String myempid =sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");

        final String basic = editTextBasic.getText().toString().trim();
        final String overtime = editTextOver.getText().toString().trim();
        final String meals = editTextMeal.getText().toString().trim();
       // final String empid = editTextEmpid.getText().toString().trim();
        final String mileage = editTextMileage.getText().toString().trim();
        final String password = pword.toString().trim();
        final String empid = myempid.toString().trim();
        final String date = editTextDate.getText().toString().trim();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
        Date todayDate = new Date();
        final String thisDate = currentDate.format(todayDate);

        if (basic.equals("") || overtime.equals("") || meals.equals("") || mileage.equals("") || date.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SendTimeSheet.this, response, Toast.LENGTH_LONG).show();
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SendTimeSheet.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMPID, empid);
                params.put(KEY_DATE, date);
                params.put(KEY_BASIC, basic);
                params.put(KEY_OVERTIME, overtime);
                params.put(KEY_MEAL, meals);
                params.put(KEY_MILEAGE, mileage);

                return params;
            }





        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        String sql = "UPDATE times SET sent='1' WHERE empid = '"+empid+"' AND date ='"+date+"'";
        db.execSQL(sql);
        Toast.makeText(getApplicationContext(), "Records Saved Successfully", Toast.LENGTH_LONG).show();

    }

    private void updateTimesheet(){
        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        //String email = sharedPreferences.getString(LoginActivity.EMAIL_SHARED_PREF, "Not Available");
        final String pword =sharedPreferences.getString(LoginActivity.PASSWORD_SHARED_PREF,"Not Available");


        final String basic = editTextBasic.getText().toString().trim();
        final String overtime = editTextOver.getText().toString().trim();
        final String meals = editTextMeal.getText().toString().trim();
        final String empid = editTextEmpid.getText().toString().trim();
        final String mileage = editTextMileage.getText().toString().trim();
        final String date = editTextDate.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SendTimeSheet.this, response, Toast.LENGTH_LONG).show();
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SendTimeSheet.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMPID, empid);
                params.put(KEY_DATE, date);
                params.put(KEY_BASIC, basic);
                params.put(KEY_OVERTIME, overtime);
                params.put(KEY_MEAL, meals);
                params.put(KEY_MILEAGE, mileage);

                return params;
            }




        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}
