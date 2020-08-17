package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.models.register.SendPrize;

import java.util.List;

public class EditPrizeAdapter extends RecyclerView.Adapter<EditPrizeAdapter.ViewHolder> {

    List<SendPrize> sendPrizes;
    Context context;

    public EditPrizeAdapter(List<SendPrize> sendPrizes, Context context) {
        this.sendPrizes = sendPrizes;
        this.context = context;
    }

    @NonNull
    @Override
    public EditPrizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_prize_edit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditPrizeAdapter.ViewHolder holoder, int position) {
        SendPrize model = sendPrizes.get(position);

        holoder.txt_name.setText(model.getDescription());
        holoder.img_delete.setOnClickListener(v -> {
            sendPrizes.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return sendPrizes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        ImageView img_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name_edited);
            img_delete = itemView.findViewById(R.id.img_delete_edited);
        }
    }
}
