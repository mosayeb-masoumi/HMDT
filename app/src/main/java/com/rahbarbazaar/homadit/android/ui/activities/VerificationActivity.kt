package com.rahbarbazaar.homadit.android.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.Formatter
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast

//import com.rahbarbazaar.shopper.BuildConfig
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.models.api_error.ErrorUtils
import com.rahbarbazaar.homadit.android.models.dashboard.dashboard_create.DashboardCreateData
import com.rahbarbazaar.homadit.android.models.dashboard.dashboard_history.DashboardHistory
import com.rahbarbazaar.homadit.android.models.login.LoginModel
import com.rahbarbazaar.homadit.android.models.shopping_memberprize.MemberPrize
import com.rahbarbazaar.homadit.android.models.verify.VerifyModel
import com.rahbarbazaar.homadit.android.network.ServiceProvider
import com.rahbarbazaar.homadit.android.utilities.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.activity_verification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.pushpole.sdk.PushPole
import com.rahbarbazaar.homadit.android.BuildConfig
import com.rahbarbazaar.homadit.android.R
import com.rahbarbazaar.homadit.android.models.Lottary.LottaryModel

import com.rahbarbazaar.homadit.android.models.history.HistoryData
import com.rahbarbazaar.homadit.android.models.transaction.TransactionData

class VerificationActivity : CustomBaseActivity(), View.OnClickListener {

    private var connectivityReceiver: BroadcastReceiver? = null
    var mobile: String = ""
    var pid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)


        PushPole.initialize(this, true)
//
        pid = PushPole.getId(this)


        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@VerificationActivity, findViewById<View>(R.id.rl_root))
            }
        }

        mobile = intent.getStringExtra("mobile")
        text_user_mobile.text = mobile
        reverseTimer(90, text_min)
        ll_txt_user_mobile.setOnClickListener(this)
        linear_recode.setOnClickListener(this)
        button_verify.setOnClickListener(this)

        val tf = Typeface.createFromAsset(assets, "BYekan.ttf")
        et_user_verify.typeface = tf
        text_min.typeface = tf
        text_sec.typeface = tf
        text_user_mobile.typeface = tf

        // event on done keyboard
        et_user_verify.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendVerifyRequest()
                closeKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun closeKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
                (currentFocus).windowToken, 0)
    }

    private fun reverseTimer(Seconds: Int, text_min: TextView?) {
        object : CountDownTimer((Seconds * 1000 + 1000).toLong(), 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000).toInt()

                val hours = seconds / (60 * 60)
                val tempMint = seconds - hours * 60 * 60
                val minutes = tempMint / 60
                seconds = tempMint - minutes * 60
                text_min?.text = String.format("%02d", minutes) + ":" + String.format("%02d", seconds)
            }

            override fun onFinish() {
                linear_recode.isEnabled = true
                linear_recode.visibility = View.VISIBLE
                rl_recode_number.visibility = View.GONE
            }
        }.start()
    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.ll_txt_user_mobile -> {
                startActivity(Intent(this@VerificationActivity, LoginActivity::class.java))
                this@VerificationActivity.finish()
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            }

            R.id.linear_recode -> {
                linear_recode.visibility = View.GONE
                rl_recode_number.visibility = View.VISIBLE
                reverseTimer(90, text_min)
                recodeRequest()
            }

            R.id.button_verify -> {
                sendVerifyRequest()
            }
        }
    }

    private fun sendVerifyRequest() {

        ll_av_verify.visibility = View.VISIBLE
        button_verify.visibility = View.GONE

        var verifyCode = et_user_verify.text.toString().trim()
        val service = ServiceProvider(this).getmService()
        val call = service.verify(verifyCode ,pid)
        call.enqueue(object : Callback<VerifyModel> {

            override fun onResponse(call: Call<VerifyModel>, response: Response<VerifyModel>) {

                if (response.code() == 200) {
                    val expireAt: Int
                    val access_token = response.body()?.accessToken
                    val refresh_token = response.body()?.refreshToken
                    expireAt = response.body()?.expireAt!!
                    Cache.setString(this@VerificationActivity, "access_token", access_token)
                    Cache.setString(this@VerificationActivity, "refresh_token", refresh_token)
                    Cache.setInt(this@VerificationActivity, "expireAt", expireAt)

                    requestDashboardData()

                } else if (response.code() == 422) {
                    val apiError = ErrorUtils.parseError422(response)
                    if (apiError.errors.code != null) {

                        ll_av_verify.visibility = View.GONE
                        button_verify.visibility = View.VISIBLE

                        var builderMobile = StringBuilder()
                        for (a in apiError.errors.code) {
                            builderMobile.append("$a ")
                        }
                        Toast.makeText(this@VerificationActivity, "" + builderMobile, Toast.LENGTH_LONG).show()
                    }

                } else if (response.code() == 406) {
                    val apiError = ErrorUtils.parseError406(response)
                    showError406Dialog(apiError.message)
                    ll_av_verify.visibility = View.GONE
                    button_verify.visibility = View.VISIBLE
                } else {
                    ll_av_verify.visibility = View.GONE
                    button_verify.visibility = View.VISIBLE
                    Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<VerifyModel>, t: Throwable) {
                ll_av_verify.visibility = View.GONE
                button_verify.visibility = View.VISIBLE
                Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
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

    private fun requestDashboardData() {
        val service = ServiceProvider(this).getmService()
        val call = service.dashboardData
        call.enqueue(object : Callback<DashboardCreateData> {

            override fun onResponse(call: Call<DashboardCreateData>, response: Response<DashboardCreateData>) {

                if (response.code() == 200) {
                    var dashboardCreateData: DashboardCreateData
                    dashboardCreateData = response.body()!!
                    RxBus.DashboardModel.publishDashboardModel(dashboardCreateData)
//                    ClientConfig.Update_URL = dashboardCreateData.data.updateUrl
                    Cache.setString(this@VerificationActivity,"Update_URL",dashboardCreateData.data.updateUrl)
                    Cache.setString(this@VerificationActivity,"minVersionCode",dashboardCreateData.data.minVersionCode)
                    Cache.setString(this@VerificationActivity,"currentVersionCode",dashboardCreateData.data.currentVersionCode)

                    requestInitMemberPrizeLists()

                } else {
                    ll_av_verify.visibility = View.GONE
                    button_verify.visibility = View.VISIBLE
                    Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DashboardCreateData>, t: Throwable) {
                ll_av_verify.visibility = View.GONE
                button_verify.visibility = View.VISIBLE
                Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
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

                    getHistoryList0()
                    getTransactionAmountList0()
                    sendDeviceInfo()

                } else {
                    Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MemberPrize>, t: Throwable) {
                Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getHistoryList0(){
        val service = ServiceProvider(this).getmService()
        val call = service.getHistoryList(0)
        call.enqueue(object : Callback<HistoryData>{

            override fun onResponse(call: Call<HistoryData>, response: Response<HistoryData>) {

                if(response.code()==200){
                    var historyData = HistoryData()
                    historyData = response.body()!!
                    RxBus.HistoryList0.publishHistoryList0(historyData);

                }else if(response.code()==204){
                    // send zero item
                    var historyData = HistoryData()
                    RxBus.HistoryList0.publishHistoryList0(historyData)

                }else{
                    Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<HistoryData>, t: Throwable) {
                Toast.makeText(  this@VerificationActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getTransactionAmountList0() {
        val service = ServiceProvider(this).getmService()
        val call = service.getTransactionList(0, "credit")
        call.enqueue(object : Callback<TransactionData> {

            override fun onResponse(call: Call<TransactionData>, response: Response<TransactionData>) {
                if (response.code() == 200) {

                    var transactionAmountList0 = TransactionData()
                    transactionAmountList0 = response.body()!!
                    RxBus.TransactionAmountList0.publishTransactionAmountList0(transactionAmountList0)

                } else if (response.code() == 204) {
                    // send zero item
                    var transactionAmountList0 = TransactionData()
                    RxBus.TransactionAmountList0.publishTransactionAmountList0(transactionAmountList0)
                }else{
                    Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TransactionData>, t: Throwable) {
                Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
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
//                    startActivity(Intent(this@VerificationActivity, AgreementActivity::class.java))
//                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
//                    finish()
                    getLottary()
                } else {
                    Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DashboardHistory>, t: Throwable) {
                Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getLottary(){

        val service = ServiceProvider(this).getmService()
        val call = service.lottaryMain
        call.enqueue(object : Callback<LottaryModel> {

            override fun onResponse(call: Call<LottaryModel>, response: Response<LottaryModel>) {

                if (response.code() == 200) {

                    var lottary = LottaryModel()
                    lottary = response.body()!!

                    RxBus.Lottary.publishLottary(lottary)

                    Cache.setString(this@VerificationActivity, "maximum", null)
                    Cache.setString(this@VerificationActivity, "current", null)


                    if (lottary.data.activeMe.data.size > 0) {
                        Cache.setString(this@VerificationActivity, "takepart", "yes")
                    }else{
                        Cache.setString(this@VerificationActivity, "takepart", "no")
                    }

                    if (lottary.data.activeMe.data.size > 0) {
                        Cache.setString(this@VerificationActivity, "amount", lottary.data.activeMe.data[0].amount)
                    }else{
                        Cache.setString(this@VerificationActivity, "amount", "0")
                    }

                    startActivity(Intent(this@VerificationActivity, AgreementActivity::class.java))
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                    this@VerificationActivity.finish()



                } else {
                    Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LottaryModel>, t: Throwable) {
                Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun recodeRequest() {

        val service = ServiceProvider(this).getmService()
        val call = service.login(mobile)
        call.enqueue(object : Callback<LoginModel> {

            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.code() == 200) {
                    var data = response.body()?.data
                    Toast.makeText(this@VerificationActivity, "" + data, Toast.LENGTH_LONG).show()
                } else if (response.code() == 422) {

                    val apiError = ErrorUtils.parseError422(response)
                    if (apiError.errors.mobile != null) {

                        var builderMobile = StringBuilder()
                        for (a in apiError.errors.mobile) {
                            builderMobile.append("$a ")
                        }
                        Toast.makeText(this@VerificationActivity, "" + builderMobile, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                Toast.makeText(this@VerificationActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
    }
}
