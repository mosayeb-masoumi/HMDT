package com.rahbarbazaar.shopper.controllers.interfaces;


import android.app.Dialog;

public interface RegisterItemInteraction {
    void onClicked(String name, String id, String spn_name, Dialog dialog, Boolean chkbox);
}
