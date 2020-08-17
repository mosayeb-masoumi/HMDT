package com.rahbarbazaar.homadit.android.models.barcodlist

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BarcodeData() : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("mygroup")
    @Expose
    var mygroup: String? = null

    @SerializedName("min_price")
    @Expose
    var minPrice: Int? = null
    @SerializedName("max_price")
    @Expose
    var maxPrice: Int? = null
    @SerializedName("max_amount")
    @Expose
    var maxAmount: Int? = null

    @SerializedName("barcode")
    @Expose
    var barcode: String? = null
    @SerializedName("decription")
    @Expose
    var decription: String? = null

    @SerializedName("unit")
    @Expose
    var unit: String? = null


    @SerializedName("detail")
    @Expose
    var barcodeDetail: List<BarcodeDetail>? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        mygroup = parcel.readString()

        minPrice = parcel.readInt()
        maxPrice = parcel.readInt()
        maxAmount = parcel.readInt()


        barcode = parcel.readString()
        decription = parcel.readString()
        unit = parcel.readString()

        barcodeDetail = parcel.createTypedArrayList(BarcodeDetail.CREATOR)
    }

    constructor(id: String?, mygroup: String?, min_price: Int?, max_price: Int?, max_amount: Int?, barcode: String?, decription: String?, unit: String?,
                barcodeDetail: List<BarcodeDetail>?) : this() {
        this.id = id
        this.mygroup = mygroup
        this.minPrice = min_price
        this.maxPrice = max_price
        this.maxAmount = max_amount

        this.barcode = barcode
        this.decription = decription
        this.unit = unit
        this.barcodeDetail = barcodeDetail
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BarcodeData> {
        override fun createFromParcel(parcel: Parcel): BarcodeData {
            return BarcodeData(parcel)
        }

        override fun newArray(size: Int): Array<BarcodeData?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(mygroup)

        parcel.writeInt(this!!.minPrice!!)
        parcel.writeInt(this!!.maxPrice!!)
        parcel.writeInt(this!!.maxAmount!!)


        parcel.writeString(barcode)
        parcel.writeString(decription)
        parcel.writeString(unit)
        parcel.writeTypedList(barcodeDetail)
    }

}