package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.homadit.android.models.register.Member;


public class RegisterMemberViewHolderDialog extends RecyclerView.ViewHolder {

    private TextView txt_name;
//    private CheckBox checkbox;
    ImageView img;
    RelativeLayout rl_member_row;

    public RegisterMemberViewHolderDialog(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
//        checkbox = itemView.findViewById(R.id.checkbox);
        img = itemView.findViewById(R.id.img_untik);
        rl_member_row = itemView.findViewById(R.id.rl_member_row);
    }

    public void bindData(Member model) {
        txt_name.setText(model.name);
    }



    public void setOnRegisterHolderListener(RegisterItemInteraction listener, Member model, String spn_name, Dialog dialog) {


        rl_member_row.setOnClickListener(view -> {

            if(!model.isSelected()){

                model.setSelected(true);
                img.setBackground(itemView.getResources().getDrawable(R.drawable.tik));
                listener.onClicked(model.name, model.id,spn_name,dialog, true);


            }else if(model.isSelected()){
                model.setSelected(false);
                img.setBackground(itemView.getResources().getDrawable(R.drawable.untik));
                listener.onClicked(model.name, model.id,spn_name,dialog, false);
            }






//
//                state = "checkbox";
//
//                if (checkbox.isChecked()) {
//                    checkbox.setChecked(true);
//                    model.setSelected(true);
//                    listener.onClicked(model.name, model.id,spn_name,dialog, true);
//                } else if (!checkbox.isChecked()) {
//                    checkbox.setChecked(false);
//                    model.setSelected(false);
//                    listener.onClicked(model.name, model.id,spn_name,dialog, false);
//                }



        });



//
//        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//
//            state = "checkbox";
//
//            if (checkbox.isChecked()) {
//                checkbox.setChecked(true);
//                model.setSelected(true);
//                listener.onClicked(model.name, model.id,spn_name,dialog, true);
//            } else if (!checkbox.isChecked()) {
//                checkbox.setChecked(false);
//                model.setSelected(false);
//                listener.onClicked(model.name, model.id,spn_name,dialog, false);
//            }
//        });
//
//
//        txt_name.setOnClickListener(view -> {
//
//            state = "title";
//
////            if (model.isSelected()) {
////                checkbox.setChecked(false);
////                model.setSelected(false);
////                listener.onClicked(model.name, model.id,spn_name,dialog, false);
////            } else if (!model.isSelected()) {
////                checkbox.setChecked(true);
////                model.setSelected(true);
////                listener.onClicked(model.name, model.id,spn_name,dialog, true);
////            }
//        });
    }
}
