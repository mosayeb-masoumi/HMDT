package com.example.panelist.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.panelist.R;
import com.example.panelist.models.register.RegisterMemberEditModel;
import java.util.List;


public class AdapterRegisterMemberEdit extends RecyclerView.Adapter<AdapterRegisterMemberEdit.ViewHolderEdit> {

    List<RegisterMemberEditModel> editMemberList;
    Context context;

    public AdapterRegisterMemberEdit(List<RegisterMemberEditModel> editMemberList, Context context) {
        this.editMemberList = editMemberList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderEdit onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_register_member_edit, parent, false);
        return new ViewHolderEdit(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEdit holder, int position) {
       RegisterMemberEditModel model = editMemberList.get(position);
       holder.txt_name.setText(model.txt_name);
       holder.img_delete.setOnClickListener(v -> {
           editMemberList.remove(position);
           notifyItemRemoved(position);
           notifyDataSetChanged();
       });
    }

    @Override
    public int getItemCount() {
        return editMemberList.size();
    }

    public class ViewHolderEdit extends RecyclerView.ViewHolder {

        TextView txt_name;
        ImageView img_delete;
        public ViewHolderEdit(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name_edited);
            img_delete = itemView.findViewById(R.id.img_delete_edited);

        }
    }





















//    public AdapterRegisterMemberEdit(List<RegisterMemberEditModel> editMemberList, Context context) {
//        this.editMemberList = editMemberList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public RegisterMemberViewHolderEdit onCreateViewHolder(@NonNull ViewGroup parent, int i) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_register_member_edit, parent, false);
//        return new RegisterMemberViewHolderEdit(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RegisterMemberViewHolderEdit holder, int position) {
//
//        final RegisterMemberEditModel model = editMemberList.get(position);
//        holder.bindData(model ,position);
////        holder.setOnRegisterEditHolderListener(listener,model);
//
//
//    }
//
////    private RegisterEditItemInteraction listener = null;
////    public void setEditListener(RegisterEditItemInteraction listener) {
////        this.listener = listener;
////    }
//
//    @Override
//    public int getItemCount() {
//        return editMemberList.size();
//    }
}
