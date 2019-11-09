package com.example.panelist.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.panelist.R;
import com.example.panelist.utilities.Cache;
import com.example.panelist.utilities.CustomBaseActivity;
import com.example.panelist.utilities.GeneralTools;
import com.example.panelist.utilities.GpsTracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends CustomBaseActivity {

    BroadcastReceiver connectivityReceiver = null;
    private GpsTracker gpsTracker;
    String strLat, strLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //check network broadcast reciever
        GeneralTools tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tools.doCheckNetwork(SplashActivity.this, findViewById(R.id.rl_root));
            }

        };


     startActivity();

    }


    private void startActivity(){
//        String email = Cache.getString("email");
//        String password = Cache.getString("password");
        String email = "email";
        String password ="password";

        if(!email.equals("") && !password.equals("")){

            if(checkGpsON()){
                requestLoginData();
                int a = 5 ;
            }else{
                displayLocationSettingsRequest(this,125);
            }



        }else{
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
//                    gotoMainActivity();
                    int b = 6 ;
                }
            }, 2700);
        }
    }




    public void getLocation() {
        gpsTracker = new GpsTracker(SplashActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            strLat = (String.valueOf(latitude));
            strLng = (String.valueOf(longitude));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    public boolean checkGpsON() {
        final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }

    // turn on gps as google
    private void displayLocationSettingsRequest(Context context, int requestCode) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
                try {
                    status.startResolutionForResult((Activity) context, requestCode);

                } catch (IntentSender.SendIntentException ignored) {
                }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==125){



            Handler handler = new Handler();
            handler.postDelayed(() -> {
//                presenter.activityLoaded();

                startActivity();

            }, 2700);
            startActivity();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestLoginData() {
        int a = 5 ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(connectivityReceiver);
        super.onDestroy();
    }
}
