package com.rahbarbazaar.homadit.android.controllers.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rahbarbazaar.homadit.android.R;

public class LottaryPrizeViewHolder extends RecyclerView.ViewHolder {

    TextView txtPrize;

    public LottaryPrizeViewHolder(View itemView) {
        super(itemView);
        txtPrize  = itemView.findViewById(R.id.txt_prize);
    }

    public void bindData(String model) {
        txtPrize.setText(model);
    }
}
