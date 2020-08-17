package com.rahbarbazaar.homadit.android.models.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Member {

    @SerializedName("data")
    @Expose
    var data: List<MemberDetail>? = null
}