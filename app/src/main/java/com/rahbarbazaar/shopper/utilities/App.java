package com.rahbarbazaar.shopper.utilities;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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

}
