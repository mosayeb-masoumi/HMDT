package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.GroupGoodsItemInteraction;
import com.rahbarbazaar.homadit.android.controllers.viewholders.GroupGoodsViewHolder;
import com.rahbarbazaar.homadit.android.models.group_goods.GroupGoodsModel;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupGoodsViewHolder> {

    public List<GroupGoodsModel> searchList;
    Context context;


    public GroupAdapter(List<GroupGoodsModel> searchList, Context context) {
        this.searchList = searchList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupGoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group_goods, parent, false);
        return new GroupGoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupGoodsViewHolder holder, int position) {
        GroupGoodsModel model = searchList.get(position);
        holder.bindData(model);
        holder.setOnGroupGoodsHolderListener(listener, model);
    }

    private GroupGoodsItemInteraction listener = null;
    public void setListener(GroupGoodsItemInteraction listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }


}
