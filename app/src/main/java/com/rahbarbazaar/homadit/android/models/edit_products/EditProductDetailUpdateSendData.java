package com.rahbarbazaar.homadit.android.models.edit_products;

import com.rahbarbazaar.homadit.android.models.register.RegisterMemberEditModel;
import com.rahbarbazaar.homadit.android.models.register.SendPrize;

import java.util.ArrayList;
import java.util.List;

public class EditProductDetailUpdateSendData {

    private List<SendPrize> prize = new ArrayList<>();
    private List<RegisterMemberEditModel> member = new ArrayList<>();
    private String cost;
    private String discount_amount;
    private String discount_type;
    private String paid;
    private String bought_id;
    private String amount;

    public void setPrize(List<SendPrize> prize) {
        this.prize = prize;
    }

    public void setMember(List<RegisterMemberEditModel> member) {
        this.member = member;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public void setBought_id(String bought_id) {
        this.bought_id = bought_id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
