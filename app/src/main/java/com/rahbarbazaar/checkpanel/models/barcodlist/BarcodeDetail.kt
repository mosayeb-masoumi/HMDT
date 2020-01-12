package com.rahbarbazaar.checkpanel.models.barcodlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BarcodeDetail {
    @SerializedName("label")
    @Expose
    var label: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null
}