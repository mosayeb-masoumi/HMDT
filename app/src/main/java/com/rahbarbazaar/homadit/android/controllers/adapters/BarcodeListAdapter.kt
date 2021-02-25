package com.rahbarbazaar.homadit.android.controllers.adapters

import android.app.AlertDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.interfaces.BarcodeItemInteraction
import com.rahbarbazaar.homadit.android.controllers.viewholders.BarcodeListViewHolder
import com.rahbarbazaar.homadit.android.models.barcodlist.Barcode
import com.rahbarbazaar.homadit.android.models.barcodlist.BarcodeData

class BarcodeListAdapter(private val barcodeList: MutableList<BarcodeData>, val context: Context,  val dialog: AlertDialog, val barcode: Barcode) :
        RecyclerView.Adapter<BarcodeListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BarcodeListViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_barcodelist, parent, false)
        return BarcodeListViewHolder(view, context)
    }


    override fun onBindViewHolder(holder: BarcodeListViewHolder, position: Int) {

        val model = barcodeList[position]
        holder.bindUserData(model)
        holder.setOnBarcodeListHolderListener(listener, model,position ,barcode,dialog)
    }

    private var listener: BarcodeItemInteraction? = null
    fun setListener(listener: BarcodeItemInteraction) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return barcodeList.size
    }
}