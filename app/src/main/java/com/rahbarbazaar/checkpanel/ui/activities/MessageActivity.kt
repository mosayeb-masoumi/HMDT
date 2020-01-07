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
import android.widget.LinearLayout
import android.widget.Toast
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.adapters.MessageAdapter
import com.rahbarbazaar.checkpanel.controllers.interfaces.MessageItemInteraction
import com.rahbarbazaar.checkpanel.models.message.Message
import com.rahbarbazaar.checkpanel.models.message.MessageList
import com.rahbarbazaar.checkpanel.models.message.MessageRead
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbazaar.checkpanel.utilities.GeneralTools
import kotlinx.android.synthetic.main.activity_message.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessageActivity : CustomBaseActivity(), MessageItemInteraction {


    private var connectivityReceiver: BroadcastReceiver? = null

    lateinit var messageList: MessageList
    lateinit var message: ArrayList<Message>
    private lateinit var adapter: MessageAdapter

    private var linearLayoutManager: LinearLayoutManager? = null
    private var isScrolling: Boolean = false
    var page: Int = 0
    var totalPages: Int = 0
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@MessageActivity, findViewById<View>(R.id.rl_root))
            }
        }


        ll_return_message.setOnClickListener {
            finish()
        }

    }

    private fun getMessageList(page: Int) {

        avi_message.visibility = View.VISIBLE
        val service = ServiceProvider(this@MessageActivity).getmService()
        val call = service.getMessageList(page)
        call.enqueue(object : Callback<MessageList> {
            override fun onResponse(call: Call<MessageList>, response: Response<MessageList>) {
                if (response.code() == 200) {

                    messageList = response.body()!!
                    setRecyclerView(messageList)
                    avi_message.visibility = View.GONE

                } else if (response.code() == 204) {
                    avi_message.visibility = View.GONE
                    if (this@MessageActivity.page == 0) {
                        txt_no_message.visibility = View.VISIBLE
                    }else{
                        txt_no_message.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(this@MessageActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    avi_message.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<MessageList>, t: Throwable) {
                Toast.makeText(this@MessageActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                avi_message.visibility = View.GONE
            }

        })

    }

    private fun setRecyclerView(messageList: MessageList) {

        totalPages = messageList.total!!

        if (page == 0) {
            message.clear()
        }

        linearLayoutManager = LinearLayoutManager(this@MessageActivity, LinearLayout.VERTICAL, false)

//        var message: ArrayList<Message>()
        message.addAll(messageList.data!!)

        val rv_message: RecyclerView = findViewById(R.id.rv_message)
        rv_message.layoutManager = linearLayoutManager

        adapter = MessageAdapter(message, this@MessageActivity)
        adapter.setListener(this)  // important to set or else the app will crashed (onClick)
        rv_message.adapter = adapter
        adapter.notifyDataSetChanged()

        rv_message.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        getMessageList(page)
                    }

                }
            }
        })

    }


    override fun messageListOnClicked(model: Message, position: Int, status: String) {
        // to notify expand and collaps item
        adapter.notifyItemChanged(position)

        if (status == "unseen") {
            requestFirstReadMeassage(model.id)
        }

    }

    private fun requestFirstReadMeassage(id: String?) {
        val service = ServiceProvider(this@MessageActivity).getmService()
        val call = service.getMessageRead(id)
        call.enqueue(object : Callback<MessageRead> {

            override fun onResponse(call: Call<MessageRead>, response: Response<MessageRead>) {
                if (response.code() == 200) {

                    var state: Boolean? = response.body()?.data

                } else {
                    Toast.makeText(this@MessageActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<MessageRead>, t: Throwable) {
                Toast.makeText(this@MessageActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
            }


        })
    }


    override fun onResume() {
        super.onResume()
        // message must be initialize
        message = ArrayList<Message>()
        getMessageList(page)
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }
}