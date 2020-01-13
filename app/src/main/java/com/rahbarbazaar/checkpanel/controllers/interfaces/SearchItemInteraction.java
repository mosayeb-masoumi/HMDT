package com.rahbarbazaar.checkpanel.controllers.interfaces;


import android.app.AlertDialog;

import com.rahbarbazaar.checkpanel.models.searchable.SearchModel;

public interface SearchItemInteraction {
    void searchListItemOnClick(SearchModel model, AlertDialog dialog);
}
