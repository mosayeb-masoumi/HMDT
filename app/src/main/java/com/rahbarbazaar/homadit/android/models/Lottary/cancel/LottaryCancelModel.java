package com.rahbarbazaar.homadit.android.models.Lottary.cancel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LottaryCancelModel {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("current")
    @Expose
    public Integer current;
    @SerializedName("maximum")
    @Expose
    public Integer maximum;
}
