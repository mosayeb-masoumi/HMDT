package com.rahbarbazaar.homadit.android.models.searchable;

public class SearchModel {

    private String title;
    private String id;


    public SearchModel(String title, String id) {
        this.title = title;
        this.id = id;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }
}
