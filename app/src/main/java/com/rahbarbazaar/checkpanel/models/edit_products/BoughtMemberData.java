package com.rahbarbazaar.checkpanel.models.edit_products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoughtMemberData {
    @SerializedName("data")
    @Expose
    public List<BoughtMember> data = null;
}
