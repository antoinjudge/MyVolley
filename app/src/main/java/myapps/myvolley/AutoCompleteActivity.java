package myapps.myvolley;

import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import myapps.myvolley.AutoCompleteAdapter;
import myapps.myvolley.RecyclerListener;
import myapps.myvolley.AutoCompleteAdapter;
import  myapps.myvolley.Config;

/**
 * Created by Antoin on 06/05/2016.
 */
public class AutoCompleteActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    protected GoogleApiClient mGoogleApiClient;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private EditText mAutocompleteView;
    TextView output ;
    private EditText mAutocompleteViewTwo;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewTwo;
    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayoutManager mLinearLayoutManagerTwo;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private AutoCompleteAdapter mAutoCompleteAdapterTwo;
    private Button getKms;
    private TextView addTextView;
    private TextView distTextView;
    RequestQueue requestQueue;
    String data = "";
    private Button reset;
    ImageView delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_search);
        getKms =(Button) findViewById(R.id.getDistBtn);
        getKms.setVisibility(View.INVISIBLE);
        mAutocompleteView = (EditText)findViewById(R.id.autocomplete_places);
        mAutocompleteViewTwo=(EditText ) findViewById(R.id.autocomplete_places_two);
        delete=(ImageView)findViewById(R.id.cross);
        mAutoCompleteAdapter =  new AutoCompleteAdapter(this, R.layout.search_adapter,
                mGoogleApiClient, BOUNDS_INDIA, null);
        mAutoCompleteAdapterTwo =  new AutoCompleteAdapter(this, R.layout.search_adapter_two,
                mGoogleApiClient, BOUNDS_INDIA, null);
        //Recycler View 1
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerViewTwo=(RecyclerView)findViewById(R.id.recyclerViewTwo);
        mLinearLayoutManager=new LinearLayoutManager(this);
        mLinearLayoutManagerTwo=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);
        //Recycler View 2
        mRecyclerViewTwo.setLayoutManager(mLinearLayoutManagerTwo);
        mRecyclerViewTwo.setAdapter(mAutoCompleteAdapterTwo);
        delete.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);

        reset =(Button) findViewById(R.id.resetBtn);
        reset.setVisibility(View.INVISIBLE);
        addTextView=(TextView)findViewById(R.id.addTV);
        addTextView.setOnClickListener(this);
        addTextView.setVisibility(View.INVISIBLE);
        distTextView=(TextView) findViewById(R.id.distTV);
        distTextView.setVisibility(View.INVISIBLE);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
                mAutocompleteViewTwo.setText("");
                mAutocompleteView.setText("");

            }
        });
        getKms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mAutocompleteViewTwo.setText("");
               //mAutocompleteView.setText("");
                //EditText editText = (EditText) findViewById(R.id.autocomplete_places);
                String startAddress = mAutocompleteView.getText().toString();
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

                //EditText endEditText = (EditText) findViewById(R.id.autocomplete_places_two);
                String endAddress = mAutocompleteViewTwo.getText().toString();

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
                                            //addButton.setVisibility(View.VISIBLE);
                                           // distanceTV.setVisibility(View.VISIBLE);
                                            Toast.makeText(getApplicationContext(), "Distance i Kilometers is "+ distKm, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                    distTextView.setVisibility(View.VISIBLE);
                                    distTextView.setText(data);
                                    addTextView.setVisibility(View.VISIBLE);




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




        mAutocompleteView.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        mAutocompleteViewTwo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapterTwo.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerListener(this, new RecyclerListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final AutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getCount() == 1) {
                                    //Do the things here on Click.....
                                    Toast.makeText(getApplicationContext(), String.valueOf(places.get(0).getLatLng()), Toast.LENGTH_SHORT).show();
                                    mAutocompleteView.setText(item.description);
                                    mRecyclerView.setVisibility(View.INVISIBLE);
                                    places.release();

                                } else {
                                    Toast.makeText(getApplicationContext(), Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );

        mRecyclerViewTwo.addOnItemTouchListener(
                new RecyclerListener(this, new RecyclerListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final AutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapterTwo.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getCount() == 1) {
                                    //Do the things here on Click.....
                                    Toast.makeText(getApplicationContext(), String.valueOf(places.get(0).getLatLng()), Toast.LENGTH_SHORT).show();
                                    mAutocompleteViewTwo.setText(item.description);
                                    mRecyclerViewTwo.setVisibility(View.INVISIBLE);
                                    places.release();
                                    getKms.setVisibility(View.VISIBLE);
                                    reset.setVisibility(View.VISIBLE);
                                    try  {
                                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    } catch (Exception e) {

                                    }



                                } else {
                                    Toast.makeText(getApplicationContext(), Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback","Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v==delete){
            mAutocompleteView.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
            Log.v("Google API","Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            Log.v("Google API","Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
