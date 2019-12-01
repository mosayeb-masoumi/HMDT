package com.example.panelist.ui.activities

import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.panelist.R
import com.example.panelist.models.api_error.ErrorUtils
import com.example.panelist.models.login.LoginModel
import com.example.panelist.network.ServiceProvider
import com.example.panelist.utilities.App.context
import com.example.panelist.utilities.CustomBaseActivity
import com.example.panelist.utilities.GeneralTools
import io.reactivex.disposables.CompositeDisposable
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

        btn_submit_login.visibility = View.GONE
        avi_login.visibility = View.VISIBLE
        avi_login.show()
        requestLogin()

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
                    this@LoginActivity.finish()
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)


                    var data = response.body()?.data
                    Toast.makeText(context, "" + data, Toast.LENGTH_LONG).show()
//                    Toasty.success(context, ""+data, Toast.LENGTH_SHORT, true).show()

                    avi_login.hide()
                    btn_submit_login.visibility = View.VISIBLE
                    finish()

                } else if (response.code() == 422) {

                    avi_login.hide()
                    btn_submit_login.visibility = View.VISIBLE

                    val apiError = ErrorUtils.parseError422(response)
                    if (apiError.errors.mobile != null) {

                        var builderMobile = StringBuilder()
                        for (a in apiError.errors.mobile) {
                            builderMobile.append("$a ")
                        }
                        Toast.makeText(context, "" + builderMobile, Toast.LENGTH_SHORT).show()
                    }


                }else{
                    Toast.makeText(context, "" +resources.getString(R.string.serverFaield) , Toast.LENGTH_SHORT).show()
                    btn_submit_login.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                Toast.makeText(context, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                btn_submit_login.visibility = View.VISIBLE
            }


        })





//        disposable.add(service.login(mobile)
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
//
//                        var a : Int =(e as HttpException).code()
//
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
