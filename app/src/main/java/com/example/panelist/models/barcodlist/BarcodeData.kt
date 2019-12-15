package com.example.panelist.models.barcodlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BarcodeData {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("decription")
    @Expose
    var decription: String? = null
}