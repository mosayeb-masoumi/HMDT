package com.rahbarbazaar.checkpanel.controllers.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.interfaces.EditProductsItemInteraction
import com.rahbarbazaar.checkpanel.models.edit_products.EditProducts

class EditProductsViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {


    private val txtTitle: TextView = itemView.findViewById(R.id.txt_title_edit_products)
    private val btn_delete: Button = itemView.findViewById(R.id.btn_delete_edit_product)
    private val btn_edit: Button = itemView.findViewById(R.id.btn_edit_edit_product)


    fun bindUserData(model: EditProducts) {
        txtTitle.text = model.description

    }

    fun setOnEditProductsListHolderListener(listener: EditProductsItemInteraction?, model: EditProducts, position: Int) {

        btn_delete.setOnClickListener {

            listener?.editProductsListOnClicked(model,position, "delete")
        }


        btn_edit.setOnClickListener {
            listener?.editProductsListOnClicked(model,position, "edit")
        }

    }

}