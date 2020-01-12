package com.rahbarbazaar.checkpanel.controllers.viewholders

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.models.barcodlist.BarcodeDetail

class BarcodeListDetailViewHolder (view: View, val context: Context) : RecyclerView.ViewHolder(view) {


    private val txt_lable: TextView = itemView.findViewById(R.id.txt_lable)
    private val txt_value: TextView = itemView.findViewById(R.id.txt_value)

    @SuppressLint("SetTextI18n")
    fun bindBarcodeDetailData(model: BarcodeDetail) {
        txt_lable.text = model.label + ":"
        txt_value.text= model.value!!.trim()
    }
}