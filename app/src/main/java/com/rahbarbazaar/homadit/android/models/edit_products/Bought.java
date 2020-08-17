package com.rahbarbazaar.homadit.android.models.edit_products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bought {
    @SerializedName("data")
    @Expose
    public List<EditProducts> data = null;
}
