package com.nyuprojectx.androidweather.service;

import com.nyuprojectx.androidweather.data.Channel;

/**
 * Created by liyuanda on 2015/9/13.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}