package com.example.panelist.models.register;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SendRegisterTotalData {

//    @SerializedName("sendPrizes")
    List<SendPrize> prize = new ArrayList<>();
//    @SerializedName("member")
    List<RegisterMemberEditModel> member = new ArrayList<>();

    private String shop_id;
    private String cost;
    private String paid;
    private String discount_amount;
    private String discount_type;
    private String date;


    public List<SendPrize> getPrize() {
        return prize;
    }

    public void setPrize(List<SendPrize> prize) {
        this.prize = prize;
    }

    public List<RegisterMemberEditModel> getMember() {
        return member;
    }

    public void setMember(List<RegisterMemberEditModel> member) {
        this.member = member;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
