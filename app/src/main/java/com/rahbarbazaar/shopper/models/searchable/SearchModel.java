package com.rahbarbazaar.shopper.models.searchable;

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
}
