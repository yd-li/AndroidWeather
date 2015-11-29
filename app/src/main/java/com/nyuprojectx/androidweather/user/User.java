package com.nyuprojectx.androidweather.user;

/**
 * Created by YuandaLi on 11/27/15.
 */
public class User {

    public String email, username, password, location;
    public int date;

    public User(String email, int date, String username, String password, String location) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.date = date;
        this.location = location;
    }

    public User(String username, String password) {
        this("", -1, username, password, "New York");
    }
}
