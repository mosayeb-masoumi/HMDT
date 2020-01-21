package com.rahbarbazaar.shopper.ui.activities

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.shopper.models.api_error.ErrorUtils
import com.rahbarbazaar.shopper.models.barcodlist.Barcode
import com.rahbarbazaar.shopper.models.shopping_memberprize.MemberPrize
import com.rahbarbazaar.shopper.network.ServiceProvider
import com.rahbarbazaar.shopper.utilities.Cache
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity
import com.rahbarbazaar.shopper.utilities.GeneralTools
import com.rahbarbazaar.shopper.utilities.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_qrcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QRcodeActivity : CustomBaseActivity(), View.OnClickListener {

    private var connectivityReceiver: BroadcastReceiver? = null
    lateinit var initMemberPrizeLists: MemberPrize
    var disposable: Disposable = CompositeDisposable()

    companion object {
        lateinit var ResultScan: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@QRcodeActivity, findViewById<View>(R.id.rl_root))
            }
        }

        initMemberPrizeLists = MemberPrize()
        disposable = RxBus.MemberPrizeLists.subscribeMemberPrizeLists(){ result ->
            if (result is MemberPrize) {
                initMemberPrizeLists = result
            }
        }

        btn_choose_scanner.setOnClickListener(this)
        btn_choose_search.setOnClickListener(this)
        btn_register_barcode.setOnClickListener(this)
        rl_home_qrcode.setOnClickListener(this)
        linear_return_qrcode.setOnClickListener(this)

        // state can be null
        val state:String? = intent.getStringExtra("static_barcode")
        if(state=="static_barcode"){
            edt_barcode.setText("")
        }else  {
            edt_barcode?.setText(ResultScan)
        }

        txt_list_registable.text= initMemberPrizeLists.data.categories


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)




        edt_barcode

        // event on done keyboard
        edt_barcode.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getListOfProducts()
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


    override fun onClick(view: View) {
        when (view.id) {

            R.id.btn_choose_scanner -> {

                if (checkCameraPermission()) {
                    startActivity(Intent(this@QRcodeActivity, ScanActivity::class.java))
                    finish()
                } else {
                    requestCameraPermission()
                }
            }

            R.id.btn_choose_search -> {

            }

            R.id.btn_register_barcode -> {
                getListOfProducts()
            }

            R.id.rl_home_qrcode -> {
               startActivity(Intent(this@QRcodeActivity , MainActivity::class.java))
                finish()
            }

            R.id.linear_return_qrcode -> {
                finish()
            }

        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this@QRcodeActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this@QRcodeActivity, arrayOf(Manifest.permission.CAMERA), 33)
    }

    private fun getListOfProducts() {

        showAvi()

        val barcode = edt_barcode.text.toString()
        val service = ServiceProvider(this).getmService()
        val call = service.getBarcodeList(barcode)
        call.enqueue(object : Callback<Barcode> {
            override fun onResponse(call: Call<Barcode>, response: Response<Barcode>) {
              if(response.code()==200){

                  var barcode = Barcode()
                  barcode = response.body()!!

                  RxBus.BarcodeList.publishBarcodeList(barcode)
//                  startActivity(Intent(this@QRcodeActivity, BarcodeListActivity::class.java))
                  val intent = Intent(this@QRcodeActivity,BarcodeListActivity::class.java)
                  intent.putExtra("barcode",edt_barcode.text.toString())
                  startActivity(intent)
                  overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

                  overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                  showbtn()
                  finish()

              }else if(response.code()==422){
                  showbtn()
                  val apiError = ErrorUtils.parseError422(response)
                  if (apiError.errors.barcode != null) {

                      var builderBarcode = StringBuilder()
                      for (a in apiError.errors.barcode) {
                          builderBarcode.append("$a ")
                      }
                      Toast.makeText(this@QRcodeActivity, "" + builderBarcode, Toast.LENGTH_SHORT).show()

                  }


              }else if(response.code()==204){
                  val intent = Intent(this@QRcodeActivity,PurchasedItemsActivity::class.java)
                  intent.putExtra("no_product","no_product")
                  intent.putExtra("barcode",edt_barcode.text.toString())
                  startActivity(intent)
                  overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

                  showbtn()
              }else{
                  Toast.makeText(this@QRcodeActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                  showbtn()
              }
            }

            override fun onFailure(call: Call<Barcode>, t: Throwable) {
                Toast.makeText(this@QRcodeActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                showbtn()
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 33) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(this@QRcodeActivity, ScanActivity::class.java))
            } else {
                Toast.makeText(this, "نیاز به اجازه ی دسترسی دوربین", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showbtn() {
        btn_register_barcode.visibility = View.VISIBLE
        avi_register_barcode.visibility = View.GONE
    }

    private fun showAvi() {
        btn_register_barcode.visibility = View.GONE
        avi_register_barcode.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        // to delete BarcodeEditText while register successfully
        val barcode_state:String? = Cache.getString(this@QRcodeActivity,"barcode_registered")
        if(barcode_state =="barcode_registered"){
            edt_barcode.setText("")
            Cache.setString(this@QRcodeActivity,"barcode_registered","")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
        disposable.dispose()
    }
}