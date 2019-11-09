package com.example.panelist.ui.activities

import android.app.Activity
import android.content.*
import android.location.LocationManager
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.panelist.R
import com.example.panelist.utilities.CustomBaseActivity
import com.example.panelist.utilities.GeneralTools
import com.example.panelist.utilities.GpsTracker
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import kotlinx.android.synthetic.main.activity_splash1.*
import java.util.*

class SplashActivity1 : CustomBaseActivity() {

    private var connectivityReceiver: BroadcastReceiver? = null
    private var gpsTracker: GpsTracker? = null
    var lat:String = ""
    var lng:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash1)

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@SplashActivity1, findViewById<View>(R.id.rl_root))
            }

        }



    }

    private fun startAnim() {
        avi.show()
    }

    private fun startActivity() {
        val token = ""

        if (token != "") {
            if (checkGpsON()) {
                requestLoginData()

            } else {
                displayLocationSettingsRequest(this, 125)
            }


        } else {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                startActivity(Intent(this@SplashActivity1,LanguageActivity::class.java))
                 finish()
                }
            }, 2700)
        }
    }



    fun checkGpsON(): Boolean {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // turn on gps as google
    private fun displayLocationSettingsRequest(context: Context, requestCode: Int) {
        val googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result1 ->
            val status = result1.status
            if (status.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
                try {
                    status.startResolutionForResult(context as Activity, requestCode)

                } catch (ignored: IntentSender.SendIntentException) {
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 125) {

            val handler = Handler()
            handler.postDelayed({
                startActivity()

            }, 2700)
            startActivity()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        startAnim()
        startActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }


    private fun requestLoginData() {
         var a = 5;
        avi.hide()
    }
}
