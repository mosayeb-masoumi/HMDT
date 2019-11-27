package com.example.panelist.ui.activities

import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.panelist.R
import com.example.panelist.models.api_error403.ShowMessage403
import com.example.panelist.models.dashboard.DashboardModel
import com.example.panelist.network.ServiceProvider
import com.example.panelist.utilities.*
import kotlinx.android.synthetic.main.activity_splash1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SplashActivity1 : CustomBaseActivity() {

    private var connectivityReceiver: BroadcastReceiver? = null
//    private var gpsTracker: GpsTracker? = null
//    var lat: String = ""
//    var lng: String = ""


    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash1)

        context = this@SplashActivity1

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@SplashActivity1, findViewById<View>(R.id.rl_root))
            }

        }


        btn_reload.setOnClickListener {

            reload()
        }

    }



    private fun startAnim() {
        avi.show()
    }

    private fun startActivity() {
        val accessToken = Cache.getString("access_token")

        if (accessToken != "") {

            requestDashboardData()

        } else {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    LocaleManager.setNewLocale(this@SplashActivity1, "fa")
                    startActivity(Intent(this@SplashActivity1, LoginActivity::class.java))
                    finish()
                }
            }, 2700)
        }
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


    private fun requestDashboardData() {

        val service = ServiceProvider(this).getmService()
        val call = service.dashboardData
        call.enqueue(object : Callback<DashboardModel> {

            override fun onResponse(call: Call<DashboardModel>, response: Response<DashboardModel>) {

                if (response.code() == 200) {

                    var a: Boolean
                    a = response.body()?.data!!
                    startActivity(Intent(this@SplashActivity1, MainActivity::class.java))
                    finish()
                } else if (response.code() == 403) {

                    ShowMessage403.message(response, context)
                    hideLoading()

                } else if(response.code()==422) {
                    hideLoading()
                }else{
                    Toast.makeText(App.context, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<DashboardModel>, t: Throwable) {
                Toast.makeText(App.context, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                hideLoading()
            }

        })

    }

    private fun hideLoading() {
        avi.hide()
        btn_reload.visibility=View.VISIBLE
    }


    private fun reload() {
        avi.show()
        btn_reload.visibility=View.GONE

        startActivity(Intent(this@SplashActivity1,SplashActivity1::class.java))
    }
}
