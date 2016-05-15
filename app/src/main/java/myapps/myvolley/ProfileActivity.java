package myapps.myvolley;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Antoin on 25/01/2016.
 */
public class ProfileActivity extends AppCompatActivity {
    //Textview to show currently logged in user
    private TextView textView;
    private Button changeLogJourney;
    private Button changeAddItem;
    private ImageButton journeyImgBtn;
    private ImageButton hoursImgBtn;
    private ImageButton submitImgBtn;
    private ImageButton expImgBtn;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //ActionBar actionBar = getSupportActionBar();
       // actionBar.hide();
        setContentView(R.layout.activity_profile);

        //Initializing textview
        textView = (TextView) findViewById(R.id.textView);


       // changeAddItem=(Button) findViewById(R.id.buttonAddItem);
        journeyImgBtn =(ImageButton) findViewById(R.id.fuelButton);
        hoursImgBtn=(ImageButton) findViewById(R.id.hoursButton);
        submitImgBtn=(ImageButton) findViewById(R.id.submitButton);
        expImgBtn=(ImageButton) findViewById(R.id.expenseButton);


        journeyImgBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        JourneyActivity.class);
                startActivity(i);
                finish();
            }
        });
        expImgBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        showAll.class);
                startActivity(i);
                finish();
            }
        });

        submitImgBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SendTimeSheet.class);
                startActivity(i);
                finish();
            }
        });

        hoursImgBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        TimeSheet.class);
                startActivity(i);
                finish();
            }
        });
        textView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                logout();
            }
        });

        //Fetching employee id from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String empid = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        String pword =sharedPreferences.getString(LoginActivity.PASSWORD_SHARED_PREF,"Not Available");

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);

        //Showing the current logged in email to textview
        //textView.setText( pword+ " Is Logged in " + empid + " " + todayDate );
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Logout function
    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Putting the value false for loggedin
                        editor.putBoolean(LoginActivity.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to emp;oyeeId
                        editor.putString(LoginActivity.EMPID_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            //calling logout method when the logout button is clicked
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Profile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://myapps.myvolley/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Profile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://myapps.myvolley/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}