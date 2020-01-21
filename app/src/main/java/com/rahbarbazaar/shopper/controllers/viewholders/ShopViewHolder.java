package com.rahbarbazaar.shopper.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.interfaces.ShopItemInteraction;
import com.rahbarbazaar.shopper.models.shop.ShopCenterModel;

public class ShopViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView txt_title;

    public ShopViewHolder(@NonNull View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.image_shop_item);
        txt_title = itemView.findViewById(R.id.text_shop_item);
    }

    public void bindData(ShopCenterModel model, int position) {
        txt_title.setText(model.title);
        Glide.with(itemView.getContext()).load(model.image).centerCrop().into(img);


    }

    public void setOnShopListHolderListener(ShopItemInteraction listener, ShopCenterModel model, int position) {

        itemView.setOnClickListener(v -> {
            if (model.status.equals("enable")) {
                listener.shopItemOnClicked(model, position);
            }
        });

    }
}
