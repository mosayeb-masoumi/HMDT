package com.rahbarbazaar.homadit.android.models.api_error;

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

    @SerializedName("size")
    @Expose
    public List<String> size = null;

    @SerializedName("brand")
    @Expose
    public List<String> brand = null;

    @SerializedName("unit")
    @Expose
    public List<String> unit = null;

    @SerializedName("barcode")
    @Expose
    public List<String> barcode = null;

    @SerializedName("description")
    @Expose
    public List<String> description = null;

    @SerializedName("brand_id")
    @Expose
    public List<String> brand_id = null;

    @SerializedName("category_id")
    @Expose
    public List<String> category_id = null;

    @SerializedName("sub_category_id")
    @Expose
    public List<String> sub_category_id = null;

    @SerializedName("group_id")
    @Expose
    public List<String> group_id = null;

    @SerializedName("category")
    @Expose
    public List<String> category = null;

}
