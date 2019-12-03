package com.example.panelist.models.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterData {
    @SerializedName("shops")
    @Expose
    public List<List<Shop>> shops = null;
    @SerializedName("members")
    @Expose
    public List<List<Member>> members = null;
}
