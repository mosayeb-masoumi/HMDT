package com.rahbarbazaar.shopper.controllers.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.shopper.controllers.interfaces.EditProductsItemInteraction
import com.rahbarbazaar.shopper.controllers.viewholders.EditProductsViewHolder
import com.rahbarbazaar.shopper.models.edit_products.EditProducts

class EditProductsAdapter(private val editProducts: List<EditProducts>, val context: Context) :
        RecyclerView.Adapter<EditProductsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EditProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_edit_products, parent, false)
        return EditProductsViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: EditProductsViewHolder, position: Int) {

        val model = editProducts[position]
        holder.bindUserData(model)
        holder.setOnEditProductsListHolderListener(listener, model, position)
    }

    private var listener: EditProductsItemInteraction? = null
    fun setListener(listener: EditProductsItemInteraction) {
        this.listener = listener
    }

    override fun getItemCount(): Int {

        return  editProducts.size
    }
}