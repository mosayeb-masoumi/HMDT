package com.rahbarbazaar.homadit.android.models.purchased_item;

import com.rahbarbazaar.homadit.android.models.register.RegisterMemberEditModel;

import java.util.ArrayList;
import java.util.List;

public class SendPurchasedItemNewData {

    private String shopping_id;
    private String barcode;
    private String category;
    private String brand;
    private String one_title;
    private String one_data;
    private String two_title;
    private String two_data;
    private String description;
    private String cost;
    private String amount;
    private List<RegisterMemberEditModel> member = new ArrayList<>();
    private String image_1;
    private String image_2;
    private String image_3;
    private String image_4;


    public String getShopping_id() {
        return shopping_id;
    }

    public void setShopping_id(String shopping_id) {
        this.shopping_id = shopping_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOne_title() {
        return one_title;
    }

    public void setOne_title(String one_title) {
        this.one_title = one_title;
    }

    public String getOne_data() {
        return one_data;
    }

    public void setOne_data(String one_data) {
        this.one_data = one_data;
    }

    public String getTwo_title() {
        return two_title;
    }

    public void setTwo_title(String two_title) {
        this.two_title = two_title;
    }

    public String getTwo_data() {
        return two_data;
    }

    public void setTwo_data(String two_data) {
        this.two_data = two_data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<RegisterMemberEditModel> getMember() {
        return member;
    }

    public void setMember(List<RegisterMemberEditModel> member) {
        this.member = member;
    }

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }

    public String getImage_3() {
        return image_3;
    }

    public void setImage_3(String image_3) {
        this.image_3 = image_3;
    }

    public String getImage_4() {
        return image_4;
    }

    public void setImage_4(String image_4) {
        this.image_4 = image_4;
    }
}
