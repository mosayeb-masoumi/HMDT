package com.rahbarbazaar.shopper.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.shopper.controllers.adapters.BarcodeListAdapter
import com.rahbarbazaar.shopper.controllers.interfaces.BarcodeItemInteraction
import com.rahbarbazaar.shopper.models.barcodlist.Barcode
import com.rahbarbazaar.shopper.models.barcodlist.BarcodeData
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity
import com.rahbarbazaar.shopper.utilities.DialogFactory
import com.rahbarbazaar.shopper.utilities.GeneralTools
import com.rahbarbazaar.shopper.utilities.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_barcode_list.*


class BarcodeListActivity : CustomBaseActivity(), BarcodeItemInteraction {

    private var connectivityReceiver: BroadcastReceiver? = null

    var disposable: Disposable = CompositeDisposable()
    lateinit var barcode: Barcode

    var getBarcode: String? = ""

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

        getBarcode = intent.getStringExtra("barcode")

        linear_exit_barcode_list.setOnClickListener {
//            startActivity(Intent(this@BarcodeListActivity,QRcodeActivity::class.java))
            val intent=Intent(this@BarcodeListActivity,QRcodeActivityOld::class.java)
            intent.putExtra("static_barcode","static_barcode")
            startActivity(intent)
            finish()
        }

        rl_register_barcodeList.setOnClickListener {
            val intent = Intent(this@BarcodeListActivity, PurchasedItemsActivity::class.java)
            intent.putExtra("no_product", "no_product")
            intent.putExtra("barcode", getBarcode)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }


    private fun setRecyclerView() {

        val barcodeList = ArrayList<BarcodeData>()

        for (i in barcode.data!!.indices) {
//            if (lists[i].name == "reza") {
////                lists.removeAt(i)
////            }
            barcodeList.add(BarcodeData(barcode.data!![i].id, barcode.data!![i].mygroup, barcode.data!![i].show, barcode.data!![i].decription,
                    barcode.data!![i].unit,barcode.data!![i].image,barcode.data!![i].barcodeDetail))
        }

        val recyclerview: RecyclerView = findViewById(R.id.recycler_barcodelist)

        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val adapter = BarcodeListAdapter(barcodeList, this@BarcodeListActivity)
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

                }, rl_root_barcodelist, model)


            }
            "btnRegisterActive" -> {

                val intent = Intent(this, PurchasedItemsActivity::class.java)
//                intent.putExtra("unit", model.unit)
                intent.putExtra("product_id", model.id)
                intent.putExtra("barcodeListItemDesc", model.decription)
                intent.putExtra("mygroup", model.mygroup)
                intent.putExtra("unit", model.unit)

                startActivity(intent)
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            }

            "btnRegisterDeactive" -> {

                showDeactiveActionDialog()
            }
        }
    }

    private fun showDeactiveActionDialog() {

        val dialogFactory = DialogFactory(this)
        dialogFactory.createDeactiveActionDialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg params: String) {

            }

            override fun onDeniedButtonClicked(bool: Boolean) {

            }
        }, rl_root_barcodelist)

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