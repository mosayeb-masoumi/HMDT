package com.rahbarbazaar.homadit.android.models.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ShopCenterModel {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("url")
    @Expose
    public String url;
}
