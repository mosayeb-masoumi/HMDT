package com.rahbarbaazar.checkpanel.models.register_newshop;

import com.rahbarbaazar.checkpanel.models.register.RegisterMemberEditModel;

import java.util.ArrayList;
import java.util.List;

public class NewShopSendData {

    List<RegisterMemberEditModel> memberEditedList = new ArrayList<>();

    public List<RegisterMemberEditModel> getMemberEditedList() {
        return memberEditedList;
    }

    public void setMemberEditedList(List<RegisterMemberEditModel> memberEditedList) {
        this.memberEditedList = memberEditedList;
    }
}
