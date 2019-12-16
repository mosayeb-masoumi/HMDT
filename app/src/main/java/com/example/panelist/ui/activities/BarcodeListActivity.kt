package com.example.panelist.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.method.TextKeyListener.clear
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.panelist.R
import com.example.panelist.controllers.adapters.AdapterBarcodeList
import com.example.panelist.controllers.interfaces.BarcodeItemInteraction
import com.example.panelist.models.barcodlist.Barcode
import com.example.panelist.models.barcodlist.BarcodeData
import com.example.panelist.utilities.CustomBaseActivity
import com.example.panelist.utilities.DialogFactory
import com.example.panelist.utilities.GeneralTools
import com.example.panelist.utilities.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_barcode_list.*


class BarcodeListActivity : CustomBaseActivity(), BarcodeItemInteraction {

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

// get data from rxbus
        disposable = CompositeDisposable()
        disposable = RxBus.BarcodeList.subscribeBarcodeList { result ->
            if (result is Barcode) {
                barcode = result
            }
        }

        setRecyclerView()
    }


    private fun setRecyclerView() {

        val barcodeList = ArrayList<BarcodeData>()

        for (i in barcode.data!!.indices) {
//            if (lists[i].name == "reza") {
////                lists.removeAt(i)
////            }
            barcodeList.add(BarcodeData(barcode.data!![i].id, barcode.data!![i].decription ,barcode.data!![i].subCategory,
                    barcode.data!![i].main,barcode.data!![i].category, barcode.data!![i].owner, barcode.data!![i].brand ,
                    barcode.data!![i].subBrand, barcode.data!![i].company ,  barcode.data!![i].country , barcode.data!![i].unit ,
                    barcode.data!![i].packaging, barcode.data!![i].price,barcode.data!![i].type ,barcode.data!![i].amount,
                    barcode.data!![i].image))
        }

        val recyclerview: RecyclerView = findViewById(R.id.recycler_barcodelist)

        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val adapter = AdapterBarcodeList(barcodeList, this@BarcodeListActivity)
        adapter.setListener(this)  // important to set or else the app will crashed
        recyclerview.adapter = adapter
//        adapter.notifyDataSetChanged()


    }


    override fun barcodeListOnClicked(model: BarcodeData, state: String) {

        when (state) {
            "btnDetail" -> {
                val dialogFactory = DialogFactory(this)
                dialogFactory.createbarcodeListDetailDialog(object : DialogFactory.DialogFactoryInteraction {
                    override fun onAcceptButtonClicked(vararg strings: String?) {
                    }

                    override fun onDeniedButtonClicked(cancel_dialog: Boolean) {

                    }

                },rl_root_barcodelist,model)


            }
            "btnChoose" -> {
                Toast.makeText(this, "" + state, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
//        disposable.dispose()
        disposable.apply {
            dispose()
        }
    }

}