package com.rahbarbazaar.checkpanel.controllers.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.interfaces.ShoppingProductsItemInteraction
import com.rahbarbazaar.checkpanel.controllers.viewholders.ShoppingProductsViewHolder
import com.rahbarbazaar.checkpanel.models.edit_products.EditProducts

class ShoppingProductsAdapter (private val editProducts: List<EditProducts>, val context: Context) :
        RecyclerView.Adapter<ShoppingProductsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ShoppingProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_shopping_products, parent, false)
        return ShoppingProductsViewHolder(view,context)
    }


    override fun onBindViewHolder(holder: ShoppingProductsViewHolder, position: Int) {

        val model = editProducts[position]
        holder.bindUserData(model)
        holder.setOnShoppingProductsListHolderListener(listener, model, position)
    }

    private var listener: ShoppingProductsItemInteraction? = null
    fun setListener(listener: ShoppingProductsItemInteraction) {
        this.listener = listener

    }


    override fun getItemCount(): Int {

        return  editProducts.size
    }
}