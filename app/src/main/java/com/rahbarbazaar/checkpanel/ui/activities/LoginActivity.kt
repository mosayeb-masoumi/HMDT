package com.rahbarbazaar.checkpanel.ui.activities

import android.app.Activity
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.models.api_error.ErrorUtils
import com.rahbarbazaar.checkpanel.models.login.LoginModel
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbazaar.checkpanel.utilities.GeneralTools
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginActivity : CustomBaseActivity() {

    private var connectivityReceiver: BroadcastReceiver? = null

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

        btn_submit_login.setOnClickListener {submitRequest()}

        // event on done keyboard
        edt_phone.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitRequest()
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

    private fun submitRequest() {

        btn_submit_login.visibility = View.GONE
        avi_login.visibility = View.VISIBLE
        avi_login.show()
        requestLogin()

    }


    private fun requestLogin() {

       val mobile = edt_phone.text.toString()
        val service = ServiceProvider(this).getmService()
        val call = service.login(mobile)

        call.enqueue(object : Callback<LoginModel> {

            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.code() == 200) {
                    val intent = Intent(this@LoginActivity,VerificationActivity::class.java)
                    intent.putExtra("mobile",mobile)
                    startActivity(intent)
                    this@LoginActivity.finish()
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)


                    var data = response.body()?.data
                    Toast.makeText(this@LoginActivity, "" + data, Toast.LENGTH_LONG).show()
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
                        Toast.makeText(this@LoginActivity, "" + builderMobile, Toast.LENGTH_SHORT).show()
                    }


                }else{
                    Toast.makeText(this@LoginActivity, "" +resources.getString(R.string.serverFaield) , Toast.LENGTH_SHORT).show()
                    btn_submit_login.visibility = View.VISIBLE
                    avi_login.hide()
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "" + resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                avi_login.hide()
                btn_submit_login.visibility = View.VISIBLE
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
