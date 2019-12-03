package com.example.panelist.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.panelist.R;
import com.example.panelist.controllers.viewholders.RegisterItemInteraction;
import com.example.panelist.controllers.viewholders.RegisterMemberViewHolderDialog;
import com.example.panelist.models.register.RegisterMemberModel;

import java.util.List;

public class AdapterRegisterMemberDialog extends RecyclerView.Adapter<RegisterMemberViewHolderDialog> {

    public List<RegisterMemberModel> members;
    public Context context;

    public AdapterRegisterMemberDialog(List<RegisterMemberModel> members, Context context) {
        this.members = members;
        this.context = context;
    }

    @NonNull
    @Override
    public RegisterMemberViewHolderDialog onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_register_member, parent, false);
        return new RegisterMemberViewHolderDialog(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterMemberViewHolderDialog holder, int position) {

        final RegisterMemberModel model = members.get(position);
        holder.bindData(model);
        holder.setOnRegisterHolderListener(listener,model);
    }

    private RegisterItemInteraction listener = null;
    public void setListener(RegisterItemInteraction listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
