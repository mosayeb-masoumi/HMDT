package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.GroupGoodsItemInteraction;
import com.rahbarbazaar.homadit.android.controllers.interfaces.SearchItemInteraction;
import com.rahbarbazaar.homadit.android.models.group_goods.GroupGoodsModel;
import com.rahbarbazaar.homadit.android.models.searchable.SearchModel;
import com.rahbarbazaar.homadit.android.ui.activities.PurchasedItemActivity;

public class GroupGoodsViewHolder extends RecyclerView.ViewHolder {

    private TextView txt_title;
    private CheckBox checkbox;
    public GroupGoodsViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_title = itemView.findViewById(R.id.txt_tilte_search_item);
        checkbox = itemView.findViewById(R.id.checkbox);
    }

    public void bindData(GroupGoodsModel model) {
        txt_title.setText(model.getTitle());
    }

    public void setOnGroupGoodsHolderListener(GroupGoodsItemInteraction listener, GroupGoodsModel model) {


        checkbox.setOnCheckedChangeListener((compoundButton, b) -> {

            if(b){
                model.setChecked(true);
                listener.groupGoodsListItemOnClick(model);
            }else{
                model.setChecked(false);
            }
        });


    }
}
