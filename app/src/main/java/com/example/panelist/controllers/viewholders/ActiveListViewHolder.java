package com.example.panelist.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.panelist.R;
import com.example.panelist.models.activelist.ActiveList;
import com.example.panelist.utilities.ConvertEnDigitToFa;

public class ActiveListViewHolder extends RecyclerView.ViewHolder {

    private TextView txt_title,txt_date;
    private ImageView img_edit_shop,img_register,img_edit_product;

    public ActiveListViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_title = itemView.findViewById(R.id.txt_title_activelist);
        txt_date=itemView.findViewById(R.id.txt_date_activelist);
        img_edit_shop=itemView.findViewById(R.id.img_edit_shop);
        img_register=itemView.findViewById(R.id.img_register);
        img_edit_product=itemView.findViewById(R.id.img_edit_product);
    }

    public void bindData(ActiveList model, int position) {

        txt_title.setText(model.title);
        String year =model.date.substring(0,4);
        String month =model.date.substring(5,7);
        String day =model.date.substring(8,10);
        String date = year+"/"+month+"/"+day;

        String convert = ConvertEnDigitToFa.convert(date);
        txt_date.setText(convert);
    }

    public void setOnActiveListHolderListener(ActiveListItemInteraction listener, ActiveList model, int position) {

        img_edit_shop.setOnClickListener(v -> listener.activeListOnClicked(model.title , model.id,"edit_shop"));
        img_register.setOnClickListener(v -> listener.activeListOnClicked(model.title , model.id,"register"));
        img_edit_product.setOnClickListener(v -> listener.activeListOnClicked(model.title , model.id,"edit_product"));
    }
}
