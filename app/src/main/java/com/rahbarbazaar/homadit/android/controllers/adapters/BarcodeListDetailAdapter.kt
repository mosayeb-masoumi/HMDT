package com.rahbarbazaar.homadit.android.controllers.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.viewholders.BarcodeListDetailViewHolder
import com.rahbarbazaar.homadit.android.models.barcodlist.BarcodeDetail

class BarcodeListDetailAdapter (private val barcodeDetail: List<BarcodeDetail>, val context: Context) :
        RecyclerView.Adapter<BarcodeListDetailViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BarcodeListDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_barcodlist_detail, parent, false)
        return BarcodeListDetailViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: BarcodeListDetailViewHolder, position: Int) {
        val model = barcodeDetail[position]
        holder.bindBarcodeDetailData(model)
    }

    override fun getItemCount(): Int {
        return barcodeDetail.size
    }
}