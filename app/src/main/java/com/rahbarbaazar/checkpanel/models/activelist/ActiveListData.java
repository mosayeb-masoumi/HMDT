package com.rahbarbaazar.checkpanel.models.activelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActiveListData {
    @SerializedName("data")
    @Expose
    public List<ActiveList> data = null;
}
