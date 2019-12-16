package com.rahbarbaazar.checkpanel.controllers.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.rahbarbaazar.checkpanel.R
import com.rahbarbaazar.checkpanel.controllers.interfaces.BarcodeItemInteraction
import com.rahbarbaazar.checkpanel.models.barcodlist.BarcodeData


class BarcodeListViewHolder (view: View, val context:Context) : RecyclerView.ViewHolder(view){


    private val txtDescription: TextView = view.findViewById(R.id.txt_barcoselist_item)
    private val btnDetail: Button = view.findViewById(R.id.btn_detail_barcoselist_item)
    private val btnChoose: Button = view.findViewById(R.id.btn_choose_barcoselist_item)

    fun bindUserData(model: BarcodeData) {

        txtDescription.text = model.decription
    }

    fun setOnBarcodeListHolderListener(listener: BarcodeItemInteraction?, model: BarcodeData, position: Int) {

        btnDetail.setOnClickListener {
            listener?.barcodeListOnClicked(model,"btnDetail")

        }

        btnChoose.setOnClickListener {
            listener?.barcodeListOnClicked(model,"btnChoose")
        }
    }

}