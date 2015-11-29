package com.nyuprojectx.androidweather.user;

/**
 * Created by YuandaLi on 11/27/15.
 */
public interface GetUserCallback {

    // Invoked when background task is completed
    public abstract void done(User returnedUser);
}
