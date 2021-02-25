package com.rahbarbazaar.homadit.android.controllers.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.interfaces.ShoppingProductsItemInteraction
import com.rahbarbazaar.homadit.android.controllers.viewholders.ShoppingProductsViewHolder
import com.rahbarbazaar.homadit.android.models.shopping_product.ShoppingProductList

class ShoppingProductsAdapter (private val shoppingProductList: List<ShoppingProductList>, val context: Context) :
        RecyclerView.Adapter<ShoppingProductsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ShoppingProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_shopping_products, parent, false)
        return ShoppingProductsViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: ShoppingProductsViewHolder, position: Int) {

        val model = shoppingProductList[position]
        holder.bindUserData(model)
        holder.setOnShoppingProductsListHolderListener(listener, model, position)
    }

    private var listener: ShoppingProductsItemInteraction? = null
    fun setListener(listener: ShoppingProductsItemInteraction) {
        this.listener = listener

    }

    override fun getItemCount(): Int {

        return  shoppingProductList.size
    }
}