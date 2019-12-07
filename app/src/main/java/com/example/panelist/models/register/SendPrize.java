package com.example.panelist.models.register;

public class SendPrize {

    private String descrition;
    private String id;

    public SendPrize(String descrition, String id) {
        this.descrition = descrition;
        this.id = id;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
