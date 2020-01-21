package com.rahbarbazaar.shopper.models.dashboard.dashboard_update;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardUpdateModel {

    @SerializedName("unread")
    @Expose
    private Integer unread;

    public Integer getUnread() {
        return unread;
    }
}
