package com.rahbarbazaar.checkpanel.ui.activities

import android.content.*
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.format.Formatter
import android.view.View
import android.widget.Toast
import com.rahbarbazaar.checkpanel.BuildConfig
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.models.api_error.ErrorUtils
import com.rahbarbazaar.checkpanel.models.api_error403.ShowMessage403
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create.DashboardCreateData
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_history.DashboardHistory
import com.rahbarbazaar.checkpanel.models.shopping_memberprize.MemberPrize
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.*
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException
import java.util.*


class SplashActivity : CustomBaseActivity() {

    private var connectivityReceiver: BroadcastReceiver? = null

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        context = this@SplashActivity

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@SplashActivity, findViewById<View>(R.id.rl_root))
            }
        }

        txtVersion.setText(BuildConfig.VERSION_NAME)

        btn_reload.setOnClickListener {
            reload()
        }
    }

    private fun startAnim() {
        avi.show()
    }

    private fun startActivity() {
        var accessToken = Cache.getString(this@SplashActivity, "access_token")
        // add this condition to remove unwanted bug for clause
        if (accessToken == null) {
            accessToken = ""
        }


        if (accessToken != "") {
            requestDashboardData()

        } else {

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                    this@SplashActivity.finish()

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
        call.enqueue(object : Callback<DashboardCreateData> {

            override fun onResponse(call: Call<DashboardCreateData>, response: Response<DashboardCreateData>) {

                if (response.code() == 200) {

                    val dashboardCreateData: DashboardCreateData
                    dashboardCreateData = response.body()!!
//                    RxBus.publishDashboardData(dashboardModel)
                    RxBus.DashboardModel.publishDashboardModel(dashboardCreateData)
//                    ClientConfig.Update_URL = dashboardCreateData.data.updateUrl
                    Cache.setString(this@SplashActivity,"Update_URL",dashboardCreateData.data.updateUrl)
                    Cache.setString(this@SplashActivity,"minVersionCode",dashboardCreateData.data.minVersionCode)
                    Cache.setString(this@SplashActivity,"currentVersionCode",dashboardCreateData.data.currentVersionCode)

//                    requestRegisterData()


                    requestInitMemberPrizeLists()


                } else if (response.code() == 403) {

                    ShowMessage403.message(response, context)
                    hideLoading()

                } else if (response.code() == 406) {
                    val apiError = ErrorUtils.parseError406(response)
                    showError406Dialog(apiError.message)
                    hideLoading()
                } else {
                    Toast.makeText(this@SplashActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<DashboardCreateData>, t: Throwable) {
                Toast.makeText(this@SplashActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                hideLoading()
            }
        })
    }

    private fun requestInitMemberPrizeLists() {
        val service = ServiceProvider(this).getmService()
        val call = service.memberPrizeLists
        call.enqueue(object : Callback<MemberPrize> {

            override fun onResponse(call: Call<MemberPrize>, response: Response<MemberPrize>) {

                if (response.code() == 200) {

                    var memberPrize = MemberPrize()
                    memberPrize = response.body()!!
                    RxBus.MemberPrizeLists.publishMemberPrizeLists(memberPrize)
                    sendDeviceInfo()


                } else {
                    Toast.makeText(this@SplashActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<MemberPrize>, t: Throwable) {
                Toast.makeText(this@SplashActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                hideLoading()
            }
        })
    }


    private fun sendDeviceInfo() {

        val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        @Suppress("DEPRECATION")
        val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo

        val network_type:String?
        @Suppress("DEPRECATION")
        if(info.typeName == "MOBILE"){
           network_type = info.extraInfo
        }else{
             network_type = info.typeName
        }


        val sdk = Build.VERSION.SDK_INT
        val os_type = "Android"
        val os_version = sdk.toString()
        val device_brand = Build.BRAND
        val device_model = Build.MODEL
        val version_code = BuildConfig.VERSION_CODE.toString()
        val version_name = BuildConfig.VERSION_NAME

        val service = ServiceProvider(this).getmService()
        val call = service.sendDeviceInfo(device_brand,device_model,os_type,os_version,version_code,version_name,ip,network_type)
        call.enqueue(object : Callback<DashboardHistory> {

            override fun onResponse(call: Call<DashboardHistory>, response: Response<DashboardHistory>) {

                if (response.code() == 200) {

                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                    this@SplashActivity.finish()

                } else {
                    Toast.makeText(  this@SplashActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DashboardHistory>, t: Throwable) {
                Toast.makeText(  this@SplashActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showError406Dialog(message: String?) {

        val dialogFactory = DialogFactory(this)
        dialogFactory.createError406Dialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg strings: String?) {
                finish()
                System.exit(0)
            }

            override fun onDeniedButtonClicked(cancel_dialog: Boolean) {

            }

        }, splash_root, message)

    }


    private fun hideLoading() {
        avi.hide()
        btn_reload.visibility = View.VISIBLE
    }

    private fun reload() {
        avi.show()
        btn_reload.visibility = View.GONE
        startActivity(Intent(this@SplashActivity, SplashActivity::class.java))
    }
}
