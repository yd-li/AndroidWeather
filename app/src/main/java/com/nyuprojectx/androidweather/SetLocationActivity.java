package com.nyuprojectx.androidweather;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class SetLocationActivity extends AppCompatActivity {

    private int requestCode;
    private String currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        Intent intent = getIntent();
        requestCode = intent.getIntExtra("REQUEST_CODE", 0);
        currentLocation = intent.getStringExtra("CURRENT_LOCATION");

        TextView currentLocationTextView = (TextView)findViewById(R.id.currentLocationTextView);
        currentLocationTextView.setText("Current location: " + currentLocation);
        Button setLocationOKButton = (Button)findViewById(R.id.setLocationOKButton);
        setLocationOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newLocationEditText = (EditText) findViewById(R.id.newLocationEditText);
                String newLocation = newLocationEditText.getText().toString();

                if (newLocation == null || newLocation.equals("")) {
                    Toast.makeText(SetLocationActivity.this, "No input. Type a valid location again.", Toast.LENGTH_SHORT).show();
                } else {
                    //  input exists
                    Geocoder gc = new Geocoder(SetLocationActivity.this);

                    List<Address> list = null;

                    try {
                        list = gc.getFromLocationName(newLocation, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (list.size() > 0) {
                        Address address = list.get(0);
                        Intent intent = new Intent();
                        intent.putExtra("newLocation", address.getLocality());
                        setResult(requestCode, intent);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), "Location not found. Type a valid location again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button setLocationCancelButton = (Button) findViewById(R.id.setLocationCancelButton);
        setLocationCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("newLocation", currentLocation);
                setResult(requestCode, intent);
                finish();
            }
        });
    }
}
