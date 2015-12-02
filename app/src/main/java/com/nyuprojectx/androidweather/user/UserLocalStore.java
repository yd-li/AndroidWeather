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
        userLocalDatabaseEditor.putInt("uid", user.uid);
        userLocalDatabaseEditor.putString("uname", user.uname);
        userLocalDatabaseEditor.putString("email", user.email);
        userLocalDatabaseEditor.putString("passwd", user.passwd);
        userLocalDatabaseEditor.putString("bio", user.bio);
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

        int uid = userLocalDatabase.getInt("uid", -1);
        String uname = userLocalDatabase.getString("uname", "");
        String email = userLocalDatabase.getString("email", "");
        String passwd = userLocalDatabase.getString("password", "");
        String bio = userLocalDatabase.getString("bio", "");

        User user = new User(uid, uname, email, passwd, bio);
        return user;
    }
}
