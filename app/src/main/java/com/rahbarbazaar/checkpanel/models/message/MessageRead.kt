package com.rahbarbazaar.checkpanel.models.message

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageRead {

    @SerializedName("data")
    @Expose
    var data: Boolean? = null

}