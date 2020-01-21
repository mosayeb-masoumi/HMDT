package com.rahbarbazaar.shopper.controllers.interfaces

import com.rahbarbazaar.shopper.models.shopping_product.ShoppingProductList


interface ShoppingProductsItemInteraction {

    fun shoppingProductsListOnClicked(model: ShoppingProductList, position: Int)
}