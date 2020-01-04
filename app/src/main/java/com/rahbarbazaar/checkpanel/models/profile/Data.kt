package com.rahbarbazaar.checkpanel.models.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("personal")
    @Expose
    var personal: Personal? = null
    @SerializedName("family")
    @Expose
    var family: List<Family>? = null
    @SerializedName("member")
    @Expose
    var member: Member? = null

}