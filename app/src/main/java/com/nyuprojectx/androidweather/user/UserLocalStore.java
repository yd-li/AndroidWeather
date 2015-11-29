package com.nyuprojectx.androidweather.user;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by YuandaLi on 11/27/15.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("email", user.email);
        userLocalDatabaseEditor.putString("username", user.username);
        userLocalDatabaseEditor.putString("password", user.password);
        userLocalDatabaseEditor.putInt("date", user.date);
        userLocalDatabaseEditor.putString("location", user.location);
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public User getLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }

        String email = userLocalDatabase.getString("email", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        int date = userLocalDatabase.getInt("date", -1);
        String location = userLocalDatabase.getString("location", "New York");

        User user = new User(email, date, username, password, location);
        return user;
    }
}
