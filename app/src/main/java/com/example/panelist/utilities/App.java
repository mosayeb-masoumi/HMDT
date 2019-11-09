package com.example.panelist.utilities;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class App extends Application {

    public static Context context;
    public static String ServerURL = "https://test.rahbarbazar.com/cp/api/v1/";

//    Login/Login


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = this;
    }
}
