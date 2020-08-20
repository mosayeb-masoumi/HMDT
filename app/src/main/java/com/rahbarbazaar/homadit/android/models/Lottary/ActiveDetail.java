package com.rahbarbazaar.homadit.android.models.Lottary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActiveDetail {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("start")
    @Expose
    public Integer start;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("end")
    @Expose
    public Integer end;
    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("event")
    @Expose
    public Integer event;
    @SerializedName("event_date")
    @Expose
    public String eventDate;
    @SerializedName("minimum")
    @Expose
    public String minimum;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("prize")
    @Expose
    public List<String> prize = null;
}
