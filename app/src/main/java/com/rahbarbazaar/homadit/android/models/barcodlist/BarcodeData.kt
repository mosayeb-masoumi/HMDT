package com.rahbarbazaar.homadit.android.models.barcodlist

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BarcodeData() : Parcelable {


    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("mygroup")
    @Expose
    var mygroup: String? = null

    @SerializedName("barcode")
    @Expose
    var barcode: String? = null

    @SerializedName("decription")
    @Expose
    var decription: String? = null

    @SerializedName("unit")
    @Expose
    var unit: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("detail")
    @Expose
    var barcodeDetail: List<BarcodeDetail>? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("min_price")
    @Expose
    var minPrice: String? = null
    @SerializedName("max_price")
    @Expose
    var maxPrice: String? = null
    @SerializedName("max_amount")
    @Expose
    var maxAmount: String? = null

    @SerializedName("word")
    @Expose
    var word: Boolean = false





    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        mygroup = parcel.readString()
        barcode = parcel.readString()
        decription = parcel.readString()
        unit = parcel.readString()
        image = parcel.readString()
        barcodeDetail = parcel.createTypedArrayList(BarcodeDetail.CREATOR)
        price = parcel.readString()
        minPrice = parcel.readString()
        maxPrice = parcel.readString()
        maxAmount = parcel.readString()
        word =parcel.readBoolean()
    }



    constructor(id: String?, mygroup: String?,barcode: String?, decription: String?,unit: String?,image: String?
                ,barcodeDetail: List<BarcodeDetail>?,price: String?,min_price: String? ,max_price: String?, max_amount: String? ,word: Boolean) : this() {
        this.id = id
        this.mygroup = mygroup
        this.barcode = barcode
        this.decription = decription
        this.unit = unit
        this.image = image
        this.barcodeDetail = barcodeDetail
        this.price = price
        this.minPrice = min_price
        this.maxPrice = max_price
        this.maxAmount = max_amount
        this.word = word
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BarcodeData> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BarcodeData {
            return BarcodeData(parcel)
        }

        override fun newArray(size: Int): Array<BarcodeData?> {
            return arrayOfNulls(size)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeString(id)
        parcel.writeString(mygroup)
        parcel.writeString(barcode)
        parcel.writeString(decription)
        parcel.writeString(unit)
        parcel.writeString(image)
        parcel.writeTypedList(barcodeDetail)
        parcel.writeString(price)
        parcel.writeString(minPrice)
        parcel.writeString(maxPrice)
        parcel.writeString(maxAmount)
        parcel.writeBoolean(word)


    }

}