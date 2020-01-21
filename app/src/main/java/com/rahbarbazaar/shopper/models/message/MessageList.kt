package com.rahbarbazaar.shopper.models.message

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageList {

    @SerializedName("total")
    @Expose
    var total: Int? = null
    @SerializedName("data")
    @Expose
    var data: List<Message>? = null
}