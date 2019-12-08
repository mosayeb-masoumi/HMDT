package com.example.panelist.models.activelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActiveList {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("title")
    @Expose
    public String title;


    public ActiveList(String id, String date, String title) {
        this.id = id;
        this.date = date;
        this.title = title;
    }
}
