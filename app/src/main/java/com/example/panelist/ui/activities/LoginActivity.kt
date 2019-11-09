package com.example.panelist.ui.activities

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.panelist.R
import com.example.panelist.models.login.LoginModel
import com.example.panelist.network.ServiceProvider
import com.example.panelist.utilities.App.context
import com.example.panelist.utilities.CustomBaseActivity
import com.example.panelist.utilities.GeneralTools
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : CustomBaseActivity() {

    private var connectivityReceiver: BroadcastReceiver? = null
    lateinit var disposable: CompositeDisposable

    var phone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@LoginActivity, findViewById<View>(R.id.rl_root))
            }

        }


        disposable = CompositeDisposable()
        btn_submit_login.setOnClickListener {
            submitRequest()
        }

    }

    private fun submitRequest() {

        if (checkGpsPermission()) {
            if (checkGpsOn()) {
                btn_submit_login.visibility = View.GONE
                avi_login.visibility = View.VISIBLE
                avi_login.show()
                phone = edt_phone.text.toString()
                requestLogin(phone)

            } else {
                displayLocationSettingsRequest(context, 123)
            }
        } else {

            getGpsPermission()
        }

    }

    private fun getGpsPermission() {
        ActivityCompat.requestPermissions(this@LoginActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), 3)
    }


    private fun checkGpsOn(): Boolean {

        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }

    private fun checkGpsPermission(): Boolean {

        return !(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

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
                    status.startResolutionForResult(this@LoginActivity, requestCode)

                } catch (ignored: IntentSender.SendIntentException) {
                }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            3 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkGpsOn()) {
                        requestLogin(phone)
                    } else {
                        displayLocationSettingsRequest(context, 123)
                    }
                } else {
                    submitRequest()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 123) {
            submitRequest()
        } else {
            submitRequest()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun requestLogin(phone: String) {

        val service = ServiceProvider(this).getmService()
        disposable.add(service.login(phone)
                .observeOn(AndroidSchedulers.mainThread() )
                .subscribeOn(Schedulers.io())
                .subscribeWith(object  : DisposableSingleObserver<LoginModel>(){
                    override fun onSuccess(result: LoginModel) {
                       var a :String = result.data.toString()
                        var b :String =a
                    }

                    override fun onError(e: Throwable) {
                        var b :String =""
                    }


                }))
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }
}
