package com.rahbarbazaar.shopper.models.shopping_product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class TotalShoppingProductData {

    @SerializedName("data")
    @Expose
    var data: Data? = null
}