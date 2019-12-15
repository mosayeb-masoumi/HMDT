package com.example.panelist.controllers.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.panelist.R
import com.example.panelist.controllers.viewholders.BarcodeItemInteraction
import com.example.panelist.controllers.viewholders.BarcodeListViewHolder
import com.example.panelist.models.barcodlist.Barcode

class AdapterBarcodeList (private val barcodList: List<Barcode>, val listener: BarcodeItemInteraction<Barcode>) :
        RecyclerView.Adapter<BarcodeListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BarcodeListViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_barcodelist, parent, false)
        return BarcodeListViewHolder(view,listener)
    }

    override fun getItemCount(): Int {
        return barcodList.size
    }

    override fun onBindViewHolder(holder: BarcodeListViewHolder, position: Int) {
        holder.bindUserData(barcodList[position])

    }

}