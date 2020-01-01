package com.rahbarbazaar.checkpanel.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.controllers.interfaces.ShopItemInteraction;
import com.rahbarbazaar.checkpanel.controllers.viewholders.ShopViewHolder;
import com.rahbarbazaar.checkpanel.models.shop.ShopCenterModel;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopViewHolder> {

    List<ShopCenterModel> shopCenterModels;
    Context context;

    public ShopAdapter(List<ShopCenterModel> shopCenterModels, Context context) {
        this.shopCenterModels = shopCenterModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shopcenter, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopCenterModel model = shopCenterModels.get(position);
        holder.bindData(model, position);
        holder.setOnShopListHolderListener(listener, model, position);

    }

    private ShopItemInteraction listener = null;
    public void setListener(ShopItemInteraction listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return shopCenterModels.size();
    }
}
