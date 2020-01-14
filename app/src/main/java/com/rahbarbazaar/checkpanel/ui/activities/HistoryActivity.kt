package com.rahbarbazaar.checkpanel.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.adapters.HistoryAdapter
import com.rahbarbazaar.checkpanel.controllers.interfaces.HistoryItemInteraction
import com.rahbarbazaar.checkpanel.models.edit_products.TotalEditProductData
import com.rahbarbazaar.checkpanel.models.history.HistoryData
import com.rahbarbazaar.checkpanel.models.history.History
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbazaar.checkpanel.utilities.DialogFactory
import com.rahbarbazaar.checkpanel.utilities.GeneralTools
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_edit_product.*
import kotlinx.android.synthetic.main.activity_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : CustomBaseActivity(), HistoryItemInteraction {


    private var connectivityReceiver: BroadcastReceiver? = null

    lateinit var historyData: HistoryData
    lateinit var history: ArrayList<History>
    private lateinit var adapter: HistoryAdapter

    private var linearLayoutManager: LinearLayoutManager? = null
    private var isScrolling: Boolean = false
    var page: Int = 0
    var totalPages: Int = 0
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@HistoryActivity, findViewById<View>(R.id.rl_root_barcodelist))
            }
        }


        linear_exit_history_list.setOnClickListener {
            finish()
        }

    }

    private fun getHistoryList(page: Int) {
        avi_history.visibility = View.VISIBLE
        val service = ServiceProvider(this@HistoryActivity).getmService()
        val call = service.getHistoryList(page)
        call.enqueue(object : Callback<HistoryData> {
            override fun onResponse(call: Call<HistoryData>, response: Response<HistoryData>) {
                if (response.code() == 200) {

                    historyData = response.body()!!
                    setRecyclerView(historyData)
                    avi_history.visibility = View.GONE

                } else if (response.code() == 204) {
                    avi_history.visibility = View.GONE
                    if (this@HistoryActivity.page == 0) {
                        txt_no_message_history.visibility = View.VISIBLE
                    } else {
                        txt_no_message_history.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(this@HistoryActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    avi_history.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<HistoryData>, t: Throwable) {
                Toast.makeText(this@HistoryActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                avi_history.visibility = View.GONE
            }

        })
    }

    private fun setRecyclerView(historyData: HistoryData) {

        totalPages = historyData.total!!

        if (page == 0) {
            history.clear()
        }

        history.addAll(historyData.data!!)
        linearLayoutManager = LinearLayoutManager(this@HistoryActivity, LinearLayout.VERTICAL, false)

        val rv_history: RecyclerView = findViewById(R.id.rv_history)
        rv_history.layoutManager = linearLayoutManager

        adapter = HistoryAdapter(history, this@HistoryActivity)
        adapter.setListener(this)  // important to set or else the app will crashed (onClick)
        rv_history.adapter = adapter
        adapter.notifyDataSetChanged()

        rv_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentItems = linearLayoutManager!!.childCount
                totalItems = linearLayoutManager!!.itemCount
                scrollOutItems = linearLayoutManager!!.findFirstVisibleItemPosition()

                if (isScrolling &&(currentItems + scrollOutItems == totalItems) ) {

                    isScrolling = false
                    page++

                    if(page<=totalPages){
                        getHistoryList(page)
                    }

                }
            }
        })

    }

    override fun historyListOnClicked(model: History, btn_title: String) {
        if (btn_title == "btn_detail"){

            val dialogFactory = DialogFactory(this)
            dialogFactory.createHistoryDetailDialog(object : DialogFactory.DialogFactoryInteraction {
                override fun onAcceptButtonClicked(vararg strings: String?) {
                }

                override fun onDeniedButtonClicked(cancel_dialog: Boolean) {

                }

            }, history_root, model)

        }else if(btn_title == "btn_shop_item"){

            val intent = Intent(this@HistoryActivity,ShoppingProducts::class.java)
            intent.putExtra("shopping_product_id", model.id)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

        }

    }



    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        // message must be initialize
        history = ArrayList<History>()
        page=0
        getHistoryList(page)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)

    }

}