package com.rahbarbazaar.homadit.android.models.register;

public class SendPrize {

    private String description;
    private String id;

    public SendPrize(String description, String id) {
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
