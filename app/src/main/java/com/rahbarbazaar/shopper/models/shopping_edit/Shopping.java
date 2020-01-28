package com.rahbarbazaar.shopper.models.shopping_edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shopping {


    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("shop")
    @Expose
    public String shop;
    @SerializedName("shop_id")
    @Expose
    public String shop_id;
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
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("image_1")
    @Expose
    public String image_1;
    @SerializedName("image_2")
    @Expose
    public String image_2;
    @SerializedName("image_3")
    @Expose
    public String image_3;
    @SerializedName("image_4")
    @Expose
    public String image_4;



}
