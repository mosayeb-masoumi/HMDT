package com.rahbarbazaar.checkpanel.models.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("extra")
    @Expose
    public String extra;
    @SerializedName("origin")
    @Expose
    public String origin;
    @SerializedName("title")
    @Expose
    public String title;

}
