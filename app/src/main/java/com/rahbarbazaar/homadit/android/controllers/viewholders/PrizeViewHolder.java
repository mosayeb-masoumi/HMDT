package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.PrizeItemInteraction;
import com.rahbarbazaar.homadit.android.models.register.Prize;


public class PrizeViewHolder extends RecyclerView.ViewHolder {

    private TextView txt_title;
    private CheckBox checkbox;

    public PrizeViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_title = itemView.findViewById(R.id.txt_name);
        checkbox = itemView.findViewById(R.id.checkbox);
    }

    public void bindData(Prize model) {
        txt_title.setText(model.title);
    }

    public void setOnPrizeHolderListener(PrizeItemInteraction listener, Prize model) {
        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(checkbox.isChecked()){
                checkbox.setChecked(true);
                listener.prizeOnClicked(model.title,model.id,true);
            }else if(!checkbox.isChecked()){
                checkbox.setChecked(false);
                listener.prizeOnClicked(model.title,model.id,false);
            }
        });
    }
}
