package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.GroupGoodsItemInteraction;
import com.rahbarbazaar.homadit.android.controllers.interfaces.SearchItemInteraction;
import com.rahbarbazaar.homadit.android.models.group_goods.GroupGoodsModel;
import com.rahbarbazaar.homadit.android.models.searchable.SearchModel;
import com.rahbarbazaar.homadit.android.ui.activities.PurchasedItemActivity;

public class GroupGoodsViewHolder extends RecyclerView.ViewHolder {

    private TextView txt_title;
    RelativeLayout root_row_group_goods;
    ImageView img;
//    ImageView img_untik;

    public GroupGoodsViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_title = itemView.findViewById(R.id.txt_tilte_search_item);
        root_row_group_goods = itemView.findViewById(R.id.root_row_group_goods);
        img = itemView.findViewById(R.id.img_search_item);
//        img_untik = itemView.findViewById(R.id.img_untik);
    }

    public void bindData(GroupGoodsModel model) {
        txt_title.setText(model.getTitle());

        Glide
                .with(itemView.getContext())
                .load(model.getIcon())
                .centerCrop()
                .placeholder(R.color.blue)
                .into(img);


//        img_untik.setBackground(itemView.getResources().getDrawable(R.drawable.untik));
    }

    public void setOnGroupGoodsHolderListener(GroupGoodsItemInteraction listener, GroupGoodsModel model) {

        root_row_group_goods.setOnClickListener(view -> {
            listener.groupGoodsListItemOnClick(model);
//            img_untik.setBackground(itemView.getResources().getDrawable(R.drawable.tik));
        });

    }
}
