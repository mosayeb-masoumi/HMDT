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
import android.widget.Toast
import com.example.panelist.R
import com.example.panelist.models.api_error.ErrorUtils
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
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : CustomBaseActivity() {

    private var connectivityReceiver: BroadcastReceiver? = null
    lateinit var disposable: CompositeDisposable


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
                requestLogin()

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
                        requestLogin()
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

    private fun requestLogin() {


       var mobile = edt_phone.text.toString()

        val service = ServiceProvider(this).getmService()
        val call = service.login(mobile)

        call.enqueue(object : Callback<LoginModel> {

            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.code() == 200) {

//                    startActivity(Intent(this@LoginActivity, VerificationActivity::class.java))
                    val intent = Intent(this@LoginActivity,VerificationActivity::class.java)
                    intent.putExtra("mobile",mobile)
                    startActivity(intent)


                    var data = response.body()?.data
                    Toast.makeText(context, "" + data, Toast.LENGTH_LONG).show()
                    avi_login.hide()
                    btn_submit_login.visibility = View.VISIBLE
                    finish()

                } else if (response.code() == 422) {

                    avi_login.hide()
                    btn_submit_login.visibility = View.VISIBLE

//                    val apiError = ErrorUtils.parseError(response)
//                    if (apiError.errors.mobile != null) {
//
//                        var builderMobile = StringBuilder()
//                        for (a in apiError.errors.mobile) {
//                            builderMobile.append("$a ")
//                        }
//                        Toast.makeText(context, "" + builderMobile, Toast.LENGTH_LONG).show()
//                    }

                }else{
                    Toast.makeText(context, "" + R.string.serverFaield, Toast.LENGTH_LONG).show()
                    btn_submit_login.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                Toast.makeText(context, "" + R.string.connectionFaield, Toast.LENGTH_LONG).show()
                btn_submit_login.visibility = View.VISIBLE
            }


        })


//        disposable.add(service.login(phone)
//                .observeOn(AndroidSchedulers.mainThread() )
//                .subscribeOn(Schedulers.io())
//                .subscribeWith(object  : DisposableSingleObserver<LoginModel>(){
//                    override fun onSuccess(result: LoginModel) {
//
//                       var a :String = result.data.toString()
//                        var b :String =a
//                    }
//
//                    override fun onError(e: Throwable) {
////                        var a = 5
//                        var a : Int =(e as HttpException).code()
//                        if(a==422){
//                            var b =a
//                        }
//                    }
//                }))
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
