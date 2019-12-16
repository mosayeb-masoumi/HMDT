package com.rahbarbaazar.checkpanel.models.register;

import android.widget.CheckBox;

public class RegisterMemberModel {

    public String txt_name;
    public String id;
    public CheckBox checkBox;

    public RegisterMemberModel(String txt_name, String id, CheckBox checkBox) {
        this.txt_name = txt_name;
        this.id = id;
        this.checkBox = checkBox;
    }

    //    public RegisterMemberModel(String txt_name, String id) {
//        this.txt_name = txt_name;
//        this.id = id;
//    }
}
