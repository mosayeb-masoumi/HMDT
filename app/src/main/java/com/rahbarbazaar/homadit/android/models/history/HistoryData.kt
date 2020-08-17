package com.rahbarbazaar.homadit.android.models.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HistoryData {

    @SerializedName("total")
    @Expose
    var total: Int? = null
    @SerializedName("data")
    @Expose
    var data: List<History>? = null
}