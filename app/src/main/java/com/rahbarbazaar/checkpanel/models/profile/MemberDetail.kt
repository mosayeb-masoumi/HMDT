package com.rahbarbazaar.checkpanel.models.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class MemberDetail {

    @SerializedName("role")
    @Expose
    var role: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("data")
    @Expose
    var data: List<MemberDetailObj>? = null
}