package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryLinkItemInteraction;
import com.rahbarbazaar.homadit.android.controllers.viewholders.LottaryLinkViewHolder;
import com.rahbarbazaar.homadit.android.models.Lottary.ActiveLinkDetail;

import java.util.ArrayList;
import java.util.List;

public class LottaryLinkAdapter extends RecyclerView.Adapter<LottaryLinkViewHolder> {

    List<ActiveLinkDetail> list = new ArrayList<>();
    Context context;

    public LottaryLinkAdapter(List<ActiveLinkDetail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LottaryLinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lottary_link, parent, false);
        return new LottaryLinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LottaryLinkViewHolder holder, int position) {
        ActiveLinkDetail model = list.get(position);
        holder.bindData(model);
        holder.setOnLotteryLinkHolderListener(listener,model ,position);

    }

    private LottaryLinkItemInteraction listener = null;
    public void setListener(LottaryLinkItemInteraction listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
