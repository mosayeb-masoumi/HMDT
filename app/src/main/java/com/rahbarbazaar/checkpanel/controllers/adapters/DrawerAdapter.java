package com.rahbarbazaar.checkpanel.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.controllers.interfaces.DrawerItemClicked;
import com.rahbarbazaar.checkpanel.controllers.viewholders.DrawerViewHolder;
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create.DrawerItems;

import java.util.List;


public class DrawerAdapter extends RecyclerView.Adapter<DrawerViewHolder> {

    private List<DrawerItems> drawerItems ;
    private Context context;

    public DrawerAdapter(List<DrawerItems> drawerItems, Context context) {
        this.drawerItems = drawerItems;
        this.context = context;
    }

    @NonNull
    @Override
    public DrawerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_drawer_items, parent, false);
        return new DrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerViewHolder holder, int position) {



        DrawerItems model = drawerItems.get(position);
        holder.bindData(model);
        holder.setOnPrizeHolderListener(listener,model);

    }


    private DrawerItemClicked listener = null;
    public void setListener(DrawerItemClicked listener) {
        this.listener = listener;
    }

//    DrawerItemClicked

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }



}
