package com.rahbarbazaar.homadit.android.models.shopping_edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {


    @SerializedName("shopping")
    @Expose
    public Shopping shopping;
    @SerializedName("shopping_member")
    @Expose
    public ShoppingMember shoppingMember;
//    @SerializedName("shopping_prize")
//    @Expose
//    public ShoppingPrize shoppingPrize;
    @SerializedName("shop")
    @Expose
    public List<List<Shop>> shop = null;
//    @SerializedName("prize")
//    @Expose
//    public List<List<Prize>> prize = null;
    @SerializedName("member")
    @Expose
    public List<List<Member>> member = null;




    @SerializedName("shopping_prize")
    @Expose
    public List<Object> shoppingPrize = null;
    @SerializedName("prize")
    @Expose
    public List<Object> prize = null;

}
