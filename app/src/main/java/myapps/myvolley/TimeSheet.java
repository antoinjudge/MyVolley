package myapps.myvolley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TimeSheet extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase db;

    private EditText editTBasic;
    private EditText editTOT;
    private EditText editTMeals;
    private  EditText editTEmpID;
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

        btnAdd = (Button) findViewById(R.id.buttonAddToTimeSheet);
        btnView = (Button) findViewById(R.id.buttonViewTimeSheet);

        btnAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);


        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String email = sharedPreferences.getString(LoginActivity.EMAIL_SHARED_PREF, "Not Available");
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
        db = openOrCreateDatabase("TimesheetDB", Context.MODE_PRIVATE, null);
    }

    protected void createDatabase(){
        db=openOrCreateDatabase("TimesheetDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS timesheet(empid INTEGER PRIMARY KEY  NOT NULL, basic INTEGER,overtime INTEGER, meals INTEGER, mileage INTEGER);");
    }

    protected void insertIntoDB(){
        String empID = editTOT.getText().toString().trim();
        String basic = editTBasic.getText().toString().trim();
        String overtime = editTOT.getText().toString().trim();
        String meals = editTMeals.getText().toString().trim();
        String mileage = editTMiles.getText().toString().trim();
        if( basic.equals("") || overtime.equals("") || meals.equals("") || mileage.equals("") || empID.equals("")   ){
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        String query = "INSERT INTO timesheet (empID,basic,overtime, meals, mileage) VALUES('"+empID+"', '"+basic+"', '"+overtime+"', '"+meals+"', '"+mileage+"'  );";
        db.execSQL(query);
        Toast.makeText(getApplicationContext(),"Saved Successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if(v == btnAdd){
            insertIntoDB();
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
