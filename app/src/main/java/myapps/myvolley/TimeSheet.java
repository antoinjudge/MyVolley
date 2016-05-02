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

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSheet extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase db;
    private static final String SELECT_SQL = "SELECT * FROM times";
    private Cursor c;

    private EditText editTBasic;
    private EditText editTOT;
    private EditText editTMeals;
    private  EditText editTEmpID;
    private TextView dateTV;
    private Button btnAdd;
    private EditText editTMiles;
    private  Button btnView;



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
        editTMiles = (EditText) findViewById(R.id.editTextMileage);
        editTEmpID =(EditText) findViewById(R.id.editTextEmpID);
        dateTV=(TextView) findViewById(R.id.textView3);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        dateTV.setText(dayOfTheWeek);

        btnAdd = (Button) findViewById(R.id.buttonAddToTimeSheet);
        btnView = (Button) findViewById(R.id.buttonViewTimeSheet);

        btnAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);


        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
       // String email = sharedPreferences.getString(LoginActivity.EMAIL_SHARED_PREF, "Not Available");
        String pword =sharedPreferences.getString(LoginActivity.PASSWORD_SHARED_PREF,"Not Available");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("MyDailyTS", Context.MODE_PRIVATE, null);
    }

    protected void createDatabase(){
        db=openOrCreateDatabase("MyDailyTS", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS times(empid INTEGER , basic INTEGER  NOT NULL DEFAULT 0,overtime INTEGER NOT NULL DEFAULT 0, meals INTEGER NOT NULL DEFAULT 0, mileage INTEGER NOT NULL DEFAULT 0, date TEXT PRIMARY KEY  NOT NULL);");

    }


    protected void insertIntoDB(){
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String myempid = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);

        String empID = myempid.toString().trim();
        int myEmpId= Integer.parseInt(empID);
        String basic = editTBasic.getText().toString().trim();
        int mybasic = Integer.parseInt(basic);
        String overtime = editTOT.getText().toString().trim();
        int myOT = Integer.parseInt(overtime);
        String meals = editTMeals.getText().toString().trim();
        int myMeals= Integer.parseInt(meals);
        String mileage = editTMiles.getText().toString().trim();
        int myMileage = Integer.parseInt(mileage);
        String date = thisDate.toUpperCase().trim();

        if( basic.equals("") || overtime.equals("") || meals.equals("") || mileage.equals("") || empID.equals("")   ){
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        //if(isEntry(date)){
          //  String myQuery = "UPDATE times SET empid = '"+empID+"', basic ='"+(basic + basic)+"', overtime = '"+(overtime+overtime)+"', meals = '"+(meals+meals)+"', mileage ='"+(mileage+mileage)+"', date ='"+date+"';";
            //db.execSQL(myQuery);
        //}
        //else {
            String query = "INSERT OR IGNORE INTO times (empID, date) VALUES('" + myEmpId + "',  '" + date + "' );";// UPDATE times SET( empId = '"+empID+"',basic = '"+(basic+ 100)+" WHERE date = '"+date+");";
        db.execSQL(query);
            String query2 = "UPDATE times SET basic = basic +'"+mybasic+"',overtime= overtime+ '"+myOT+"', meals = meals +'"+myMeals+"', mileage = mileage + '"+myMileage+"' WHERE date = '"+date+"' AND empID = '"+myEmpId+"'";
           db.execSQL(query2);
            Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
        //}
    }

    public boolean isEntry(String date ){


        String queryString = "SELECT * FROM times WHERE date = "+"'"+date+"'";
        Cursor c = db.rawQuery(queryString, null);
        if(c.getCount() > 0){
            Log.i("CHECK", "true");
            return true;
        }
        else{
            return false;
        }
    }



    @Override
    public void onClick(View v) {
        if(v == btnAdd){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeSheet.this);
            // Setting Dialog Title
            alertDialog.setTitle("Confirm Submission...");
            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.common_plus_signin_btn_icon_light_focused);
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want submit this timesheet??");
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    // Write your code here to invoke YES event
                    insertIntoDB();
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
        if(v==btnView){
            showPeoples();
        }
    }

    private void showPeoples(){
        Intent intent = new Intent(this,ViewTimeSheet.class);
        startActivity(intent);
        finish();
    }
}
