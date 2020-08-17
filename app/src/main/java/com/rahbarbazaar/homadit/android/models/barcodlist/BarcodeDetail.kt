package com.rahbarbazaar.homadit.android.models.barcodlist

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BarcodeDetail(): Parcelable{

    @SerializedName("label")
    @Expose
    var label: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null

    constructor(parcel: Parcel) : this() {
        label = parcel.readString()
        value = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BarcodeDetail> {
        override fun createFromParcel(parcel: Parcel): BarcodeDetail {
            return BarcodeDetail(parcel)
        }

        override fun newArray(size: Int): Array<BarcodeDetail?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(label)
        dest.writeString(value)
    }
}