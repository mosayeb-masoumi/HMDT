package com.rahbarbazaar.checkpanel.models.edit_products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditProductsData {

    @SerializedName("total")
    @Expose
    public Integer total;
    @SerializedName("bought")
    @Expose
    public Bought bought;
    @SerializedName("member")
    @Expose
    public List<List<Member>> member = null;
    @SerializedName("prize")
    @Expose
    public List<List<Prize>> prize = null;
}
