package com.rahbarbazaar.homadit.android.controllers.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.models.barcodlist.BarcodeDetail

class BarcodeListDetailViewHolder (view: View, val context: Context) : RecyclerView.ViewHolder(view) {


    private val txt_lable: TextView = itemView.findViewById(R.id.txt_lable)
    private val txt_value: TextView = itemView.findViewById(R.id.txt_value)

//    @SuppressLint("SetTextI18n")
    fun bindBarcodeDetailData(model: BarcodeDetail) {
        txt_lable.text = model.label + ":"
        txt_value.text= model.value!!
    }
}