package com.rahbarbazaar.homadit.android.models.Lottary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveLinkDetail {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("link")
    @Expose
    public String link;
}
