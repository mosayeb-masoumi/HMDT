package com.rahbarbazaar.homadit.android.models.dashboard.dashboard_create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rahbarbazaar.homadit.android.models.shop.ShopCenter;

public class DashboardCreateModel {

    @SerializedName("one")
    @Expose
    public String one;
    @SerializedName("min_version_code")
    @Expose
    public String minVersionCode;
    @SerializedName("current_version_code")
    @Expose
    public String currentVersionCode;
    @SerializedName("two")
    @Expose
    public String two;
//    @SerializedName("three")
//    @Expose
//    public String three;
    @SerializedName("four")
    @Expose
    public String four;


    @SerializedName("about")
    @Expose
    public String about;


    @SerializedName("board")
    @Expose
    public String board;

    @SerializedName("board_title")
    @Expose
    public String board_title;

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

    @SerializedName("education_page")
    @Expose
    public String education_page;

    @SerializedName("agreement_page")
    @Expose
    public String agreementPage;

    @SerializedName("address_page")
    @Expose
    public String addressPage;

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
