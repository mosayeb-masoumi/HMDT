package com.rahbarbazaar.shopper.models.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shop {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("online")
    @Expose
    public String online;

}
