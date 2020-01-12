package com.rahbarbazaar.checkpanel.models.barcodlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BarcodeData {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("mygroup")
    @Expose
    var mygroup: String? = null
    @SerializedName("show")
    @Expose
    var show: String? = null
    @SerializedName("decription")
    @Expose
    var decription: String? = null
    @SerializedName("detail")
    @Expose
    var barcodeDetail: List<BarcodeDetail>? = null


    constructor(id: String?, mygroup: String?, show: String?, decription: String?, barcodeDetail: List<BarcodeDetail>?) {
        this.id = id
        this.mygroup = mygroup
        this.show = show
        this.decription = decription
        this.barcodeDetail = barcodeDetail
    }
}