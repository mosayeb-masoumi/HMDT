package com.rahbarbazaar.checkpanel.controllers.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.interfaces.HistoryItemInteraction
import com.rahbarbazaar.checkpanel.models.history.History
import com.rahbarbazaar.checkpanel.utilities.ConvertEnDigitToFa

class HistoryViewHolder (view: View, val context: Context) : RecyclerView.ViewHolder(view) {


    private val txtDate: TextView = itemView.findViewById(R.id.txt_date_historyitem)
    private val txtTitle: TextView = itemView.findViewById(R.id.txt_title_historyitem)
    private val btn_detail: Button = itemView.findViewById(R.id.btn_detail_history_item)
    private val btn_shop_item: Button = itemView.findViewById(R.id.btn_history_shopitem)


    fun bindUserData(model: History) {


        txtTitle.text= model.title

        // to delete time from date
//        val year = model.date!!.substring(0, 4)
//        val month = model.date!!.substring(5, 7)
//        val day = model.date!!.substring(8, 10)
//        val date = "$year/$month/$day"

        val convert = ConvertEnDigitToFa.convert(model.date)
        txtDate.text = convert

    }

    fun setOnMessageListHolderListener(listener: HistoryItemInteraction?, model: History, position: Int) {

        btn_detail.setOnClickListener {
            listener?.historyListOnClicked(model,"btn_detail")
        }

       btn_shop_item.setOnClickListener {
           listener?.historyListOnClicked(model,"btn_shop_item")
       }
    }
}