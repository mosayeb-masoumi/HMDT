package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastItemInteraction;

public class LotteryPastViewHolder extends RecyclerView.ViewHolder {

    LinearLayout ll_root;
    TextView txt_score,txt_date;

    public LotteryPastViewHolder(@NonNull View itemView) {
        super(itemView);
        ll_root = itemView.findViewById(R.id.ll_root_past_lottary_item);
        txt_date = itemView.findViewById(R.id.txt_date_past_lottery_row);
        txt_score = itemView.findViewById(R.id.txt_score_past_lottery_row);
    }

    public void bindData(String model) {


    }

    public void setOnLotteryPastHolderListener(LottaryPastItemInteraction listener, String model, int position) {

        ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.pastLottaryItemOnClicked(model ,position);
            }
        });
    }
}
