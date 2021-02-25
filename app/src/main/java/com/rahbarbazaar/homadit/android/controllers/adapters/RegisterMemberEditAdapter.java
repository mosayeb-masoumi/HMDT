package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.models.register.RegisterMemberEditModel;

import java.util.List;


public class RegisterMemberEditAdapter extends RecyclerView.Adapter<RegisterMemberEditAdapter.ViewHolderEdit> {

    List<RegisterMemberEditModel> editMemberList;
    Context context;

    public RegisterMemberEditAdapter(List<RegisterMemberEditModel> editMemberList, Context context) {
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

           final Dialog dialog = new Dialog(context);
           dialog.setContentView(R.layout.sample_dialog);
           ImageView img_close = dialog.findViewById(R.id.img_close);
           TextView txt_header = dialog.findViewById(R.id.txt_header);
           TextView txt_description = dialog.findViewById(R.id.txt_description);
           Button btn_no = dialog.findViewById(R.id.btn2);
           Button btn_yes = dialog.findViewById(R.id.btn1);

           txt_description.setPadding(0,0,0,30);
           txt_header.setText("هشدار!");
           btn_no.setText("نه");
           btn_yes.setText("بله");

           txt_description.setText("آیا مطمینید که "+ model.txt_name + " از لیست حذف شود؟");

           img_close.setOnClickListener(view -> dialog.dismiss());

           if (dialog.getWindow() != null) {
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           }

           btn_no.setOnClickListener(view -> dialog.dismiss());

           btn_yes.setOnClickListener(view -> {
               editMemberList.remove(position);
               notifyItemRemoved(position);
               notifyDataSetChanged();

               dialog.dismiss();
           });


           dialog.show();

//           editMemberList.remove(position);
//           notifyItemRemoved(position);
//           notifyDataSetChanged();
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

}
