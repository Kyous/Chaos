package com.chaos.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by mac on 10/25/15.
 */
public class ChaosApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        ChaosApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ChaosApplication.context;
    }

    public static String serverURL() {
        return "http://www.kyous.co.za";

    }
}
