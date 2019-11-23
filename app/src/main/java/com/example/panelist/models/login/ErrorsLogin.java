package com.example.panelist.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorsLogin {
    @SerializedName("mobile")
    @Expose
    public List<String> mobile = null;
}
