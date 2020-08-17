package com.rahbarbazaar.homadit.android.controllers.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.interfaces.ShoppingProductsItemInteraction
import com.rahbarbazaar.homadit.android.models.shopping_product.ShoppingProductList

class ShoppingProductsViewHolder (view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    private val txtTitle: TextView = itemView.findViewById(R.id.txt_title_shopping_products)

    fun bindUserData(model: ShoppingProductList) {
        txtTitle.text = model.description
    }

    fun setOnShoppingProductsListHolderListener(listener: ShoppingProductsItemInteraction?, model: ShoppingProductList, position: Int) {

        itemView.setOnClickListener {
            listener?.shoppingProductsListOnClicked(model,position)
        }
    }
}