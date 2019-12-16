package com.rahbarbaazar.checkpanel.models.shopping_edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShoppingMember {
    @SerializedName("shopping_member_data")
    @Expose
    public List<ShoppingMemberData> shoppingMemberData = null;
}
