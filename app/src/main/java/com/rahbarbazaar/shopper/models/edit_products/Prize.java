package com.rahbarbazaar.shopper.models.edit_products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prize {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("id")
    @Expose
    public String id;
}
