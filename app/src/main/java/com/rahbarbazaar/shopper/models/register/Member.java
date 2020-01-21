package com.rahbarbazaar.shopper.models.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Member {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public String id;


    public Member(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
