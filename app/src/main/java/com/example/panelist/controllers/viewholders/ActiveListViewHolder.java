package com.example.panelist.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.panelist.R;
import com.example.panelist.models.activelist.ActiveList;

public class ActiveListViewHolder extends RecyclerView.ViewHolder {

    TextView txt_title;

    public ActiveListViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_title = itemView.findViewById(R.id.txt_title_activelist);
    }

    public void bindData(ActiveList model, int position) {

//        txt_title.setText(model.data.get(position).title);
        txt_title.setText(model.id);
    }

    public void setOnActiveListHolderListener(ActiveListItemInteraction listener, ActiveList model, int position) {
    }
}
