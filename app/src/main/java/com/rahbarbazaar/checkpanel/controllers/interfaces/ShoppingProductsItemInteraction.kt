package com.rahbarbazaar.checkpanel.controllers.interfaces

import android.widget.Button
import com.rahbarbazaar.checkpanel.models.edit_products.EditProducts


interface ShoppingProductsItemInteraction {

    fun shoppingProductsListOnClicked(model: EditProducts, position: Int)
}