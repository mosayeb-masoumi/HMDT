package com.rahbarbazaar.shopper.models.purchased_item;

import com.rahbarbazaar.shopper.models.register.RegisterMemberEditModel;
import com.rahbarbazaar.shopper.models.register.SendPrize;

import java.util.ArrayList;
import java.util.List;

public class SendPurchasedItemData {

    private List<SendPrize> prize = new ArrayList<>();
    private List<RegisterMemberEditModel> member = new ArrayList<>();
    private String shopping_id;
    private String cost;
    private String discount_amount;
    private String discount_type;
    private String paid;
    private String amount;
    private String product_id;
    private String type;

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

    public String getShopping_id() {
        return shopping_id;
    }

    public void setShopping_id(String shopping_id) {
        this.shopping_id = shopping_id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
