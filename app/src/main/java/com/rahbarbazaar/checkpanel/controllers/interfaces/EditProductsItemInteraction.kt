package com.rahbarbazaar.checkpanel.controllers.interfaces

import com.rahbarbazaar.checkpanel.models.edit_products.EditProducts


interface EditProductsItemInteraction {

    fun editProductsListOnClicked(model: EditProducts, position: Int, status:String)
}