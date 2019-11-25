package com.example.panelist.models.api_error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorsMessage {
    @SerializedName("mobile")
    @Expose
    public List<String> mobile = null;

    @SerializedName("code")
    @Expose
    public List<String> code = null;

}
