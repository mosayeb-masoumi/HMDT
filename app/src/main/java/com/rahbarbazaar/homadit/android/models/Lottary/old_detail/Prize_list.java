package com.rahbarbazaar.homadit.android.models.Lottary.old_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Prize_list {
    @SerializedName("data")
    @Expose
    public List<Prize> data = null;
}
