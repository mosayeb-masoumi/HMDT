package com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardCreateModel {
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

}
