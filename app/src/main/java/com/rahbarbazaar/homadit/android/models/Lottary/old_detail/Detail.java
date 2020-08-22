package com.rahbarbazaar.homadit.android.models.Lottary.old_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("event_date")
    @Expose
    public String eventDate;
    @SerializedName("minimum")
    @Expose
    public String minimum;
    @SerializedName("description")
    @Expose
    public String description;
}
