package com.rahbarbazaar.shopper.models.edit_products;

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
    @SerializedName("unit")
    @Expose
    public String unit;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("discount_type")
    @Expose
    public String discount_type;
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
    @SerializedName("bought_member_data")
    @Expose
    public BoughtMemberData boughtMemberData;
    @SerializedName("bought_prize")
    @Expose
    public String boughtPrize;
    @SerializedName("bought_prize_data")
    @Expose
    public BoughtPrizeData boughtPrizeData;
}
