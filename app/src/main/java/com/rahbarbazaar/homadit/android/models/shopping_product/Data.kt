package com.rahbarbazaar.homadit.android.models.shopping_product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("total")
    @Expose
    var total: Int? = null
    @SerializedName("bought")
    @Expose
    var bought: Bought? = null
}