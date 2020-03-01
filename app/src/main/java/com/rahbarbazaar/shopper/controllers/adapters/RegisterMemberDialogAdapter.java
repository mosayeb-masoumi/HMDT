package com.rahbarbazaar.shopper.controllers.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.shopper.controllers.viewholders.RegisterMemberViewHolderDialog;
import com.rahbarbazaar.shopper.models.register.Member;

import java.util.List;

public class RegisterMemberDialogAdapter extends RecyclerView.Adapter<RegisterMemberViewHolderDialog> {

    public List<Member> members;
    public Context context;
    public String spn_name;
    Dialog dialog;

    public RegisterMemberDialogAdapter(List<Member> members, String spn_name, Dialog dialog, Context context) {
        this.members = members;
        this.context = context;
        this.spn_name = spn_name;
        this.dialog = dialog;
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
        holder.setOnRegisterHolderListener(listener,model,spn_name , dialog);
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
