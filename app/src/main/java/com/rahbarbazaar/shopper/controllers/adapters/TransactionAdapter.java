package com.rahbarbazaar.shopper.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.interfaces.TransactionItemInteraction;
import com.rahbarbazaar.shopper.controllers.viewholders.TransactionViewHolder;
import com.rahbarbazaar.shopper.models.transaction.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

    private List<Transaction> transactions;
    Context context;
    private String type;

    public TransactionAdapter(List<Transaction> transactions, Context context, String type) {
        this.transactions = transactions;
        this.context = context;
        this.type = type;
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction model = transactions.get(position);
        holder.bindData(model, position, type);
        holder.setOnActiveListHolderListener(listener, model, position);

    }

    private TransactionItemInteraction listener = null;

    public void setListener(TransactionItemInteraction listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
