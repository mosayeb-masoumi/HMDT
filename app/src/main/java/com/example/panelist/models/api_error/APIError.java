package com.example.panelist.models.api_error;

import com.example.panelist.models.login.ErrorsLogin;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIError {

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("errors")
    @Expose
    public ErrorsLogin errors;

}
