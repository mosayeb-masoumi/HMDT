package com.rahbarbazaar.checkpanel.models.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileData {
    @SerializedName("data")
    @Expose
    var data: Data? = null
}