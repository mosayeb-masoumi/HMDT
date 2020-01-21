package com.rahbarbazaar.shopper.models.message

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Message {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("body")
    @Expose
    var body: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("expanded")
    @Expose
    var expanded: Boolean? = null

}