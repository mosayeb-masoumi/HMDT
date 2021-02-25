package com.rahbarbazaar.homadit.android.controllers.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.DrawerItemClicked;
import com.rahbarbazaar.homadit.android.models.dashboard.dashboard_create.DrawerItems;

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
