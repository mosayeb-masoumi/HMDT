package com.rahbarbazaar.shopper.models.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class MemberData {
    @SerializedName("data")
    @Expose
    var data: List<Member>? = null
}