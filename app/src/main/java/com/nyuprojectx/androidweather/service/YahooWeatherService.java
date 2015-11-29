package com.nyuprojectx.androidweather.service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nyuprojectx.androidweather.WeatherActivity;
import com.nyuprojectx.androidweather.data.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liyuanda on 2015/9/13.
 */
public class YahooWeatherService {
    private WeatherServiceCallback callback;
    private Context context;
    private Exception error;

    private final String CACHED_WEATHER_FILE = "weather.data";

    public YahooWeatherService(WeatherServiceCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
    }

    public void refreshWeather(String location) {

        new AsyncTask<String, Void, Channel>() {
            @Override
            protected Channel doInBackground(String... strings) {

                String location = strings[0];

                Channel channel = loadCache(location);

                if (channel != null) {
                    return channel;
                } else {
                    channel = new Channel();
                }

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='c'", location);

                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject data = new JSONObject(result.toString());

                    JSONObject queryResults = data.optJSONObject("query");

                    int count = queryResults.optInt("count");

                    if (count == 0) {
                        error = new LocationWeatherException("No weather information found for " + location);
                        return null;
                    }

                    JSONObject channelJSON = queryResults.optJSONObject("results").optJSONObject("channel");

                    loadMetadata(location, channelJSON);

                    channel.populate(channelJSON);

                    cacheWeatherData(channel);

                    return channel;

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Channel channel) {

                if (channel == null && error != null) {
                    callback.serviceFailure(error);
                } else {
                    callback.serviceSuccess(channel);
                }

            }

        }.execute(location);
    }

    private void loadMetadata(String location, JSONObject channelJSON) throws ParseException, JSONException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm aa z", Locale.US);

        Date lastBuildDate = sdf.parse(channelJSON.optString("lastBuildDate"));

        long ttl = channelJSON.optLong("ttl");

        long expiration = ttl * 60 * 1000 + lastBuildDate.getTime();

        channelJSON.put("expiration", expiration);
        channelJSON.put("requestLocation", location);
    }

    private void cacheWeatherData(Channel channel) {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(CACHED_WEATHER_FILE, Context.MODE_PRIVATE);
            outputStream.write(channel.toJSON().toString().getBytes());
            outputStream.close();

        } catch (IOException e) {
            // IGNORE: file save operation failed
        }
    }

    private Channel loadCache(String location) {
        try {
            FileInputStream inputStream = context.openFileInput(CACHED_WEATHER_FILE);

            StringBuilder cache = new StringBuilder();
            int content;
            while ((content = inputStream.read()) != -1) {
                cache.append((char) content);
            }

            inputStream.close();

            JSONObject jsonCache = new JSONObject(cache.toString());

            Channel channel = new Channel();
            channel.populate(jsonCache);

            long now = (new Date()).getTime();

            if (channel.getExpiration() > now && channel.getLocation().equalsIgnoreCase(location)) {
                return channel;
            }

        } catch (Exception e) {
            context.deleteFile(CACHED_WEATHER_FILE);
        }

        return null;
    }

    public class LocationWeatherException extends Exception {
        public LocationWeatherException(String detailMessage) {
            super(detailMessage);
        }
    }
}