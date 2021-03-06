package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.PrizeItemInteraction;
import com.rahbarbazaar.homadit.android.controllers.viewholders.PrizeViewHolder;
import com.rahbarbazaar.homadit.android.models.register.Prize;

import java.util.List;


public class PrizeAdapter extends RecyclerView.Adapter<PrizeViewHolder> {

    List<Prize> prizeList;
    Context context;

    public PrizeAdapter(List<Prize> prizeList, Context context) {
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
