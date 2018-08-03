package com.kisscigarette.app.common;

import android.app.Application;


/**
 * Created by jxd on 2017/12/15.
 */

public class MyApplication extends Application {

    private static Application instance;


    @Override
    public void onCreate() {
        super.onCreate();

        MyApplication.instance = this;


    }

    public static Application getInstance() {
        if (instance != null) {
            return instance;

        } else {
            throw new RuntimeException("u should init instance first");
        }
    }



}
