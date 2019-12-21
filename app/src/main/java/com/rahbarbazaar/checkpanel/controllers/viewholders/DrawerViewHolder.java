package com.rahbarbazaar.checkpanel.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.controllers.interfaces.DrawerItemClicked;
import com.rahbarbazaar.checkpanel.models.dashboard.DrawerItems;

public class DrawerViewHolder extends RecyclerView.ViewHolder {

    ImageView img;
    TextView title;
    public DrawerViewHolder(@NonNull View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.image_drawer);
        title = itemView.findViewById(R.id.text_drawer);
    }

    public void bindData(DrawerItems model) {
        title.setText(model.title);
    }

    public void setOnPrizeHolderListener(DrawerItemClicked listener, DrawerItems model) {
    }
}
