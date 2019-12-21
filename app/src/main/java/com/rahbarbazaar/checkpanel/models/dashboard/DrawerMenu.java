package com.rahbarbazaar.checkpanel.models.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrawerMenu {
    @SerializedName("data")
    @Expose
    public List<DrawerItems> data = null;
}
