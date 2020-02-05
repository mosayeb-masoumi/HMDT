package com.rahbarbazaar.shopper.models.barcodlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BarcodeData {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("mygroup")
    @Expose
    var mygroup: String? = null
    @SerializedName("barcode")
    @Expose
    var barcode: String? = null
    @SerializedName("decription")
    @Expose
    var decription: String? = null

    @SerializedName("unit")
    @Expose
    var unit: String? = null


    @SerializedName("detail")
    @Expose
    var barcodeDetail: List<BarcodeDetail>? = null


    constructor(id: String?, mygroup: String?, barcode: String?, decription: String?, unit: String?,
                barcodeDetail: List<BarcodeDetail>?) {
        this.id = id
        this.mygroup = mygroup
        this.barcode = barcode
        this.decription = decription
        this.unit = unit
        this.barcodeDetail = barcodeDetail
    }
}