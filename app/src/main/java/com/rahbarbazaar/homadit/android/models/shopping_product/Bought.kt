package com.rahbarbazaar.homadit.android.models.shopping_product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Bought {
    @SerializedName("data")
    @Expose
    var data: List<ShoppingProductList>? = null
}