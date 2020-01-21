package com.rahbarbazaar.shopper.models.shopping_edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShoppingPrizeData {

    @SerializedName("prize")
    @Expose
    public String prize;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("prize_type_id")
    @Expose
    public String prizeTypeId;
    @SerializedName("prize_type")
    @Expose
    public String prizeType;
}
