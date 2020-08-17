package com.rahbarbazaar.homadit.android.models.shopping_memberprize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("sequence")
    @Expose
    public Integer sequence;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
}
