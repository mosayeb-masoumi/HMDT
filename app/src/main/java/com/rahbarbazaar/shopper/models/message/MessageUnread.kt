package com.rahbarbazaar.shopper.models.message

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MessageUnread {

    @SerializedName("data")
    @Expose
    var data: Int? = null
}