package com.rahbarbazaar.homadit.android.models.Lottary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("active")
    @Expose
    public Active active;
    @SerializedName("active_me")
    @Expose
    public ActiveMe activeMe;
    @SerializedName("active_link")
    @Expose
    public ActiveLink activeLink;
    @SerializedName("old_me")
    @Expose
    public OldMe oldMe;
}
