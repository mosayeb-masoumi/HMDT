package com.rahbarbazaar.checkpanel.controllers.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.controllers.interfaces.SearchItemInteraction;
import com.rahbarbazaar.checkpanel.controllers.viewholders.SearchViewHolder;
import com.rahbarbazaar.checkpanel.models.searchable.SearchModel;
import com.rahbarbazaar.checkpanel.utilities.CustomFilter;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {


    public List<SearchModel> searchList, filterList;
    Context context;
    AlertDialog dialog;
    CustomFilter filter;

    public SearchAdapter(List<SearchModel> searchList, Context context, AlertDialog dialog) {
        this.searchList = searchList;
        this.context = context;
        this.filterList = searchList;
        this.dialog = dialog;
    }



    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchModel model = searchList.get(position);
        holder.bindData(model);
        holder.setOnPrizeHolderListener(listener, model, dialog);

    }

    private SearchItemInteraction listener = null;

    public void setListener(SearchItemInteraction listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }


    //RETURN FILTER OBJ
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }

}
