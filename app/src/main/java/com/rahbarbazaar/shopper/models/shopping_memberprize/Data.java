package com.rahbarbazaar.shopper.models.shopping_memberprize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("member")
    @Expose
    public List<List<Member>> member = null;
    @SerializedName("prize")
    @Expose
    public List<List<Prize>> prize = null;
    @SerializedName("categories")
    @Expose
    public String categories;
}
