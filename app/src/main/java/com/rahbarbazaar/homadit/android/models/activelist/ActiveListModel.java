package com.rahbarbazaar.homadit.android.models.activelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveListModel {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("title")
    @Expose
    public String title;
}
