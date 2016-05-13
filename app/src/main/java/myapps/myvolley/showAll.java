package myapps.myvolley;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.app.ProgressDialog;

import android.support.v7.app.AppCompatActivity;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
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
        sendBtn=(Button)findViewById(R.id.sendJourneyBtn);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
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

    protected void showRecords() {
        String journeyid = c.getString(0);
        String startloc = c.getString(1);
        String endloc = c.getString(2);
        String dist = c.getString(3);
        String date =c.getString(4);



        editTextJId.setText("Journey ID :"+journeyid);
        startLocEdit.setText(startloc);
        endlocEdit.setText( endloc);
        distEdit.setText(dist);
        dateEdit.setText(date);
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

    private void sendJourney() {
        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String email = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        final String pword =sharedPreferences.getString(LoginActivity.PASSWORD_SHARED_PREF,"Not Available");
        final String myempid =sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");

        final String date = dateEdit.getText().toString().trim();
        final String startloc = startLocEdit.getText().toString().trim();
        final String endloc = endlocEdit.getText().toString().trim();
        final String dist = distEdit.getText().toString().trim();

        final String empid = myempid.toString().trim();
        final String theDate = dateEdit.getText().toString().trim();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
        Date todayDate = new Date();
        final String thisDate = currentDate.format(todayDate);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(showAll.this, response, Toast.LENGTH_LONG).show();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(showAll.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMPID, empid);
                params.put(KEY_DATE, date);
                params.put(KEY_START, startloc);
                params.put(KEY_END, endloc);
                params.put(KEY_DIST, dist);
                params.put(KEY_DATE, theDate);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
       // String sql = "UPDATE times SET sent='1' WHERE empid = '"+empid+"' AND date ='"+date+"'";
       // db.execSQL(sql);
       // Toast.makeText(getApplicationContext(), "Submitted, and updated your records", Toast.LENGTH_LONG).show();

    }






    @Override
    public void onClick(View v) {
        if (v == btnNext) {
            moveNext();
        }
        if (v == sendBtn) {
            sendJourney();
        }

        if (v == btnPrev) {
            movePrev();
        }
    }
}