package com.rahbarbazaar.checkpanel.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.checkpanel.controllers.viewholders.RegisterMemberViewHolderDialog;
import com.rahbarbazaar.checkpanel.models.register.Member;

import java.util.List;

public class AdapterRegisterMemberDialog extends RecyclerView.Adapter<RegisterMemberViewHolderDialog> {

//    public List<RegisterMemberModel> members;
    public List<Member> members;
    public Context context;

    public AdapterRegisterMemberDialog(List<Member> members, Context context) {
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

        final Member model = members.get(position);
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
