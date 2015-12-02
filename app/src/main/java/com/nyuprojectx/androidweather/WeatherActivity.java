package com.nyuprojectx.androidweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nyuprojectx.androidweather.data.Channel;
import com.nyuprojectx.androidweather.data.Item;
import com.nyuprojectx.androidweather.service.WeatherServiceCallback;
import com.nyuprojectx.androidweather.service.YahooWeatherService;
import com.nyuprojectx.androidweather.user.User;
import com.nyuprojectx.androidweather.user.UserLocalStore;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private ListView forecastListView;

    private YahooWeatherService service;
    private ProgressDialog dialog;

    public String location = "New York, NY";
    public static String DEFAULT_LOCATION = "New York, NY";
    private Button refreshWeatherButton;
    private Button mapActivityButton;
    private Button assistButton;

    SharedPreferences SP;
    UserLocalStore userLocalStore;

    public static final int REQUEST_CODE_SET_LOCATION = 0;
    public static final int REQUEST_CODE_USER_SETTINGS = 1;
    public static final int REQUEST_CODE_MAP = 2;
    public static final int REQUEST_CODE_SETTINGS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);

        service = new YahooWeatherService(this, getApplicationContext());
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        service.refreshWeather(location);

        userLocalStore = new UserLocalStore(this);
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        refreshWeatherButton = (Button)findViewById(R.id.refreshWeatherButton);
        refreshWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeatherActivity.this, "Refreshing " + location, Toast.LENGTH_SHORT).show();
                service.refreshWeather(location);
            }
        });

        mapActivityButton = (Button)findViewById(R.id.showMapButton);
        mapActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, MapsActivity.class);
                intent.putExtra("currentLocation", location);
                intent.putExtra("requestCode", REQUEST_CODE_MAP);
                startActivityForResult(intent, REQUEST_CODE_MAP);
            }
        });

        assistButton = (Button)findViewById(R.id.assistButton);
        assistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        forecastListView = (ListView)findViewById(R.id.forecastListView);
        String[] arr = {"Monday", "Tuesday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, arr);
        forecastListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        // return true;
        menu.add(0, 0, 0, "Set location");
        menu.add(0, 1, 0, "Log in");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //     return true;
        // }
        // return super.onOptionsItemSelected(item);

        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(WeatherActivity.this, UserSettingsActivity.class);
                intent.putExtra("REQUEST_CODE", REQUEST_CODE_SETTINGS);
                startActivityForResult(intent, REQUEST_CODE_SETTINGS);
                break;
            case 0:
                intent = new Intent(WeatherActivity.this, SetLocationActivity.class);
                intent.putExtra("CURRENT_LOCATION", location);
                intent.putExtra("REQUEST_CODE", REQUEST_CODE_SET_LOCATION);
                startActivityForResult(intent, REQUEST_CODE_SET_LOCATION);
                break;
            case 1:
                // intent = new Intent(WeatherActivity.this, LogInActivity.class);
                // startActivity(intent);
                if (userLocalStore.getLoggedInUser() == null) {
                    intent = new Intent(WeatherActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(WeatherActivity.this, UserInfoActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String newLocation = null;
        switch (requestCode) {
            case REQUEST_CODE_SETTINGS:
                String temp = SP.getString("location", "New York");
                boolean displayMode = SP.getBoolean("displayMode", true);
                if (displayMode) {
                    forecastListView.setVisibility(View.VISIBLE);
                } else {
                    forecastListView.setVisibility(View.INVISIBLE);
                }
                break;
            case REQUEST_CODE_SET_LOCATION:
                newLocation = data.getStringExtra("newLocation");
                if (newLocation == null || newLocation.equals("")) {
                    break;
                }
                else {
                    if (!newLocation.equals(location)) {
                        Toast.makeText(this, newLocation, Toast.LENGTH_SHORT).show();
                        location = newLocation;
                        service.refreshWeather(location);
                    }
                }
                break;
            case REQUEST_CODE_USER_SETTINGS:
                String temperatureDisplayMode = data.getStringExtra("temperatureDisplayMode");
                Boolean isShowCondition = data.getBooleanExtra("isShowCondition", true);
                Boolean isShowForecast = data.getBooleanExtra("isShowForecast", true);

                if (isShowCondition) {
                    conditionTextView.setVisibility(View.VISIBLE);
                } else {
                    conditionTextView.setVisibility(View.INVISIBLE);
                }

                if (isShowForecast) {
                    forecastListView.setVisibility(View.VISIBLE);
                } else {
                    forecastListView.setVisibility(View.INVISIBLE);
                }
                break;
            case REQUEST_CODE_MAP:
                newLocation = data.getStringExtra("newLocation");
                if (newLocation.equals(location)) {
                    break;
                }
                else {
                    if (newLocation != null && !newLocation.equals("")) {
                        Toast.makeText(this, newLocation, Toast.LENGTH_SHORT).show();
                        if (!newLocation.equals(location)) {
                            location = newLocation;
                            service.refreshWeather(location);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Item item = channel.getItem();

        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawble = getResources().getDrawable(resourceId);

        weatherIconImageView.setImageDrawable(weatherIconDrawble);
        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(channel.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
