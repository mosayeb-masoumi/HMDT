package com.rahbarbazaar.shopper.models.barcodlist

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Barcode():Parcelable{

    @SerializedName("data")
    @Expose
    var data: List<BarcodeData>? = null

    constructor(parcel: Parcel) : this() {
        data = parcel.createTypedArrayList(BarcodeData.CREATOR)
    }

    companion object CREATOR : Parcelable.Creator<Barcode> {
        override fun createFromParcel(parcel: Parcel): Barcode {
            return Barcode(parcel)
        }

        override fun newArray(size: Int): Array<Barcode?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(data)
    }
}

