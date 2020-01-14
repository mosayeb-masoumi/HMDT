package com.rahbarbazaar.checkpanel.controllers.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.interfaces.ShoppingProductsItemInteraction
import com.rahbarbazaar.checkpanel.models.edit_products.EditProducts

class ShoppingProductsViewHolder (view: View, val context: Context) : RecyclerView.ViewHolder(view) {


    private val txtTitle: TextView = itemView.findViewById(R.id.txt_title_shopping_products)
    private val btn_detail: Button = itemView.findViewById(R.id.btn_detail_shopping_product)



    fun bindUserData(model: EditProducts) {
        txtTitle.text = model.description

    }

    fun setOnShoppingProductsListHolderListener(listener: ShoppingProductsItemInteraction?, model: EditProducts, position: Int) {

        btn_detail.setOnClickListener {

            listener?.shoppingProductsListOnClicked(model,position)
        }

    }


}