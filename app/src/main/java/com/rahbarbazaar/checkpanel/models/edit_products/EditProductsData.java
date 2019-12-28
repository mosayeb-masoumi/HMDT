package com.rahbarbazaar.checkpanel.models.edit_products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditProductsData {

    @SerializedName("total")
    @Expose
    public Integer total;
    @SerializedName("data")
    @Expose
    public List<EditProducts> data = null;
}
