package com.example.panelist.models.shopping_edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shopping {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("cost")
    @Expose
    public Integer cost;
    @SerializedName("paid")
    @Expose
    public Integer paid;
    @SerializedName("discount_type")
    @Expose
    public String discountType;
    @SerializedName("discount_amount")
    @Expose
    public Integer discount_amount;
    @SerializedName("shop")
    @Expose
    public String shop;
    @SerializedName("date")
    @Expose
    public String date;

}
