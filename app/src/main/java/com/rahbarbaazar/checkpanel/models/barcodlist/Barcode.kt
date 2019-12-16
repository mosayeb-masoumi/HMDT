package com.rahbarbaazar.checkpanel.models.barcodlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Barcode {

    @SerializedName("data")
    @Expose
    var data: List<BarcodeData>? = null
}