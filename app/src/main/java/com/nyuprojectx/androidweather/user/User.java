package com.nyuprojectx.androidweather.user;

/**
 * Created by YuandaLi on 11/27/15.
 */
public class User {

    public int uid;
    public String uname; public String email;
    // int signdate;
    public String passwd, bio;

    public User(int uid, String uname, String email, String passwd, String bio) {
        this.uid = uid;
        this.uname = uname;
        this.email = email;
        this.passwd = passwd;
        this.bio = bio;
    }

    public User(String uname, String passwd) {
        this(-1, uname, "", passwd, "");
    }
}
