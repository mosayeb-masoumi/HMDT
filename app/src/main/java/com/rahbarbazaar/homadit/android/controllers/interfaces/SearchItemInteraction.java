package com.rahbarbazaar.homadit.android.controllers.interfaces;


import android.app.AlertDialog;

import com.rahbarbazaar.homadit.android.models.searchable.SearchModel;

public interface SearchItemInteraction {
    void searchListItemOnClick(SearchModel model, AlertDialog dialog, String spn_name);
}
