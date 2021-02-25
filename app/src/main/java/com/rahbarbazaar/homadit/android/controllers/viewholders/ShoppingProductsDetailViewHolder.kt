package com.rahbarbazaar.homadit.android.controllers.viewholders

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.models.shopping_product.Detail

class ShoppingProductsDetailViewHolder (view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    private val txt_lable: TextView = itemView.findViewById(R.id.txt_lable)
    private val txt_value: TextView = itemView.findViewById(R.id.txt_value)

    @SuppressLint("SetTextI18n")
    fun bindUserData(model: Detail) {
        txt_lable.text = model.label + ":"
        txt_value.text= model.value!!.trim()
    }

}