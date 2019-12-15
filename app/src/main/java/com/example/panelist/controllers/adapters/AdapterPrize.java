package com.example.panelist.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.panelist.R;
import com.example.panelist.controllers.viewholders.PrizeItemInteraction;
import com.example.panelist.controllers.viewholders.PrizeViewHolder;
import com.example.panelist.controllers.viewholders.RegisterItemInteraction;
import com.example.panelist.controllers.viewholders.RegisterMemberViewHolderDialog;
import com.example.panelist.models.register.Prize;

import java.util.ArrayList;
import java.util.List;


public class AdapterPrize extends RecyclerView.Adapter<PrizeViewHolder> {

    List<Prize> prizeList;
    Context context;

    public AdapterPrize(List<Prize> prizeList, Context context) {
        this.prizeList = prizeList;
        this.context = context;
    }

    @NonNull
    @Override
    public PrizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_register_member, parent, false);
        return new PrizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrizeViewHolder holder, int position) {

        Prize model = prizeList.get(position);
        holder.bindData(model);
        holder.setOnPrizeHolderListener(listener,model);

    }
    private PrizeItemInteraction listener = null;
    public void setListener(PrizeItemInteraction listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return prizeList.size();
    }

}
