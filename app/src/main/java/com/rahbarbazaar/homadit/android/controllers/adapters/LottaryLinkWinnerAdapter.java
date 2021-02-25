package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastLinkIteminteraction;
import com.rahbarbazaar.homadit.android.controllers.viewholders.LottaryPastLinkViewHolder;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.Link;

import java.util.List;

public class LottaryLinkWinnerAdapter extends RecyclerView.Adapter<LottaryPastLinkViewHolder> {

    List<Link> linkList;
    Context context;

    public LottaryLinkWinnerAdapter(List<Link> linkList, Context context) {
        this.linkList = linkList;
        this.context = context;
    }

    @NonNull
    @Override
    public LottaryPastLinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lottary_link, parent, false);
        return new LottaryPastLinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LottaryPastLinkViewHolder holder, int position) {
        Link model = linkList.get(position);
        holder.bindData(model);
        holder.setOnLotteryPastLinkHolderListener(listener,model ,position);
    }


    private LottaryPastLinkIteminteraction listener = null;
    public void setListener(LottaryPastLinkIteminteraction listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return linkList.size();
    }
}
