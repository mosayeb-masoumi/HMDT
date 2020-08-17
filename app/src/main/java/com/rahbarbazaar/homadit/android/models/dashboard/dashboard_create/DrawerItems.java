package com.rahbarbazaar.homadit.android.models.dashboard.dashboard_create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrawerItems {

    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("image")
    @Expose
    public String image;
}
