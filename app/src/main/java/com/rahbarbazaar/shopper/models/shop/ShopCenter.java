package com.rahbarbazaar.shopper.models.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopCenter {
    @SerializedName("data")
    @Expose
    public List<ShopCenterModel> data = null;

}
