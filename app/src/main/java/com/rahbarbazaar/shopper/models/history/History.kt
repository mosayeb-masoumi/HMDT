package com.rahbarbazaar.shopper.models.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class History {


    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("discount")
    @Expose
    var discount: String? = null
    @SerializedName("discount_type")
    @Expose
    var discountType: String? = null
    @SerializedName("cost")
    @Expose
    var cost: String? = null
    @SerializedName("paid")
    @Expose
    var paid: String? = null
    @SerializedName("currency")
    @Expose
    var currency: String? = null
    @SerializedName("shopping_member")
    @Expose
    var shoppingMember: String? = null
    @SerializedName("shopping_prize")
    @Expose
    var shoppingPrize: String? = null
    @SerializedName("shopping_image1")
    @Expose
    var shoppingImage1: String? = null
    @SerializedName("shopping_image2")
    @Expose
    var shoppingImage2: String? = null
    @SerializedName("shopping_image3")
    @Expose
    var shoppingImage3: String? = null
    @SerializedName("shopping_image4")
    @Expose
    var shoppingImage4: String? = null
}