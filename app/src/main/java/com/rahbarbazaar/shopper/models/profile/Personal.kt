package com.rahbarbazaar.shopper.models.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Personal {
    @SerializedName("mobile")
    @Expose
    var mobile: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}