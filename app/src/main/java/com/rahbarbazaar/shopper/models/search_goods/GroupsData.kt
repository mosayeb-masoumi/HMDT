package com.rahbarbazaar.shopper.models.search_goods

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GroupsData {

    @SerializedName("data")
    @Expose
    var data: List<Groups>? = null
}