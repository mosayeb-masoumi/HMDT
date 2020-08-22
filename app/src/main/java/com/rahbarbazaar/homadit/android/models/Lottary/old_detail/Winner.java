package com.rahbarbazaar.homadit.android.models.Lottary.old_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Winner {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("prize")
    @Expose
    public String prize;
}
