package com.rahbarbazaar.shopper.models.shopping_edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShoppingPrize {

    @SerializedName("shopping_prize_data")
    @Expose
    public List<ShoppingPrizeData> shoppingPrizeData = null;

}
