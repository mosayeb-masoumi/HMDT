package com.rahbarbazaar.checkpanel.models.barcodlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BarcodeData {


    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("mygroup")
    @Expose
    var mygroup: String? = null
    @SerializedName("decription")
    @Expose
    var decription: String? = null
    @SerializedName("sub_category")
    @Expose
    var subCategory: String? = null
    @SerializedName("main")
    @Expose
    var main: String? = null
    @SerializedName("category")
    @Expose
    var category: String? = null
    @SerializedName("show")
    @Expose
    var show: String? = null
    @SerializedName("owner")
    @Expose
    var owner: String? = null
    @SerializedName("brand")
    @Expose
    var brand: String? = null
    @SerializedName("sub_brand")
    @Expose
    var subBrand: String? = null
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

    constructor(id: String?, mygroup: String?, decription: String?, subCategory: String?, main: String?,
                category: String?, show: String?, owner: String?, brand: String?, subBrand: String?,
                company: String?, country: String?, unit: String?, packaging: String?, price: String?,
                type: String?, amount: String?, image: String?) {
        this.id = id
        this.mygroup = mygroup
        this.decription = decription
        this.subCategory = subCategory
        this.main = main
        this.category = category
        this.show = show
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

    //    @SerializedName("id")
//    @Expose
//    var id: String? = null
//    @SerializedName("mygroup")
//    @Expose
//    var mygroup: String? = null
//    @SerializedName("decription")
//    @Expose
//    var decription: String? = null
//    @SerializedName("sub_category")
//    @Expose
//    var subCategory: String? = null
//    @SerializedName("main")
//    @Expose
//    var main: String? = null
//    @SerializedName("category")
//    @Expose
//    var category: String? = null
//    @SerializedName("owner")
//
//    @Expose
//    var show: String? = null
//    @SerializedName("show")
//
//    @Expose
//    var owner: String? = null
//    @SerializedName("brand")
//    @Expose
//    var brand: String? = null
//    @SerializedName("sub_brand")
//    @Expose
//    var subBrand: String? = null
//    @SerializedName("company")
//    @Expose
//    var company: String? = null
//    @SerializedName("country")
//    @Expose
//    var country: String? = null
//    @SerializedName("unit")
//    @Expose
//    var unit: String? = null
//    @SerializedName("packaging")
//    @Expose
//    var packaging: String? = null
//    @SerializedName("price")
//    @Expose
//    var price: String? = null
//    @SerializedName("type")
//    @Expose
//    var type: String? = null
//    @SerializedName("amount")
//    @Expose
//    var amount: String? = null
//    @SerializedName("image")
//    @Expose
//    var image: String? = null
//
//    constructor(id: String?, mygroup: String?, decription: String?, subCategory: String?, main: String?,
//                category: String?, show: String?, owner: String?, brand: String?, subBrand: String?,
//                company: String?, country: String?, unit: String?, packaging: String?, price: String?,
//                type: String?, amount: String?, image: String?) {
//        this.id = id
//        this.mygroup = mygroup
//        this.decription = decription
//        this.subCategory = subCategory
//        this.main = main
//        this.category = category
//        this.show = show
//        this.owner = owner
//        this.brand = brand
//        this.subBrand = subBrand
//        this.company = company
//        this.country = country
//        this.unit = unit
//        this.packaging = packaging
//        this.price = price
//        this.type = type
//        this.amount = amount
//        this.image = image
//    }
}