package com.rahbarbazaar.homadit.android.utilities;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;

public class App extends Application {


//    private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        context = this;



//        if (LeakCanary.isInAnalyzerProcess(this)) {
//           return;
//        }
//        refWatcher = LeakCanary.install(this);
    }


//    public static RefWatcher getRefWatcher(Context context) {
//        App application = (App) context.getApplicationContext();
//        return application.refWatcher;
//    }


    // TO support android below 21
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
