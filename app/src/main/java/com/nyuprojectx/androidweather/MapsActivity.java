package com.nyuprojectx.androidweather;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nyuprojectx.androidweather.service.GpsService;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private GpsService gps;
    private Geocoder gc;
    private int requestCode;
    private String currentLocation;
    private LatLng currentLatLng;

    private EditText newLocationTextEdit;
    private Button mapSearchButton;
    private Button mapSetLocationButton;
    private Button mapCancelButton;
    private String newLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        gps = new GpsService(MapsActivity.this);
        gc = new Geocoder(MapsActivity.this);
        Intent intent = getIntent();
        requestCode = intent.getIntExtra("requestCode", 2);
        currentLocation = intent.getStringExtra("currentLocation");
        newLocation = null;

        final Intent resultIntent = new Intent();
        intent.putExtra("newLocation", newLocation);
        setResult(requestCode, resultIntent);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        newLocationTextEdit = (EditText) findViewById(R.id.mapInputNewLocation);
        //  Search button and set location button
        mapSearchButton = (Button)findViewById(R.id.mapSearchButton);
        mapSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);

                String inputNewLocation = newLocationTextEdit.getText().toString();
                if (inputNewLocation == null || inputNewLocation.equals("")) {
                    Toast.makeText(MapsActivity.this, "No input location.", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Address> list = null;

                try {
                    list = gc.getFromLocationName(inputNewLocation, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (list.size() > 0) {
                    Address address = list.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(inputNewLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    newLocation = address.getLocality();
                    newLocationTextEdit.setText(newLocation);
                }
            }
        });
        mapSetLocationButton = (Button)findViewById(R.id.mapSetLocationButton);
        mapSetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputNewLocation = newLocationTextEdit.getText().toString();
                if (inputNewLocation == null || inputNewLocation.equals("")) {
                    resultIntent.putExtra("newLocation", currentLocation);
                }
                else {
                    if (!inputNewLocation.equals(newLocation)) {
                        List<Address> list = null;

                        try {
                            list = gc.getFromLocationName(inputNewLocation, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (list.size() > 0) {
                            Address address = list.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng).title(inputNewLocation));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            newLocation = address.getLocality();
                        }
                    }
                    resultIntent.putExtra("newLocation", newLocation);
                }
                finish();
            }
        });
        mapCancelButton = (Button)findViewById(R.id.mapCancelButton);
        mapCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultIntent.putExtra("newLocation", currentLocation);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Menu1");
        return true;
        // return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng ny = new LatLng(40.714353, -74.005973);
        // mMap.setMyLocationEnabled(true);
        // mMap.addMarker(new MarkerOptions().position(ny).title("Marker in New York"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        // Original code ends here.

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        //  Set default location
        if (gps.canGetLocation()) {
            LatLng currentLoc = new LatLng(gps.getLatitude(), gps.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLoc).title("You are here."));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLoc));
        }
        else {
            //  GPS or network unavailable
            LatLng seattle = new LatLng(47.60621, -122.33207);
            mMap.addMarker(new MarkerOptions().position(seattle).title("Default location."));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(seattle));
        }

        // Add marker for user Set Location
        List<Address> list = null;

        try {
            list = gc.getFromLocationName(currentLocation, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("User set location."));
        }
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        List<Address> list = null;
        try {
            list = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            String result = address.getLocality();
            newLocationTextEdit.setText(result);
            mMap.addMarker(new MarkerOptions().position(latLng).title(result));
        }
        else {
            Toast.makeText(this, "No result found here.", Toast.LENGTH_SHORT).show();
        }
    }
}
