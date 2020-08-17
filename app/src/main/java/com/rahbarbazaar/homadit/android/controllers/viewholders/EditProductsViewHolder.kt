package com.rahbarbazaar.homadit.android.controllers.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.interfaces.EditProductsItemInteraction
import com.rahbarbazaar.homadit.android.models.edit_products.EditProducts
import com.wang.avi.AVLoadingIndicatorView

class EditProductsViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    private val txtTitle: TextView = itemView.findViewById(R.id.txt_title_edit_products)
    private val btn_delete: Button = itemView.findViewById(R.id.btn_delete_edit_product)
    private val btn_edit: Button = itemView.findViewById(R.id.btn_edit_edit_product)
    private val avi: AVLoadingIndicatorView = itemView.findViewById(R.id.avi_delete_edit_product)

    fun bindUserData(model: EditProducts) {
        txtTitle.text = model.description

    }

    fun setOnEditProductsListHolderListener(listener: EditProductsItemInteraction?, model: EditProducts, position: Int) {

        btn_delete.setOnClickListener {
            listener?.editProductsListOnClicked(model,position, "delete" ,avi ,btn_delete)
        }

        btn_edit.setOnClickListener {
            listener?.editProductsListOnClicked(model, position, "edit", avi, btn_delete)
        }

    }

}