package com.rahbarbazaar.homadit.android.controllers.interfaces

import android.widget.Button
import com.rahbarbazaar.homadit.android.models.edit_products.EditProducts
import com.wang.avi.AVLoadingIndicatorView


interface EditProductsItemInteraction {

    fun editProductsListOnClicked(model: EditProducts, position: Int, status: String, avi: AVLoadingIndicatorView, btn_delete: Button)
}