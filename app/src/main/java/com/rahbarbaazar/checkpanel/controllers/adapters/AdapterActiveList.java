package com.rahbarbaazar.checkpanel.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rahbarbaazar.checkpanel.R;
import com.rahbarbaazar.checkpanel.controllers.interfaces.ActiveListItemInteraction;
import com.rahbarbaazar.checkpanel.controllers.viewholders.ActiveListViewHolder;
import com.rahbarbaazar.checkpanel.models.activelist.ActiveList;

import java.util.List;

public class AdapterActiveList extends RecyclerView.Adapter<ActiveListViewHolder> {

    List<ActiveList> activeLists;
    Context context;

    public AdapterActiveList(List<ActiveList> activeLists, Context context) {
        this.activeLists = activeLists;
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
        ActiveList model = activeLists.get(position);
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
        return activeLists.size();
    }


}
