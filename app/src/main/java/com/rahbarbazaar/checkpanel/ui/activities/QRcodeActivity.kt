package com.rahbarbazaar.checkpanel.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.models.barcodlist.Barcode
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbazaar.checkpanel.utilities.GeneralTools
import com.rahbarbazaar.checkpanel.utilities.RxBus
import kotlinx.android.synthetic.main.activity_qrcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QRcodeActivity : CustomBaseActivity(), View.OnClickListener {

    private var connectivityReceiver: BroadcastReceiver? = null

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

        btn_choose_scanner.setOnClickListener(this)
        btn_choose_search.setOnClickListener(this)
        btn_register_barcode.setOnClickListener(this)


        // state can be null
        val state:String? = intent.getStringExtra("static_barcode")
        if(state=="static_barcode"){
            edt_barcode.setText("")
        }else{
            edt_barcode.setText(ResultScan)
        }



    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.btn_choose_scanner -> {
                startActivity(Intent(this@QRcodeActivity, ScanActivity::class.java))

            }

            R.id.btn_choose_search -> {

            }

            R.id.btn_register_barcode -> {
                getListOfProducts()
            }
        }
    }

    private fun getListOfProducts() {

        showAvi()

        val barcode = edt_barcode.text.toString()
        val service = ServiceProvider(this).getmService()
        val call = service.getBarcodeList(barcode)
//        val call = service.getBarcodeList("1398")
        call.enqueue(object : Callback<Barcode> {
            override fun onResponse(call: Call<Barcode>, response: Response<Barcode>) {
              if(response.code()==200){

                  var barcode = Barcode()
                  barcode = response.body()!!

                  RxBus.BarcodeList.publishBarcodeList(barcode)
                  startActivity(Intent(this@QRcodeActivity, BarcodeListActivity::class.java))
                  overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                  finish()

                  showbtn()

              }else if(response.code()==422){
                  showbtn()

              }else if(response.code()==204){
                  Toast.makeText(this@QRcodeActivity, "محصول وجود ندارد", Toast.LENGTH_SHORT).show()
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
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }
}