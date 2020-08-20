package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.viewholders.LottaryPrizeViewHolder;

import java.util.List;

public class LottaryPrizeAdapter extends RecyclerView.Adapter<LottaryPrizeViewHolder> {

    List<String> prizeList;
    Context context;

    public LottaryPrizeAdapter(List<String> prizeList, Context context) {
        this.prizeList = prizeList;
        this.context = context;
    }

    @NonNull
    @Override
    public LottaryPrizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lottary_prize, parent, false);
        return new LottaryPrizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LottaryPrizeViewHolder holder, int position) {
        String model = prizeList.get(position);
        holder.bindData(model);
    }

    @Override
    public int getItemCount() {
        return prizeList.size();
    }
}
