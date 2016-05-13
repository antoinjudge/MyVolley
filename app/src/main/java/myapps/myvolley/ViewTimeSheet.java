package myapps.myvolley;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewTimeSheet extends AppCompatActivity implements View.OnClickListener {

    private EditText editTEmpID;
    private EditText editTBasic;
    private EditText editTMeals;
    //private EditText editTMileage;
    private EditText editTOT;
    private EditText editDate;
    private EditText statusTV;
    private Button btnPrev;
    private Button btnNext;
    private Button btnSave;
    private Button btnDelete;
    private TextView dayTxt;
    private TextView dateTxt;

private static final String SELECT_SQL = "SELECT * FROM times";
   // private static final String SELECT_SQL = "SELECT SUM(basic),SUM (overtime), SUM(meals) FROM times WHERE date BETWEEN '2016/05/11' AND '2016/05/14'";
    private SQLiteDatabase db;

    private Cursor c;
    private Cursor cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_time_sheet);

        //Get The Date of First Day Of Week
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String firstDate = simpleDateFormat.format(calendar.getTime());

        //Get todays Date
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);

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

        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        //btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setVisibility(View.INVISIBLE);

        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
       // btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        //SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
       // String myempid = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        //String empID = myempid.toString().trim();
        //int myEmpId= Integer.parseInt(empID);



           c = db.rawQuery(SELECT_SQL, null);
        if ( c.moveToFirst() ) {
            // start activity a
            c.moveToFirst();
            showRecords();
        }
        else{
            Toast.makeText(getApplicationContext(), "No records in Database", Toast.LENGTH_LONG).show();
        }

        cur = db.rawQuery(SELECT_SQL, null);
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
        String emplid = c.getString(0);
        String basic = c.getString(1);
        String overtime = c.getString(2);
        String meals = c.getString(3);
       // String mileage = c.getString(4);
        String date =c.getString(4);
        String status=c.getString(5);
        String sts ="";
        if(status.equals("0")){
             sts="Not Submitted";
        }else{
            sts ="Submitted";
        }

        editTEmpID.setText("Employee ID :"+emplid);
        editTBasic.setText("Basic Hours : "+basic);
        editTOT.setText("Overtime Hours : "+overtime);
        editTMeals.setText("Meals : "+meals);
        //editTMileage.setText("Mileage : "+ mileage);
        editDate.setText( date);
        statusTV.setText("Status :"+ sts);
        dateTxt.setText(date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date(date);
        String dayOfTheWeek = sdf.format(d);
        dayTxt.setText(dayOfTheWeek);

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
  //  protected void saveRecord() {
   //     String empid = editTEmpID.getText().toString().trim();
    //    String basic = editTBasic.getText().toString().trim();
    //    String overtime = editTOT.getText().toString().trim();
//
    //    String sql = "UPDATE times SET empid='" + empid + "', basic='" + basic + "' WHERE empid=" + empid + "';";

    //    if (basic.equals("") || overtime.equals("") || empid.equals("")) {
    //        Toast.makeText(getApplicationContext(), "You cannot save blank values", Toast.LENGTH_LONG).show();
    //        return;
    //    }
//

     //   db.execSQL(sql);
      //  Toast.makeText(getApplicationContext(), "Records Saved Successfully", Toast.LENGTH_LONG).show();
      //  c = db.rawQuery(SELECT_SQL, null);
     //   c.moveToPosition(Integer.parseInt(empid));
   // }

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

    @Override
    public void onClick(View v) {
        if (v == btnNext) {
            moveNext();
        }

        if (v == btnPrev) {
            movePrev();
        }

       //if (v == btnSave) {
        //    saveRecord();
      //  }
        if (v == btnDelete) {
            deleteRecord();
        }


    }
}
