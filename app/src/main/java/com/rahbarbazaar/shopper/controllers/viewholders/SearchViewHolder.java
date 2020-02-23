package com.rahbarbazaar.shopper.controllers.viewholders;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.interfaces.SearchItemInteraction;
import com.rahbarbazaar.shopper.models.searchable.SearchModel;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    private TextView txt_title;
    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_title = itemView.findViewById(R.id.txt_tilte_search_item);
    }

    public void bindData(SearchModel model) {
        txt_title.setText(model.getTitle());
    }


    public void setOnPrizeHolderListener(SearchItemInteraction listener, SearchModel model, AlertDialog dialog, String spn_name) {
        itemView.setOnClickListener(v ->
                listener.searchListItemOnClick(model , dialog,spn_name));
    }
}
