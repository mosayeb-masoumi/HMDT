package com.rahbarbaazar.checkpanel.models.dashboard;

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
    @SerializedName("video_image")
    @Expose
    public String video_image;
    @SerializedName("video_content")
    @Expose
    public String video_content;
    @SerializedName("news_image")
    @Expose
    public String news_image;
    @SerializedName("news_content")
    @Expose
    public String news_content;
    @SerializedName("myshop_image")
    @Expose
    public String myshop_image;

}
