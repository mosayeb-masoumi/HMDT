package com.rahbarbazaar.shopper.models.search_goods

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GroupsData() :Parcelable{


    @SerializedName("data")
    @Expose
    var data: List<Groups>? = null


    constructor(parcel: Parcel) : this() {
        data = parcel.createTypedArrayList(Groups.CREATOR)
    }


    
    companion object CREATOR : Parcelable.Creator<GroupsData> {
        override fun createFromParcel(parcel: Parcel): GroupsData {
            return GroupsData(parcel)
        }

        override fun newArray(size: Int): Array<GroupsData?> {
            return arrayOfNulls(size)
        }
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeTypedList(data)
    }


}



