package com.rahbarbazaar.homadit.android.controllers.interfaces

import com.rahbarbazaar.homadit.android.models.shopping_product.ShoppingProductList


interface ShoppingProductsItemInteraction {

    fun shoppingProductsListOnClicked(model: ShoppingProductList, position: Int)
}