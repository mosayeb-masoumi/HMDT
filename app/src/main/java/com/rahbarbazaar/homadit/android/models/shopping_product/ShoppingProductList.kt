package com.rahbarbazaar.homadit.android.models.shopping_product


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ShoppingProductList {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("detail")
    @Expose
    var detail: List<Detail>? = null
}