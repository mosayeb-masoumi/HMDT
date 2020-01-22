package com.rahbarbazaar.shopper.models.search_goods

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Groups {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
}