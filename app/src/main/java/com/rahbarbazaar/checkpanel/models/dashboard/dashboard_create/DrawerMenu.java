package com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create.DrawerItems;

import java.util.List;

public class DrawerMenu {
    @SerializedName("data")
    @Expose
    public List<DrawerItems> data = null;
}
