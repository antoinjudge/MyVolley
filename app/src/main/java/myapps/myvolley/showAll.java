package myapps.myvolley;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.app.ProgressDialog;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Antoin on 21/01/2016.
 */
public class showAll extends AppCompatActivity implements View.OnClickListener {
    private static final String UPDATE_URL = "http://www.antoinjudge.hol.es/myVolley/updateJourney.php";
    public static final String KEY_START = "startloc";
    public static final String KEY_END = "endloc";
    public static final String KEY_DIST = "dist";
    public static final String KEY_EMPID = "empid";
    public static final String KEY_DATE = "date";

    private EditText editTextJId;
    private EditText startLocEdit;
    private EditText endlocEdit;
    private EditText distEdit;
    private EditText dateEdit;
    private TextView dayText;
    private TextView datetext;
    private TextView resultDate;
    private Button btnPrev;
    private Button btnNext;
    private Button sendBtn;

    private static final String SELECT_SQL = "SELECT * FROM journey";
    private SQLiteDatabase db;

    private Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show_all
        );
        openDatabase();

        editTextJId = (EditText) findViewById(R.id.editTextJourneyId);
        startLocEdit = (EditText) findViewById(R.id.editTextStartLoc);
        endlocEdit = (EditText) findViewById(R.id.editTextEndLoc);
        distEdit = (EditText) findViewById(R.id.editTextDist);
        dateEdit =(EditText) findViewById(R.id.editTextDate);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        resultDate=(TextView) findViewById(R.id.resultDate);
        resultDate.setOnClickListener(this);

        //sendBtn=(Button)findViewById(R.id.sendJourneyBtn);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        //sendBtn.setOnClickListener(this);
        //sendBtn.setVisibility(View.INVISIBLE);
        datetext =(TextView) findViewById(R.id.textDate);
        dayText =(TextView) findViewById(R.id.textDay);

        c = db.rawQuery(SELECT_SQL, null);
        if ( c.moveToFirst() ) {

            c.moveToFirst();
            showRecords();
        }
        else{
            Toast.makeText(getApplicationContext(), "No records in Database", Toast.LENGTH_LONG).show();
        }







    }
    protected void openDatabase() {
        db = openOrCreateDatabase("CurrentDailyTS", Context.MODE_PRIVATE, null);
    }

    protected void searchDate(){
        String date= resultDate.getText().toString().trim();
        String SelectDate= "SELECT * FROM journey WHERE date = '"+date+"'";
        c = db.rawQuery(SelectDate, null);
        if ( c.moveToFirst() ) {

            c.moveToFirst();
            showRecords();
        }
        else{
            Toast.makeText(getApplicationContext(), "No records for that Date", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(),
                    showAll.class);
            startActivity(i);
            finish();
        }


    }

    protected void showRecords() {
        String journeyid = c.getString(0);
        String startloc = c.getString(1);
        String endloc = c.getString(2);
        String dist = c.getString(3);
        String date =c.getString(4);



        editTextJId.setText("Journey ID :" + journeyid);
        editTextJId.setKeyListener(null);
        startLocEdit.setText("Start: "+startloc);
        startLocEdit.setKeyListener(null);
        endlocEdit.setText("End: "+endloc);
        endlocEdit.setKeyListener(null);
        distEdit.setText("Kilometers: "+dist);
        distEdit.setKeyListener(null);
        dateEdit.setText("Date: "+date);
        dateEdit.setKeyListener(null);
        datetext.setText( date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date(date);
        String dayOfTheWeek = sdf.format(d);
        dayText.setText(dayOfTheWeek);


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
            resultDate.setText(theDate);
            searchDate();
        }
    }








    @Override
    public void onClick(View v) {
        if (v == btnNext) {
            moveNext();
        }

        if(v==resultDate){
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // String currentData = sdf.format(c);

            DatePickerDialog dialog = new DatePickerDialog(showAll.this,
                    new mDateSetListener(), mYear, mMonth, mDay);
            dialog.show();
        }


        if (v == btnPrev) {
            movePrev();
        }
    }
}