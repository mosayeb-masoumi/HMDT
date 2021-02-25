package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.viewholders.LottaryPastWinnerViewHolder;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.Winner;

import java.util.List;

public class LottaryPastWinnerAdapter extends RecyclerView.Adapter<LottaryPastWinnerViewHolder> {

    List<Winner> winnerList;
    Context context;

    public LottaryPastWinnerAdapter(List<Winner> winnerList, Context context) {
        this.winnerList = winnerList;
        this.context = context;
    }

    @NonNull
    @Override
    public LottaryPastWinnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lottary_pastwinner_detail, parent, false);
        return new LottaryPastWinnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LottaryPastWinnerViewHolder holder, int position) {
        Winner model = winnerList.get(position);
        holder.bindData(model);
    }

    @Override
    public int getItemCount() {
        return winnerList.size();
    }
}
