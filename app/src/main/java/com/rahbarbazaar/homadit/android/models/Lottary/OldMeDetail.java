package com.rahbarbazaar.homadit.android.models.Lottary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OldMeDetail {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("winner")
    @Expose
    public String winner;
}
