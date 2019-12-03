package com.example.panelist.controllers.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.panelist.R;
import com.example.panelist.models.register.RegisterMemberModel;

public class RegisterMemberViewHolderDialog extends RecyclerView.ViewHolder {

    TextView txt_name;
    CheckBox checkbox;

    public RegisterMemberViewHolderDialog(@NonNull View itemView) {
        super(itemView);
        txt_name=itemView.findViewById(R.id.txt_name);
        checkbox=itemView.findViewById(R.id.checkbox);
    }

    public void bindData(RegisterMemberModel model) {
        txt_name.setText(model.txt_name);
    }

    public void setOnRegisterHolderListener(RegisterItemInteraction listener, RegisterMemberModel model) {

//        itemView.setOnClickListener(v -> {
//            if(checkbox.isChecked()){
//                checkbox.setChecked(false);
//                listener.onClicked(model.txt_name,checkbox,false);
//            }else if(!checkbox.isChecked()){
//                checkbox.setChecked(true);
//                listener.onClicked(model.txt_name,checkbox,true);
//            }
//        });

//                itemView.setOnClickListener(v -> {
                    checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if(checkbox.isChecked()){
                            checkbox.setChecked(true);
                            listener.onClicked(model.txt_name,true);
                        }else if(!checkbox.isChecked()){
                            checkbox.setChecked(false);
                            listener.onClicked(model.txt_name,false);
                        }
                    });

//        });



    }
}
