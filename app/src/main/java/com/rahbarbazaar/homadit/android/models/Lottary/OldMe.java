package com.rahbarbazaar.homadit.android.models.Lottary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OldMe {
    @SerializedName("data")
    @Expose
    public List<OldMeDetail> data = null;
}
