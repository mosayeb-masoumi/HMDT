package com.rahbarbazaar.shopper.models.activelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveListModel {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("title")
    @Expose
    public String title;


//    public ActiveListModel(String id, String date, String title) {
//        this.id = id;
//        this.date = date;
//        this.title = title;
//    }
}
