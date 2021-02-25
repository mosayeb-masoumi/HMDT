package com.rahbarbazaar.homadit.android.controllers.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.viewholders.ShoppingProductsDetailViewHolder
import com.rahbarbazaar.homadit.android.models.shopping_product.Detail

class ShoppingProductsDetailAdapter (private val detail: List<Detail>, val context: Context) :
        RecyclerView.Adapter<ShoppingProductsDetailViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ShoppingProductsDetailViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_barcodlist_detail, parent, false)
        return ShoppingProductsDetailViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: ShoppingProductsDetailViewHolder, position: Int) {
        val model = detail[position]
        holder.bindUserData(model)
//        holder.setOnShoppingProductsListHolderListener(model, position)
    }

    override fun getItemCount(): Int {
        return  detail.size
    }
}