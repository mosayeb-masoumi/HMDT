package com.rahbarbazaar.shopper.controllers.interfaces;


import android.app.AlertDialog;

import com.rahbarbazaar.shopper.models.searchable.SearchModel;

public interface SearchItemInteraction {
    void searchListItemOnClick(SearchModel model, AlertDialog dialog);
}
