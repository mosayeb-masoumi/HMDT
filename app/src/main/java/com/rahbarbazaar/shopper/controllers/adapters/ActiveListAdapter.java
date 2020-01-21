package com.rahbarbazaar.shopper.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.interfaces.ActiveListItemInteraction;
import com.rahbarbazaar.shopper.controllers.viewholders.ActiveListViewHolder;
import com.rahbarbazaar.shopper.models.activelist.ActiveListModel;

import java.util.List;

public class ActiveListAdapter extends RecyclerView.Adapter<ActiveListViewHolder> {

    List<ActiveListModel> activeListModels;
    Context context;

    public ActiveListAdapter(List<ActiveListModel> activeListModels, Context context) {
        this.activeListModels = activeListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ActiveListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_activelist, parent, false);
        return new ActiveListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveListViewHolder holder, int position) {
        ActiveListModel model = activeListModels.get(position);
        holder.bindData(model , position);
        holder.setOnActiveListHolderListener(listener,model , position);

    }

    private ActiveListItemInteraction listener = null;
    public void setListener(ActiveListItemInteraction listener)
    {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return activeListModels.size();
    }


}
