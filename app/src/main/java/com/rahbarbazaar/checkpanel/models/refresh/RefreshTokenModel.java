package com.rahbarbazaar.checkpanel.models.refresh;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefreshTokenModel {
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
