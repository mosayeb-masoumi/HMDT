package com.rahbarbazaar.checkpanel.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.adapters.MessageAdapter
import com.rahbarbazaar.checkpanel.models.message.Message
import com.rahbarbazaar.checkpanel.models.message.MessageList
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbazaar.checkpanel.utilities.GeneralTools
import kotlinx.android.synthetic.main.activity_message.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessageActivity : CustomBaseActivity() {

    private var connectivityReceiver: BroadcastReceiver? = null

    lateinit var messageList: MessageList
    lateinit var message: ArrayList<Message>
    private lateinit var adapter: MessageAdapter

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


//        // message must be initialize
//        message = ArrayList<Message>()
//        getMessageList()


    }

    private fun getMessageList() {

        val service = ServiceProvider(this@MessageActivity).getmService()
        val call = service.getMessageList(0)
        call.enqueue(object : Callback<MessageList> {
            override fun onResponse(call: Call<MessageList>, response: Response<MessageList>) {
                if (response.code() == 200) {

                    messageList = response.body()!!
                    setRecyclerView(messageList)

                } else if (response.code() == 204) {
                    txt_no_message.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MessageActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<MessageList>, t: Throwable) {
                Toast.makeText(this@MessageActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setRecyclerView(messageList: MessageList) {

//        var message: ArrayList<Message>()
        message.addAll(messageList.data!!)

        val recyclerview: RecyclerView = findViewById(R.id.rv_message)
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        adapter = MessageAdapter(message, this@MessageActivity)
//        adapter.setListener(this)  // important to set or else the app will crashed
        recyclerview.adapter = adapter
        adapter.notifyDataSetChanged()

    }




    override fun onResume() {
        super.onResume()
        // message must be initialize
        message = ArrayList<Message>()
        getMessageList()
    }
}