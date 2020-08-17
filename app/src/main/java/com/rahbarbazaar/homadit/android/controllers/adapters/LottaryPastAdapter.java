package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastItemInteraction;
import com.rahbarbazaar.homadit.android.controllers.viewholders.LotteryPastViewHolder;

import java.util.ArrayList;
import java.util.List;

public class LottaryPastAdapter extends RecyclerView.Adapter<LotteryPastViewHolder> {

    List<String> list = new ArrayList<>();
    Context context;

    public LottaryPastAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LotteryPastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lottary_past, parent, false);
        return new LotteryPastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LotteryPastViewHolder holder, int position) {
        String model = list.get(position);
        holder.bindData(model);
        holder.setOnLotteryPastHolderListener(listener,model ,position);

    }

    private LottaryPastItemInteraction listener = null;
    public void setListener(LottaryPastItemInteraction listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
