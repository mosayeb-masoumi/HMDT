package com.rahbarbazaar.shopper.models.barcodlist

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rahbarbazaar.shopper.models.search_goods.Groups

//
//
//class BarcodeData {
//
//    @SerializedName("id")
//    @Expose
//    var id: String? = null
//    @SerializedName("mygroup")
//    @Expose
//    var mygroup: String? = null
//    @SerializedName("barcode")
//    @Expose
//    var barcode: String? = null
//    @SerializedName("decription")
//    @Expose
//    var decription: String? = null
//
//    @SerializedName("unit")
//    @Expose
//    var unit: String? = null
//
//
//    @SerializedName("detail")
//    @Expose
//    var barcodeDetail: List<BarcodeDetail>? = null
//
//
//    constructor(id: String?, mygroup: String?, barcode: String?, decription: String?, unit: String?,
//                barcodeDetail: List<BarcodeDetail>?) {
//        this.id = id
//        this.mygroup = mygroup
//        this.barcode = barcode
//        this.decription = decription
//        this.unit = unit
//        this.barcodeDetail = barcodeDetail
//    }
//}

class BarcodeData() :Parcelable{

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


    @SerializedName("detail")
    @Expose
    var barcodeDetail: List<BarcodeDetail>? = null



    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        mygroup = parcel.readString()
        barcode = parcel.readString()
        decription = parcel.readString()
        unit = parcel.readString()

        barcodeDetail = parcel.createTypedArrayList(BarcodeDetail.CREATOR)
    }


        constructor(id: String?, mygroup: String?, barcode: String?, decription: String?, unit: String?,
                barcodeDetail: List<BarcodeDetail>?) : this() {
        this.id = id
        this.mygroup = mygroup
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
        parcel.writeString(barcode)
        parcel.writeString(decription)
        parcel.writeString(unit)

        parcel.writeTypedList(barcodeDetail)
    }




}