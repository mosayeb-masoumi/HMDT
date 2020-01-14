package com.rahbarbazaar.checkpanel.models.shopping_edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shopping {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("cost")
    @Expose
    public String cost;
    @SerializedName("paid")
    @Expose
    public String paid;
    @SerializedName("discount_type")
    @Expose
    public String discount_type;
    @SerializedName("discount_amount")
    @Expose
    public String discount_amount;
    @SerializedName("shop")
    @Expose
    public String shop;
    @SerializedName("shop_id")
    @Expose
    public String shop_id;
    @SerializedName("date")
    @Expose
    public String date;

}
