package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.homadit.android.models.register.Member;


public class RegisterMemberViewHolderDialog extends RecyclerView.ViewHolder {

    private TextView txt_name;
    private CheckBox checkbox;
    RelativeLayout rl_member_row;

    public RegisterMemberViewHolderDialog(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        checkbox = itemView.findViewById(R.id.checkbox);
        rl_member_row = itemView.findViewById(R.id.rl_member_row);
    }

    public void bindData(Member model) {
        txt_name.setText(model.name);
    }

    public void setOnRegisterHolderListener(RegisterItemInteraction listener, Member model, String spn_name, Dialog dialog) {

        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkbox.isChecked()) {
                checkbox.setChecked(true);
                model.setSelected(true);
                listener.onClicked(model.name, model.id,spn_name,dialog, true);
            } else if (!checkbox.isChecked()) {
                checkbox.setChecked(false);
                model.setSelected(false);
                listener.onClicked(model.name, model.id,spn_name,dialog, false);
            }
        });


        rl_member_row.setOnClickListener(view -> {

            if (model.isSelected()) {
                checkbox.setChecked(false);
                model.setSelected(false);
                listener.onClicked(model.name, model.id,spn_name,dialog, true);
            } else if (!model.isSelected()) {
                checkbox.setChecked(true);
                model.setSelected(true);
                listener.onClicked(model.name, model.id,spn_name,dialog, false);
            }
        });
    }
}
