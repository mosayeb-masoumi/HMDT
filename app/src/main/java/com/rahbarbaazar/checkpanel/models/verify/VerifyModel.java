package com.rahbarbaazar.checkpanel.models.verify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyModel {

    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("refresh_token")
    @Expose
    public String refreshToken;
    @SerializedName("expire_at")
    @Expose
    public Integer expireAt;
}
