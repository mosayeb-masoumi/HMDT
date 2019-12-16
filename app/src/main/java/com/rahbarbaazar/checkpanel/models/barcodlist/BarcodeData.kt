package com.rahbarbaazar.checkpanel.models.barcodlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BarcodeData {
//    @SerializedName("id")
//    @Expose
//    var id: String? = null
//    @SerializedName("decription")
//    @Expose
//    var decription: String? = null
//
//    constructor(id: String?, decription: String?) {
//        this.id = id
//        this.decription = decription
//    }

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("decription")
    @Expose
    var decription: String? = null
    @SerializedName("sub_category")
    @Expose
    var subCategory: Any? = null
    @SerializedName("main")
    @Expose
    var main: String? = null
    @SerializedName("category")
    @Expose
    var category: String? = null
    @SerializedName("owner")
    @Expose
    var owner: String? = null
    @SerializedName("brand")
    @Expose
    var brand: String? = null
    @SerializedName("sub_brand")
    @Expose
    var subBrand: Any? = null
    @SerializedName("company")
    @Expose
    var company: String? = null
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("unit")
    @Expose
    var unit: String? = null
    @SerializedName("packaging")
    @Expose
    var packaging: String? = null
    @SerializedName("price")
    @Expose
    var price: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("amount")
    @Expose
    var amount: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null


    constructor(id: String?, decription: String?, subCategory: Any?, main: String?, category: String?, owner: String?,
                brand: String?, subBrand: Any?, company: String?, country: String?, unit: String?,
                packaging: String?, price: String?, type: String?, amount: String?, image: String?) {
        this.id = id
        this.decription = decription
        this.subCategory = subCategory
        this.main = main
        this.category = category
        this.owner = owner
        this.brand = brand
        this.subBrand = subBrand
        this.company = company
        this.country = country
        this.unit = unit
        this.packaging = packaging
        this.price = price
        this.type = type
        this.amount = amount
        this.image = image
    }
}