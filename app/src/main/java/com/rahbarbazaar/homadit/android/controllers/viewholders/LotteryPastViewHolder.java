package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastItemInteraction;
import com.rahbarbazaar.homadit.android.models.Lottary.OldMeDetail;

public class LotteryPastViewHolder extends RecyclerView.ViewHolder {

    LinearLayout ll_root;
    TextView txt_score, txt_date;
    ImageView img;

    Gson gson = new Gson();

    public LotteryPastViewHolder(@NonNull View itemView) {
        super(itemView);
        ll_root = itemView.findViewById(R.id.ll_root_past_lottary_item);
        txt_date = itemView.findViewById(R.id.txt_date_past_lottery_row);
        txt_score = itemView.findViewById(R.id.txt_score_past_lottery_row);
        img = itemView.findViewById(R.id.img_info_lottary);
    }

    public void bindData(OldMeDetail model) {

        txt_date.setText("قرعه کشی " + model.title);
        txt_score.setText("امتیاز :" + model.amount);
        img.setBackground(itemView.getResources().getDrawable(R.drawable.info_blue));
        if (model.winner.equals("بله")) {
            img.setBackground(itemView.getResources().getDrawable(R.drawable.info_green));
            txt_date.setTextColor(itemView.getResources().getColor(R.color.green));
            txt_score.setTextColor(itemView.getResources().getColor(R.color.green));
        }
    }

    public void setOnLotteryPastHolderListener(LottaryPastItemInteraction listener, OldMeDetail model, int position) {
        ll_root.setOnClickListener(view -> listener.pastLottaryItemOnClicked(model, position));
    }
}