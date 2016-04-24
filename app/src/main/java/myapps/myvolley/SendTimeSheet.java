package myapps.myvolley;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SendTimeSheet extends AppCompatActivity implements View.OnClickListener {
    private static final String REGISTER_URL = "http://www.antoinjudge.hol.es/test/addTpDb.php";
    private static final String UPDATE_URL = "http://www.antoinjudge.hol.es/test/updateTS.php";
    public static final String KEY_BASIC = "basic";
    public static final String KEY_OVERTIME = "overtime";
    public static final String KEY_MEAL = "meals";
    public static final String KEY_EMPID = "empid";
    public static final String KEY_MILEAGE = "mileage";
    public static final String KEY_PWORD = "password";

    private EditText editTextBasic;
    private EditText editTextOver;
    private EditText editTextMeal;
    private EditText editTextEmpid;
    private EditText editTextMileage;
    private Button buttonUpdate;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_time_sheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextBasic = (EditText) findViewById(R.id.editTextBasic);
        editTextOver = (EditText) findViewById(R.id.editTextOverTime);
        editTextEmpid = (EditText) findViewById(R.id.editTextEmpID);
        editTextMeal = (EditText) findViewById(R.id.editTextMeals);
        editTextMileage = (EditText) findViewById(R.id.editTextMileage);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);
        buttonUpdate= (Button ) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);


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

            sendTimesheet();

        }
        else if(v == buttonUpdate){
            updateTimesheet();
        }
    }


    private void sendTimesheet() {
        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String email = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        final String pword =sharedPreferences.getString(LoginActivity.PASSWORD_SHARED_PREF,"Not Available");
        final String myempid =sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF,"Not Available");

        final String basic = editTextBasic.getText().toString().trim();
        final String overtime = editTextOver.getText().toString().trim();
        final String meals = editTextMeal.getText().toString().trim();
       // final String empid = editTextEmpid.getText().toString().trim();
        final String mileage = editTextMileage.getText().toString().trim();
        final String password = pword.toString().trim();
        final String empid = myempid.toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
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
                params.put(KEY_PWORD, password);
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
        final String password = pword.toString().trim();

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
                params.put(KEY_PWORD, password);
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
