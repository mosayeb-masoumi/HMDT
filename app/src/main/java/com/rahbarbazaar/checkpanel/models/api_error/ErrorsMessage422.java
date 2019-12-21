package com.rahbarbazaar.checkpanel.models.api_error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorsMessage422 {

    //login activity
    @SerializedName("mobile")
    @Expose
    public List<String> mobile = null;

    //verification activity
    @SerializedName("code")
    @Expose
    public List<String> code = null;

    // register acitivty
    @SerializedName("cost")
    @Expose
    public List<String> cost = null;
    @SerializedName("paid")
    @Expose
    public List<String> paid = null;
    @SerializedName("discount_amount")
    @Expose
    public List<String> discountAmount = null;
    @SerializedName("shop_id")
    @Expose
    public List<String> shopId = null;
    @SerializedName("member")
    @Expose
    public List<String> member = null;
    @SerializedName("date")
    @Expose
    public List<String> date = null;

    @SerializedName("prize")
    @Expose
    public List<String> prize = null;

    @SerializedName("amount")
    @Expose
    public List<String> amount = null;

    @SerializedName("type")
    @Expose
    public List<String> type = null;

}
