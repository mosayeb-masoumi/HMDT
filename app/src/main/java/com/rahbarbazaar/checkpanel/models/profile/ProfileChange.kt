package com.rahbarbazaar.checkpanel.models.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileChange {
    @SerializedName("data")
    @Expose
    var data: Boolean? = null
}