package com.rahbarbazaar.checkpanel.controllers.interfaces

import com.rahbarbazaar.checkpanel.models.shopping_product.ShoppingProductList


interface ShoppingProductsItemInteraction {

    fun shoppingProductsListOnClicked(model: ShoppingProductList, position: Int)
}