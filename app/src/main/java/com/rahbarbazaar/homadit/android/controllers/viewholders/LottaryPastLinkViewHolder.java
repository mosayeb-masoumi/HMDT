package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastLinkIteminteraction;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.Link;

public class LottaryPastLinkViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView title;

    public LottaryPastLinkViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.txt_link);
        cardView = itemView.findViewById(R.id.cardview_link);
    }

    public void bindData(Link model) {
        title.setText(model.title);
    }

    public void setOnLotteryPastLinkHolderListener(LottaryPastLinkIteminteraction listener, Link model, int position) {

        cardView.setOnClickListener(view -> listener.pastLottaryLinkItemOnClicked(model, position));
    }
}
