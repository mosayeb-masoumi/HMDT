package com.rahbarbazaar.homadit.android.controllers.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.Winner;

public class LottaryPastWinnerViewHolder extends RecyclerView.ViewHolder {

    TextView txtName,txt_prize;

    public LottaryPastWinnerViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.txt_winner_name);
        txt_prize = itemView.findViewById(R.id.txt_winner_prize);
    }

    public void bindData(Winner model) {
        txtName.setText(model.name);
        txt_prize.setText(model.prize);
    }


}
