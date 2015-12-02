package com.nyuprojectx.androidweather;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nyuprojectx.androidweather.service.GpsService;
import com.nyuprojectx.androidweather.service.ServerRequests;
import com.nyuprojectx.androidweather.user.GetUserCallback;
import com.nyuprojectx.androidweather.user.Post;
import com.nyuprojectx.androidweather.user.User;
import com.nyuprojectx.androidweather.user.UserLocalStore;

import java.io.IOException;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private EditText postEditText;
    private Spinner moodSpinner;
    private TextView informationTextView;
    private Button postButton, cancelButton;

    private LatLng latLng;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
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

        postEditText = (EditText) findViewById(R.id.postEditText);
        moodSpinner = (Spinner) findViewById(R.id.moodSpinner);
        informationTextView = (TextView) findViewById(R.id.informationTextView);
        postButton = (Button) findViewById(R.id.postButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        getLocationInfomation();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mood_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(adapter);
        // moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //     @Override
        //     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //         moodIndex = position;
        //     }

        //     @Override
        //     public void onNothingSelected(AdapterView<?> parent) {

        //     }
        // });

        informationTextView.setText("Lat: " + String.valueOf(latLng.latitude) + ", Lng: " + String.valueOf(latLng.longitude)
                + "\nYour Location: " + location);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLocalStore userLocalStore = new UserLocalStore(PostActivity.this);
                if (userLocalStore.getLoggedInUser() == null) {
                    Toast.makeText(getBaseContext(), "Not logged in.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String status = postEditText.getText().toString();
                    User user = userLocalStore.getLoggedInUser();
                    int moodIndex = moodSpinner.getSelectedItemPosition();
                    Post post = new Post(user.uid, latLng, status, moodIndex);
                    Toast.makeText(getBaseContext(), String.valueOf(moodIndex), Toast.LENGTH_SHORT).show();
                    ServerRequests serverRequest = new ServerRequests(PostActivity.this);
                    serverRequest.postAsyncTask(post, new GetUserCallback() {
                        @Override
                        public void done(User returnedUser) {
                            finish();
                        }
                    });
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void getLocationInfomation() {
        // Get latitude and longtitude
        GpsService gps = new GpsService(PostActivity.this);
        if (gps.canGetLocation()) {
            latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        }
        else {
            latLng = new LatLng(0, 0);
        }
        // Get location name
        Geocoder gc = new Geocoder(PostActivity.this);
        List<Address> list = null;
        try {
            list = gc.getFromLocation(latLng.latitude, latLng.longitude, 1); // current location
            // list = gc.getFromLocation(47.60621, -122.33207, 1); // Seattle
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            location = address.getLocality();
        }
        else {
            location = "";
            Toast.makeText(this, "Can't find your location.", Toast.LENGTH_SHORT).show();
        }
    }
}
