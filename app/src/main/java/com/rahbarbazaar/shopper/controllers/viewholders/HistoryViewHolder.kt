package com.rahbarbazaar.shopper.controllers.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.shopper.controllers.interfaces.HistoryItemInteraction
import com.rahbarbazaar.shopper.models.history.History
import com.rahbarbazaar.shopper.utilities.ConvertEnDigitToFa
import com.wang.avi.AVLoadingIndicatorView

class HistoryViewHolder (view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    private val txtDate: TextView = itemView.findViewById(R.id.txt_date_historyitem)
    private val txtTitle: TextView = itemView.findViewById(R.id.txt_title_historyitem)
    private val btn_shop_item: Button = itemView.findViewById(R.id.btn_history_shopitem)
    private val avi: AVLoadingIndicatorView = itemView.findViewById(R.id.avi_history_shopitem)

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

        itemView.setOnClickListener {
            listener?.historyListOnClicked(model,"item_detail")
        }

       btn_shop_item.setOnClickListener {
           listener?.historyListOnClicked(model,"btn_shop_item")
       }
    }
}