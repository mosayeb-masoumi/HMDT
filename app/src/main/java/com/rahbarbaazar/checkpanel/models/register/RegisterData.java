package com.rahbarbaazar.checkpanel.models.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterData {

    @SerializedName("shop")
    @Expose
    public List<List<Shop>> shop = null;
    @SerializedName("member")
    @Expose
    public List<List<Member>> member = null;
    @SerializedName("prize")
    @Expose
    public List<List<Prize>> prize = null;
}
