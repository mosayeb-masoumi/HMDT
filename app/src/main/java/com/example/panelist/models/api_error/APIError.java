package com.example.panelist.models.api_error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIError {

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("errors")
    @Expose
    public ErrorsMessage errors;


}
