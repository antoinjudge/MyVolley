package myapps.myvolley;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewWeekly extends AppCompatActivity implements View.OnClickListener {

    private EditText editTEmpID;
    private EditText editTBasic;
    private EditText editTMeals;
    //private EditText editTMileage;
    private EditText editTOT;
    private EditText editDate;
    private EditText statusTV;

    private Button btnSave;
    private Button buttonSearch;
    private TextView dayTxt;
    private TextView dateTxt;
    private TextView startText;
    private TextView endText;
    private TextView dateRange;



    // private static final String SELECT_SQL = "SELECT * FROM times";
    private static final String SELECT_SQL = "SELECT SUM(basic),SUM (overtime), SUM(meals) FROM times WHERE date BETWEEN '2016/05/11' AND '2016/05/14'";
    // private static final String SELECT_SQL2 = "SELECT SUM(dist) FROM journey WHERE date BETWEEN '"+firstDate+"' AND '"+thisDate+"'";
    private SQLiteDatabase db;

    private Cursor c;
    private Cursor cur;
    private Cursor curR;
    private Cursor curR1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_weekly);

        //Get The Date of First Day Of Week
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String firstDate = simpleDateFormat.format(calendar.getTime());

        //Get todays Date
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);

        String SELECT_SQL1 = "SELECT SUM(basic),SUM (overtime), SUM(meals) FROM times WHERE date BETWEEN '2016/05/11' AND '2016/05/14'";
        String SELECT_SQL2 = "SELECT SUM(dist) FROM journey WHERE date BETWEEN '"+firstDate+"' AND '"+thisDate+"'";



        openDatabase();

        editTEmpID = (EditText) findViewById(R.id.textViewId);
        editTBasic = (EditText) findViewById(R.id.textViewBasic);
        editTOT = (EditText) findViewById(R.id.textViewOverTime);
        editTMeals = (EditText) findViewById(R.id.textViewMeals);
        //editTMileage = (EditText) findViewById(R.id.textViewMileage);
        editDate =(EditText) findViewById(R.id.textViewDate);
        statusTV =(EditText) findViewById(R.id.textViewStatus);
        dayTxt=(TextView) findViewById(R.id.textDay);
        dateTxt=(TextView)findViewById(R.id.textDate);
        startText=(TextView) findViewById(R.id.startTV);
        startText.setVisibility(View.INVISIBLE);
        endText=(TextView) findViewById(R.id.endTV);
        endText.setVisibility(View.INVISIBLE);
        dateRange=(TextView)findViewById(R.id.dateRangeTV);
        buttonSearch=(Button) findViewById(R.id.btnSearch);
        buttonSearch.setOnClickListener(this);
        buttonSearch.setVisibility(View.INVISIBLE);
        dateRange.setOnClickListener(this);
        startText.setOnClickListener(this);
        endText.setOnClickListener(this);




        c = db.rawQuery(SELECT_SQL1, null);
        if ( c.moveToFirst() ) {
            // start activity a
            c.moveToFirst();
            showRecords();
        }
        else{
            Toast.makeText(getApplicationContext(), "No records in Database", Toast.LENGTH_LONG).show();
        }

        cur = db.rawQuery(SELECT_SQL2, null);
        if ( cur.moveToFirst() ) {
            // start activity a
            c.moveToFirst();
            showSumDist();
        }
        else{
            Toast.makeText(getApplicationContext(), "No records in Database", Toast.LENGTH_LONG).show();
        }

        curR = db.rawQuery(SELECT_SQL1, null);
        if ( c.moveToFirst() ) {
            // start activity a
            c.moveToFirst();
            showRecords();
        }
        else{
            Toast.makeText(getApplicationContext(), "No records in Database", Toast.LENGTH_LONG).show();
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        db = openOrCreateDatabase("CurrentDailyTS", Context.MODE_PRIVATE, null);
    }

    protected void showRecords() {
        String basic = c.getString(0);
        String overtime = c.getString(1);
        String meals = c.getString(2);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String myempid = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        String empID = myempid.toString().trim();
        int myEmpId= Integer.parseInt(empID);

        editTEmpID.setText("Employee ID :" + myEmpId);
        editTEmpID.setKeyListener(null);
        editTBasic.setText("Basic Hours : " + basic);
        editTBasic.setKeyListener(null);
        editTOT.setText("Overtime Hours : " + overtime);
        editTOT.setKeyListener(null);
        editTMeals.setText("Meals : " + meals);
        editTMeals.setKeyListener(null);

    }

    protected void showSumDist(){
        String dist = cur.getString(0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String firstDate = simpleDateFormat.format(calendar.getTime());
        editDate.setText("Total Mileage :" + dist);
        editDate.setKeyListener(null);
        statusTV.setText( "Beginning week: " + firstDate);
        statusTV.setKeyListener(null);
        dateTxt.setText("Current Week");

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

    class mDateSetListener2 implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            int mYear = year;
            NumberFormat formatter = new DecimalFormat("00");
            String theYear =formatter.format(mYear);
            int mMonth = monthOfYear;
            String theMonth =formatter.format(mMonth+1);
            int mDay = dayOfMonth;
            String theDay =formatter.format(mDay);
            String theDate =theYear+"/"+theMonth+"/"+theDay+" ";
            endText.setText(theDate);
            buttonSearch.setVisibility(View.VISIBLE);

        }
    }
    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            int mYear = year;
            NumberFormat formatter = new DecimalFormat("00");
            String theYear =formatter.format(mYear);
            int mMonth = monthOfYear;
            String theMonth =formatter.format(mMonth+1);
            int mDay = dayOfMonth;
            String theDay =formatter.format(mDay);
            String theDate =theYear+"/"+theMonth+"/"+theDay+" ";
            startText.setText(theDate);

        }
    }

    protected void searchRange() {
        String basic = curR.getString(0);
        String overtime = curR.getString(1);
        String meals = curR.getString(2);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String myempid = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        String empID = myempid.toString().trim();
        int myEmpId= Integer.parseInt(empID);

        editTEmpID.setText("Employee ID :" + myEmpId);
        editTEmpID.setKeyListener(null);
        editTBasic.setText("Basic Hours : " + basic);
        editTBasic.setKeyListener(null);
        editTOT.setText("Overtime Hours : " + overtime);
        editTOT.setKeyListener(null);
        editTMeals.setText("Meals : " + meals);
        editTMeals.setKeyListener(null);
        statusTV.setVisibility(View.INVISIBLE);


    }

    protected  void searchSumDist(){
        String dist = curR1.getString(0);
        editDate.setText( "Total Mileage :"+ dist);
        editDate.setKeyListener(null);

    }





    protected void deleteRecord() {
        String empid = editTEmpID.getText().toString().trim();
        String thisemp = empid.replaceFirst(".*?(\\d+).*", "$1");
        int myempid = Integer.parseInt(thisemp);
        String date = editDate.getText().toString().trim();
        String overtime = editTOT.getText().toString().trim();

        String sql = "DELETE FROM  times WHERE  empid='" + myempid + "' AND date = '" + date +"' ";

        //  if (basic.equals("") || overtime.equals("") || empid.equals("")) {
        // Toast.makeText(getApplicationContext(), "You cannot save blank values", Toast.LENGTH_LONG).show();
        // return;
        // }

        db.execSQL(sql);
        Toast.makeText(getApplicationContext(), "Records Deleted Successfully", Toast.LENGTH_LONG).show();
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToPosition(myempid);
    }

    protected void dateRange(){
        String SELECT_SQL3 = "SELECT SUM(basic),SUM (overtime), SUM(meals) FROM times WHERE date BETWEEN '"+startText.getText().toString().trim()+"' AND '"+endText.getText().toString().trim()+"'";
        String SELECT_SQL4 = "SELECT SUM(dist) FROM journey WHERE date BETWEEN '"+startText.getText().toString().trim()+"' AND '"+endText.getText().toString().trim()+"'";
        curR = db.rawQuery(SELECT_SQL3, null);
        if ( curR.moveToFirst() ) {
            // start activity a
            curR.moveToFirst();
            searchRange();
        }
        else{
            Toast.makeText(getApplicationContext(), "No records in Database", Toast.LENGTH_LONG).show();
        }
        curR1 = db.rawQuery(SELECT_SQL4, null);
        if ( curR1.moveToFirst() ) {
            // start activity a
            curR1.moveToFirst();
            searchSumDist();
        }
        else{
            Toast.makeText(getApplicationContext(), "No records in Database", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
       if (v == buttonSearch) {

           dateRange();

        }

      //  if (v == btnPrev) {
      //      movePrev();
      //  }

        //if (v == btnSave) {
        //    saveRecord();
        //  }
       // if (v == btnDelete) {
          //  deleteRecord();
       // }
        if (v == dateRange) {
            endText.setVisibility(View.VISIBLE);
            startText.setVisibility(View.VISIBLE);
        }
        if(v==startText){
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // String currentData = sdf.format(c);

            DatePickerDialog dialog = new DatePickerDialog(ViewWeekly.this,
                    new mDateSetListener(), mYear, mMonth, mDay);
            dialog.show();
        }
        if(v==endText){
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // String currentData = sdf.format(c);

            DatePickerDialog dialog = new DatePickerDialog(ViewWeekly.this,
                    new mDateSetListener2(), mYear, mMonth, mDay);
            dialog.show();

        }


    }
}
