package com.rahbarbazaar.homadit.android.models.Lottary.create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LottaryCreateModel {
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
