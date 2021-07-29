package com.rahbarbazaar.homadit.android.models.video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoDetail {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("video_url")
    @Expose
    public String videoUrl;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
}
