package myapps.myvolley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Geocoder;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class JourneyActivity extends AppCompatActivity implements LocationListener{

    Button addressButton;
    Button changeTS;
    Button addButton;
    TextView addressTV;
    TextView latLongTV;
    TextView endLatLongTV;
    TextView output;
    TextView distanceTV;
    TextView manualTV;

    private static final String SELECT_SQL = "SELECT * FROM times  ";
    private SQLiteDatabase db;

    String journeyStart = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
    String mid = "&destinations=";
    String journeyEnd = "&key=AIzaSyDDW-ZGLBHF8DybvfYmvXPY20l-4CIw-e4";
    String loginURL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Drogheda&destinations=Cork&key=AIzaSyDDW-ZGLBHF8DybvfYmvXPY20l-4CIw-e4";
    String data = "";
    //String URL="";
    String startAddress = "";
    String endAddress = "";
    //String route ="";
    StringBuilder stringBuilder = new StringBuilder();
    RequestQueue requestQueue;
    private static final String TAG = "myApp";
    latLongTracker gps;
    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        openDatabase();





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState!=null){
            Log.d("STATE", savedInstanceState.toString());
        }
        Intent intent = new Intent(getApplicationContext(), latLongTracker.class);
        startActivityForResult(intent, 1);

        addressTV = (TextView) findViewById(R.id.startAddressTV);
        latLongTV = (TextView) findViewById(R.id.latLongTV);
        manualTV=(TextView) findViewById(R.id.manualTV);

        addressButton = (Button) findViewById(R.id.addressButton);
        ///changeTS =(Button) findViewById(R.id.changeSubmit);
        addButton =(Button) findViewById(R.id.addtoDatabase);
        addButton.setVisibility(View.INVISIBLE);
        distanceTV =(TextView) findViewById(R.id.distanceTV);
        distanceTV.setVisibility(View.INVISIBLE);
        requestQueue = Volley.newRequestQueue(this);
        output = (TextView) findViewById(R.id.latLongTV);




        manualTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        AutoCompleteActivity.class);
                startActivity(i);
                finish();
            }
        });
        addressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {




                EditText editText = (EditText) findViewById(R.id.startAddressET);
                Geocoder gCoder = new Geocoder(getApplicationContext());
                ArrayList<Address> addresses = null;
                try {
                    addresses = (ArrayList<Address>) gCoder.getFromLocation(53.7209, -6.3682, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder();
                    for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }


                    editText.setText(strReturnedAddress.toString());
                }else{
                    editText.setText("No Address!!");

                }

                    Toast.makeText(getApplicationContext(), "country: " + addresses.get(0).getMaxAddressLineIndex(), Toast.LENGTH_LONG).show();
                }




        });






        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                output = (TextView) findViewById(R.id.latLongTV);
                output.setText("");
                EditText editText = (EditText) findViewById(R.id.startAddressET);
                startAddress = editText.getText().toString();

                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }


                EditText endEditText = (EditText) findViewById(R.id.endAddressET);
                endAddress = endEditText.getText().toString();

                final String FORECAST_BASE_URL =
                        "https://maps.googleapis.com/maps/api/distancematrix/json?";
                final String QUERY_PARAM = "origins";
                final String END_QUERY = "&destinations";
                final String KEY = "&key";

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("maps.googleapis.com")
                        .appendPath("maps")
                        .appendPath("api")
                        .appendPath("distancematrix")
                        .appendPath("json")
                        .appendQueryParameter("origins", startAddress)
                        .appendQueryParameter("destinations", endAddress)
                        .appendQueryParameter("key", "AIzaSyDDW-ZGLBHF8DybvfYmvXPY20l-4CIw-e4");
                String myUrl = builder.build().toString();



                JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, myUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try{

                                    JSONArray array = response.getJSONArray("rows");

                                    for(int i=0; i < array.length(); i++) {
                                        JSONObject myJ = array.getJSONObject(i);
                                        String myD = myJ.getString("elements");

                                        JSONArray jsonObject = new JSONArray(myD);
                                        for (int j = 0; j < jsonObject.length(); j++) {
                                            String theObj = jsonObject.getJSONObject(j).getString("distance");

                                            JSONObject jObjt = new JSONObject(theObj);
                                            String finalDist = jObjt.getString("value");
                                            Integer myDist = Integer.valueOf(finalDist);
                                            int distKm = (myDist / 1000);

                                            data = distKm + "\n";
                                            addButton.setVisibility(View.VISIBLE);
                                            distanceTV.setVisibility(View.VISIBLE);

                                        }
                                    }

                                    output.setText( data );

                                }catch(JSONException e){e.printStackTrace();}
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley","Error");

                            }
                        }
                );
                requestQueue.add(jor);



            }



        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertIntoDB();

            }
        });




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
    protected void insertIntoDB(){
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREF_NAME, LoginActivity.MODE_PRIVATE);
        String myempid = sharedPreferences.getString(LoginActivity.EMPID_SHARED_PREF, "Not Available");
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);

        String empID = myempid.toString().trim();
        int myEmpId= Integer.parseInt(empID);
        String mileage = output.getText().toString().trim();
        int myMileage = Integer.parseInt(mileage);
        String date = thisDate.toUpperCase().trim();
        EditText editText = (EditText) findViewById(R.id.startAddressET);
        startAddress = editText.getText().toString();
        EditText endEditText = (EditText) findViewById(R.id.endAddressET);
        endAddress = endEditText.getText().toString();


        //if(isEntry(date)){
        //  String myQuery = "UPDATE times SET empid = '"+empID+"', basic ='"+(basic + basic)+"', overtime = '"+(overtime+overtime)+"', meals = '"+(meals+meals)+"', mileage ='"+(mileage+mileage)+"', date ='"+date+"';";
        //db.execSQL(myQuery);
        //}
        //else {
       // String query = "INSERT OR IGNORE INTO times (empID, date) VALUES('" + myEmpId + "',  '" + date + "' );";// UPDATE times SET( empId = '"+empID+"',basic = '"+(basic+ 100)+" WHERE date = '"+date+");";
       // db.execSQL(query);
       // String query2 = "UPDATE times SET   mileage = mileage + '"+myMileage+"' WHERE date = '"+date+"' AND empID = '"+myEmpId+"'";
       // db.execSQL(query2);
        String query3 = "INSERT INTO journey (startloc, endloc, dist, date )VALUES('"+startAddress+"', '"+endAddress+"', '"+myMileage+"' , '"+date+"');";
        db.execSQL(query3);
        Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
        //}
    }

    @Override
    public void onLocationChanged(Location location) {
        // Getting reference to TextView tv_longitude
        TextView tvLongitude = (TextView)findViewById(R.id.startAddressET);

        // Getting reference to TextView tv_latitude
        TextView tvLatitude = (TextView)findViewById(R.id.endAddressET);

        // Setting Current Longitude
        tvLongitude.setText("Longitude:" + location.getLongitude());

        // Setting Current Latitude
        tvLatitude.setText("Latitude:" + location.getLatitude() );
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            String endLocationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    endLocationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
                    endLocationAddress = null;
            }
            latLongTV.setText(locationAddress);
            endLatLongTV.setText(endLocationAddress);

        }
        public void handleDistance(Message message){
            String distance;

        }
    }

}

