package com.rahbarbazaar.checkpanel.models.edit_products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditProducts {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("discount_type")
    @Expose
    public String discountType;
    @SerializedName("cost")
    @Expose
    public String cost;
    @SerializedName("paid")
    @Expose
    public String paid;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("bought_member")
    @Expose
    public String boughtMember;
    @SerializedName("bought_prize")
    @Expose
    public String boughtPrize;

}
