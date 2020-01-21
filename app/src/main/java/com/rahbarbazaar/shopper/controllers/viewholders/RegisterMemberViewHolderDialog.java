package com.rahbarbazaar.shopper.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.shopper.models.register.Member;


public class RegisterMemberViewHolderDialog extends RecyclerView.ViewHolder {

    private TextView txt_name;
    private CheckBox checkbox;

    public RegisterMemberViewHolderDialog(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        checkbox = itemView.findViewById(R.id.checkbox);
    }

    public void bindData(Member model) {
        txt_name.setText(model.name);
    }

    public void setOnRegisterHolderListener(RegisterItemInteraction listener, Member model) {

        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkbox.isChecked()) {
                checkbox.setChecked(true);
                listener.onClicked(model.name, model.id, true);
            } else if (!checkbox.isChecked()) {
                checkbox.setChecked(false);
                listener.onClicked(model.name, model.id, false);
            }
        });
    }
}
