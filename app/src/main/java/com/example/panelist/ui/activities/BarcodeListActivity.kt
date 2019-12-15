package com.example.panelist.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import com.example.panelist.R
import com.example.panelist.models.barcodlist.Barcode
import com.example.panelist.utilities.CustomBaseActivity
import com.example.panelist.utilities.GeneralTools
import com.example.panelist.utilities.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


class BarcodeListActivity : CustomBaseActivity(){

    private var connectivityReceiver: BroadcastReceiver? = null

    var disposable: Disposable = CompositeDisposable()
    lateinit var barcode: Barcode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_list)

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@BarcodeListActivity, findViewById<View>(R.id.rl_root_barcodelist))
            }
        }


        disposable = CompositeDisposable()
        disposable = RxBus.BarcodeList.subscribeBarcodeList() { result ->
            if (result is Barcode) {
                barcode = result
            }
        }

        setRecyclerView()

    }

    private fun setRecyclerView() {

    }


    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
        disposable.dispose()
    }

}