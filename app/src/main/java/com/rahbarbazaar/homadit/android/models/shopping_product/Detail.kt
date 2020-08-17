package com.rahbarbazaar.homadit.android.models.shopping_product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Detail {
    @SerializedName("label")
    @Expose
    var label: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null
}