package com.rahbarbazaar.shopper.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.interfaces.DrawerItemClicked;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_create.DrawerItems;

public class DrawerViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView title;
    public DrawerViewHolder(@NonNull View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.image_drawer);
        title = itemView.findViewById(R.id.text_drawer);
    }

    public void bindData(DrawerItems model) {
        title.setText(model.title);
        Glide.with(itemView.getContext()).load(model.image).centerCrop().into(img);
    }

    public void setOnPrizeHolderListener(DrawerItemClicked listener, DrawerItems model) {

        itemView.setOnClickListener(v -> listener.onDrawerItemClicked(model.content));
    }
}
