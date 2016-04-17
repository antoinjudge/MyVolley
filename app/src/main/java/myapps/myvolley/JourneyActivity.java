package myapps.myvolley;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Geocoder;

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

import java.net.URL;

public class JourneyActivity extends AppCompatActivity {

    Button addressButton;
    TextView addressTV;
    TextView latLongTV;
    TextView endLatLongTV;
    TextView output ;
    String journeyStart ="https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
    String mid ="&destinations=";
    String journeyEnd ="&key=AIzaSyDDW-ZGLBHF8DybvfYmvXPY20l-4CIw-e4";
    String loginURL="https://maps.googleapis.com/maps/api/distancematrix/json?origins=Drogheda&destinations=Cork&key=AIzaSyDDW-ZGLBHF8DybvfYmvXPY20l-4CIw-e4";
    String data = "";
    //String URL="";
    String startAddress="";
    String endAddress="";
    //String route ="";
    StringBuilder stringBuilder = new StringBuilder();
    RequestQueue requestQueue;
    private static final String TAG = "myApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState!=null){
            Log.d("STATE", savedInstanceState.toString());
        }

        addressTV = (TextView) findViewById(R.id.startAddressTV);
        latLongTV = (TextView) findViewById(R.id.latLongTV);
        endLatLongTV =(TextView) findViewById(R.id.endLatLongTV);
        addressButton = (Button) findViewById(R.id.addressButton);
        requestQueue = Volley.newRequestQueue(this);
        output = (TextView) findViewById(R.id.latLongTV);



        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                output.setText("");
                EditText editText = (EditText) findViewById(R.id.startAddressET);
                startAddress = editText.getText().toString();
               // GeocodingLocation locationAddress = new GeocodingLocation();
                //locationAddress.getAddressFromLocation(startAddress,
                  //      getApplicationContext(), new GeocoderHandler());

                EditText endEditText = (EditText) findViewById(R.id.endAddressET);
                endAddress = endEditText.getText().toString();
                //GeocodingLocation endLocationAddress = new GeocodingLocation();
                //endLocationAddress.getAddressFromLocation(endAddress,
                    //    getApplicationContext(), new GeocoderHandler());
                //route = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+startAddress+"&destinations="+endAddress+"&key=AIzaSyDDW-ZGLBHF8DybvfYmvXPY20l-4CIw-e4";
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

                //String route = builder.build().toString();


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


                                            // int id = Integer.parseInt(jsonObject.optString("id").toString());
                                            //String title = jsonObject.getString();
                                            //String url = jsonObject.getString("URL");

                                            data += finalDist + "\n";
                                        }
                                    }


                                    output.setText("final distance is: "+ data);
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });






    }

    private class getDistance  {

        private String myDistance(LatLng my_latlong,LatLng frnd_latlong){
          Location l1=new Location("One");
           l1.setLatitude(my_latlong.latitude);l1.setLongitude(my_latlong.longitude);

            Location l2=new Location("Two");
            l2.setLatitude(frnd_latlong.latitude);
            l2.setLongitude(frnd_latlong.longitude);

            float distance=l1.distanceTo(l2);
            String dist=distance+" M";

            if(distance>1000.0f)
            {
                distance=distance/1000.0f;
                dist=distance+" KM";
            }
            return dist;
        }
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

