package com.example.panelist.models.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterModel {
    @SerializedName("data")
    @Expose
    public RegisterData data;
}
