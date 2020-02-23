package com.rahbarbazaar.shopper.controllers.viewholders

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.shopper.controllers.interfaces.BarcodeItemInteraction
import com.rahbarbazaar.shopper.models.barcodlist.Barcode
import com.rahbarbazaar.shopper.models.barcodlist.BarcodeData


class BarcodeListViewHolder (view: View, val context:Context) : RecyclerView.ViewHolder(view){

    private val txtDescription: TextView = view.findViewById(R.id.txt_barcoselist_item)

    fun bindUserData(model: BarcodeData) {
        txtDescription.text = model.decription
    }

    fun setOnBarcodeListHolderListener(listener: BarcodeItemInteraction?, model: BarcodeData, position: Int, barcode: Barcode, dialog: AlertDialog) {

        itemView.setOnClickListener {
            listener?.barcodeListOnClicked(model,position,barcode,dialog)
        }
    }

}