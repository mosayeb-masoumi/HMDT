package com.rahbarbazaar.homadit.android.models.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Family {

    @SerializedName("label")
    @Expose
    var label: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null

}