package com.rahbarbazaar.homadit.android.models.Lottary.old_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("detail")
    @Expose
    public Detail_list detail;
    @SerializedName("prize")
    @Expose
    public Prize_list prizeList;
    @SerializedName("winner")
    @Expose
    public Winner_list winner;
    @SerializedName("link")
    @Expose
    public Link_list link;
}
