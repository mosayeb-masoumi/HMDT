package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryLinkItemInteraction;
import com.rahbarbazaar.homadit.android.models.Lottary.ActiveLinkDetail;

public class LottaryLinkViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView title;
    public LottaryLinkViewHolder(View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.cardview_link);
        title = itemView.findViewById(R.id.txt_link);
    }

    public void bindData(ActiveLinkDetail model) {

        title.setText(model.title);
    }

    public void setOnLotteryLinkHolderListener(LottaryLinkItemInteraction listener, ActiveLinkDetail model, int position) {

        cardView.setOnClickListener(view -> {

            listener.pastLottaryItemOnClicked(model , position);
        });
    }
}
