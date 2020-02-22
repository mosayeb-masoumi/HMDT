package com.rahbarbazaar.shopper.controllers.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.shopper.controllers.interfaces.ShoppingProductsItemInteraction
import com.rahbarbazaar.shopper.models.shopping_product.ShoppingProductList

class ShoppingProductsViewHolder (view: View, val context: Context) : RecyclerView.ViewHolder(view) {


    private val txtTitle: TextView = itemView.findViewById(R.id.txt_title_shopping_products)
//    private val btn_detail: Button = itemView.findViewById(R.id.btn_detail_shopping_product)



    fun bindUserData(model: ShoppingProductList) {
        txtTitle.text = model.description

    }

    fun setOnShoppingProductsListHolderListener(listener: ShoppingProductsItemInteraction?, model: ShoppingProductList, position: Int) {

        itemView.setOnClickListener {

            listener?.shoppingProductsListOnClicked(model,position)
        }

    }


}