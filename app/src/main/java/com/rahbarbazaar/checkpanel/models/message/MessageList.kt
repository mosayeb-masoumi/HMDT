package com.rahbarbazaar.checkpanel.models.message

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageList {
    @SerializedName("data")
    @Expose
    var data: List<Message>? = null
}