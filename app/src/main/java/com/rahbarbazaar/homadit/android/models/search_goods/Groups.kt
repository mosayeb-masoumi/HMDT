package com.rahbarbazaar.homadit.android.models.search_goods

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Groups() :Parcelable {


    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("icon")
    @Expose
    var icon: String? = null



    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        id = parcel.readString()
        icon = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<Groups> {
        override fun createFromParcel(parcel: Parcel): Groups {
            return Groups(parcel)
        }

        override fun newArray(size: Int): Array<Groups?> {
            return arrayOfNulls(size)
        }
    }


    override fun describeContents(): Int {
        return 0
    }


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(title)
        dest.writeString(id)
        dest.writeString(icon)
    }
}