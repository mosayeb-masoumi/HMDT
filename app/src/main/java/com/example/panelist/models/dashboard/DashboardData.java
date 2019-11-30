package com.example.panelist.models.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardData {
    @SerializedName("one")
    @Expose
    public Integer one;
    @SerializedName("two")
    @Expose
    public Integer two;
    @SerializedName("three")
    @Expose
    public Integer three;
    @SerializedName("four")
    @Expose
    public Integer four;

}
