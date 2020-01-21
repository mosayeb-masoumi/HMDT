package com.rahbarbazaar.shopper.models.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class MemberDetailObj {

    @SerializedName("label")
    @Expose
    var label: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null
}