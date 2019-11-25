package com.example.panelist.ui.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.panelist.R
import com.example.panelist.models.api_error.ErrorUtils
import com.example.panelist.models.login.LoginModel
import com.example.panelist.models.verify.VerifyModel
import com.example.panelist.network.Service
import com.example.panelist.network.ServiceProvider
import com.example.panelist.utilities.App
import com.example.panelist.utilities.Cache
import com.example.panelist.utilities.CustomBaseActivity
import com.example.panelist.utilities.GeneralTools
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_verification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.absoluteValue

class VerificationActivity : CustomBaseActivity(), View.OnClickListener {


    private var connectivityReceiver: BroadcastReceiver? = null
    lateinit var disposable: CompositeDisposable

    var mobile: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@VerificationActivity, findViewById<View>(R.id.rl_root))
            }

        }
        disposable = CompositeDisposable()



        mobile = intent.getStringExtra("mobile")
        text_user_mobile.text = mobile


        reverseTimer(15, text_min)

        ll_txt_user_mobile.setOnClickListener(this)
        linear_recode.setOnClickListener(this)
        button_verify.setOnClickListener(this)
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
                finish()
            }

            R.id.linear_recode -> {
                linear_recode.visibility = View.GONE
                rl_recode_number.visibility = View.VISIBLE
                reverseTimer(10, text_min)

                recodeRequest()
            }

            R.id.button_verify -> {
                sendVerifyRequest()
            }
        }

    }

    private fun sendVerifyRequest() {

        var verifyCode = et_user_verify.text.toString().trim()
        val service = ServiceProvider(this).getmService()
        val call = service.verify(verifyCode)
        call.enqueue(object : Callback<VerifyModel> {

            override fun onResponse(call: Call<VerifyModel>, response: Response<VerifyModel>) {

                if(response.code()==200){

                    var expireAt:Int

                    var accessToken = response.body()?.accessToken
                    var refreshToken = response.body()?.refreshToken
                    expireAt = response.body()?.expireAt!!

                    Cache.setString("accessToken",accessToken)
                    Cache.setString("refreshToken",refreshToken)
                    Cache.setInt("expireAt",expireAt)

//                    startActivity(Intent(this@VerificationActivity,MainActivity::class.java))
//                    finish()


                }else if(response.code() ==422){
                    val apiError = ErrorUtils.parseError(response)
                    if (apiError.errors.code != null) {

                        var builderMobile = StringBuilder()
                        for (a in apiError.errors.code) {
                            builderMobile.append("$a ")
                        }
                        Toast.makeText(App.context, "" + builderMobile, Toast.LENGTH_LONG).show()
                    }

                }else{
                    Toast.makeText(App.context, "" + R.string.serverFaield, Toast.LENGTH_LONG).show()
                }



            }

            override fun onFailure(call: Call<VerifyModel>, t: Throwable) {
                Toast.makeText(App.context, "" + R.string.connectionFaield, Toast.LENGTH_LONG).show()
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
                    Toast.makeText(App.context, "" + data, Toast.LENGTH_LONG).show()
                } else if (response.code() == 422) {

//                    val apiError = ErrorUtils.parseError(response)
//                    if (apiError.errors.mobile != null) {
//
//                        var builderMobile = StringBuilder()
//                        for (a in apiError.errors.mobile) {
//                            builderMobile.append("$a ")
//                        }
//                        Toast.makeText(App.context, "" + R.string.serverFaield, Toast.LENGTH_LONG).show()
//                    }
                } else {

                }

            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {

                Toast.makeText(App.context, "" + R.string.connectionFaield, Toast.LENGTH_LONG).show()
            }


        })
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
