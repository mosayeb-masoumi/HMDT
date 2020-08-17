package com.rahbarbazaar.homadit.android.models.shopping_memberprize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
//    @SerializedName("member")
//    @Expose
//    public List<List<Member>> member = null;
//    @SerializedName("prize")
//    @Expose
//    public List<List<Prize>> prize = null;
//    @SerializedName("categories")
//    @Expose
//    public String categories;

    @SerializedName("member")
    @Expose
    public List<List<Member>> member = null;
    @SerializedName("prize")
    @Expose
    public List<List<Object>> prize = null;
    @SerializedName("categories")
    @Expose
    public List<Category> categories = null;

    @SerializedName("help_pic_1")
    @Expose
    public String help_pic_1;
    @SerializedName("help_pic_2")
    @Expose
    public String help_pic_2;
}
