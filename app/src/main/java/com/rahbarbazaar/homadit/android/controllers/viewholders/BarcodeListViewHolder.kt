package com.rahbarbazaar.homadit.android.controllers.viewholders

import android.app.AlertDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.interfaces.BarcodeItemInteraction
import com.rahbarbazaar.homadit.android.models.barcodlist.Barcode
import com.rahbarbazaar.homadit.android.models.barcodlist.BarcodeData


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