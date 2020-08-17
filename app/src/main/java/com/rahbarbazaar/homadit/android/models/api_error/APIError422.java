package com.rahbarbazaar.homadit.android.models.api_error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIError422 {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("errors")
    @Expose
    public ErrorsMessage422 errors;
}
