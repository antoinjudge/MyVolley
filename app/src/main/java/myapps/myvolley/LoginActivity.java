package myapps.myvolley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antoin on 23/01/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOGIN_URL = "http://www.antoinjudge.hol.es/myVolley/login.php";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    private EditText editTextUsername;
    private EditText editTextPassword;
    private AppCompatButton btnLogin;
    private boolean loggedIn = false;
    private Button btnLinkToRegister;

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMPID_SHARED_PREF = "empid";
    public static final String PASSWORD_SHARED_PREF = "password";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLinkToRegister =(Button) findViewById((R.id.btnLinkToRegisterScreen));
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin = (AppCompatButton) findViewById(R.id.btnLogin);

        //Adding click listener
        btnLogin.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    private void login(){
        //Getting values from edit texts
        final String empid = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Compares the response from the server with our LOGIN_SUCCESS constant
                        if(response.equalsIgnoreCase(LOGIN_SUCCESS)){
                            //Once logged in creates a shared preference to keep the username and password of the user while logged in
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Create an editor to store the shared preference values
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to Shared Preference, boolean true and employee id and password
                            editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                            editor.putString(EMPID_SHARED_PREF, empid);
                            editor.putString(PASSWORD_SHARED_PREF, password);

                            //Saving values to editor
                            editor.commit();

                            //Starting the users profile activity
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }else{
                            //If the login is not successful show error message
                            Toast.makeText(LoginActivity.this, "Invalid employee id or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_EMPID, empid);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        //Calling the login function
        login();
    }
}