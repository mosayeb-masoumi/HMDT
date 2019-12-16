package com.example.panelist.controllers.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.panelist.R
import com.example.panelist.controllers.interfaces.BarcodeItemInteraction
import com.example.panelist.controllers.viewholders.BarcodeListViewHolder
import com.example.panelist.models.barcodlist.BarcodeData

class AdapterBarcodeList (private val barcodeList: List<BarcodeData>, val context: Context) :
        RecyclerView.Adapter<BarcodeListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BarcodeListViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_barcodelist, parent, false)
        return BarcodeListViewHolder(view, context)
    }


    override fun onBindViewHolder(holder: BarcodeListViewHolder, position: Int) {

        val model = barcodeList[position]
        holder.bindUserData(model)
        holder.setOnBarcodeListHolderListener(listener, model, position)
    }

    private var listener: BarcodeItemInteraction? = null
    fun setListener(listener: BarcodeItemInteraction) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return barcodeList.size
    }

}