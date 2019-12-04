package com.example.panelist.models.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prize {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("id")
    @Expose
    public String id;
}
