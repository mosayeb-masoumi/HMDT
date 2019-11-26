package com.example.panelist.models.api_error403;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIError403 {
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("action")
    @Expose
    public String action;
}
