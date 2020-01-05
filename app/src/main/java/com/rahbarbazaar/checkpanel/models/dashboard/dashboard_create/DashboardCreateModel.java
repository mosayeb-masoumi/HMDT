package com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rahbarbazaar.checkpanel.models.shop.ShopCenter;
import com.rahbarbazaar.checkpanel.models.shop.ShopCenterModel;

public class DashboardCreateModel {

    @SerializedName("one")
    @Expose
    public Integer one;
    @SerializedName("two")
    @Expose
    public Integer two;
    @SerializedName("three")
    @Expose
    public Integer three;
    @SerializedName("four")
    @Expose
    public Integer four;
    @SerializedName("video_image")
    @Expose
    public String video_image;
    @SerializedName("video_content")
    @Expose
    public String video_content;
    @SerializedName("news_image")
    @Expose
    public String news_image;
    @SerializedName("news_content")
    @Expose
    public String news_content;
    @SerializedName("myshop_image")
    @Expose
    public String myshop_image;
    @SerializedName("drawer_menu")
    @Expose
    public DrawerMenu drawerMenu;
    @SerializedName("faq_page")
    @Expose
    public String faqPage;
    @SerializedName("agreement_page")
    @Expose
    public String agreementPage;
    @SerializedName("shop_center")
    @Expose
    public ShopCenter shopCenter;
    @SerializedName("share_url")
    @Expose
    public String shareUrl;
    @SerializedName("update_url")
    @Expose
    public String updateUrl;
    @SerializedName("user_name")
    @Expose
    public String userName;

}
