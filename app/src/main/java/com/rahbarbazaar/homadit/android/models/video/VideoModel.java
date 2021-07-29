package com.rahbarbazaar.homadit.android.models.video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoModel {

    @SerializedName("data")
    @Expose
    public List<VideoDetail> videoList = null;

}
