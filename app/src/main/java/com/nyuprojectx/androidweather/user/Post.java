package com.nyuprojectx.androidweather.user;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by YuandaLi on 11/29/15.
 */
public class Post {

    public int uid;
    public LatLng latLng;
    public String status;
    public int mood;

    public Post(int uid, LatLng latLng, String status, int mood) {
        this.uid = uid;
        this.latLng = latLng;
        this.status = status;
        this.mood = mood;
    }
}
