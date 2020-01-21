package com.rahbarbazaar.shopper.models.shopping_memberprize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Member {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public String id;
}
