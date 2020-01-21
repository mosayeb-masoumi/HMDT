package com.rahbarbazaar.shopper.models.activelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActiveListData {
//    @SerializedName("data")
//    @Expose
//    public List<ActiveListModel> data = null;

    @SerializedName("total")
    @Expose
    public Integer total;
    @SerializedName("data")
    @Expose
    public List<ActiveListModel> data = null;
}
