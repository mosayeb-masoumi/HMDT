package com.rahbarbazaar.homadit.android.controllers.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.TransactionItemInteraction;
import com.rahbarbazaar.homadit.android.models.transaction.Transaction;

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    private TextView txt_title,txt_date,txt_amount;
    private ImageView img_transaction;
    private LinearLayout ll_root;

    public TransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_title = itemView.findViewById(R.id.txt_title_accountlist);
        txt_date = itemView.findViewById(R.id.txt_date_accountlist);
        txt_amount = itemView.findViewById(R.id.txt_amount_accountlist);
        img_transaction = itemView.findViewById(R.id.img_transaction_accountlist);
        ll_root = itemView.findViewById(R.id.ll_root_activelist_rv_bg);
    }

    public void bindData(Transaction model, int position, String type) {

        txt_title.setText(model.title);
        txt_date.setText(model.date);
        txt_amount.setText(model.amount);
//        if(type.equals("papasi")){
        if(type.equals("credit")){
            ll_root.setBackground(itemView.getResources().getDrawable(R.drawable.bg_transaction_papasi_row));
        }else{
            ll_root.setBackground(itemView.getResources().getDrawable(R.drawable.activelist_rv_bg));
        }

        if(model.type.equals("debtor") || model.type.equals("کاهش")){
            img_transaction.setImageResource(R.drawable.transaction_down_icon);
        }else if(model.type.equals("creditor") || model.type.equals("افزایش")){
            img_transaction.setImageResource(R.drawable.transaction_up_icon);
        }
    }

    public void setOnActiveListHolderListener(TransactionItemInteraction listener, Transaction model, int position) {
        itemView.setOnClickListener(v -> listener.transactionItemOnClicked(model,position));
    }
}
