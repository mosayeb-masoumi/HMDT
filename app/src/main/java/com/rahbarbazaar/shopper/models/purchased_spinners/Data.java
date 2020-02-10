package com.rahbarbazaar.shopper.models.purchased_spinners;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("brand")
    @Expose
    public List<Brand> brand = null;
    @SerializedName("one_title")
    @Expose
    public String oneTitle;
    @SerializedName("one_data")
    @Expose
    public List<StatusOne> oneData = null;
    @SerializedName("two_title")
    @Expose
    public String twoTitle;
    @SerializedName("two_data")
    @Expose
    public List<StatusTwo> twoData = null;

}
