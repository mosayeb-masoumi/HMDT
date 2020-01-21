package com.rahbarbazaar.shopper.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.adapters.ShopAdapter;
import com.rahbarbazaar.shopper.controllers.interfaces.ShopItemInteraction;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_create.DashboardCreateData;
import com.rahbarbazaar.shopper.models.shop.ShopCenterModel;
import com.rahbarbazaar.shopper.models.shop.ShopCenter;
import com.rahbarbazaar.shopper.ui.activities.HtmlLoaderActivity;
import com.rahbarbazaar.shopper.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment implements ShopItemInteraction {

    Disposable disposable = new CompositeDisposable();
    DashboardCreateData dashboardCreateData;

    RecyclerView  recyclerView;
    AVLoadingIndicatorView avi;

    ShopCenter shopCenter;
    List<ShopCenterModel> shopCenterModels;
    LinearLayoutManager linearLayoutManager;
    ShopAdapter adapter;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable = RxBus.DashboardModel.subscribeDashboardModel(result -> {
            if (result instanceof DashboardCreateData) {
                dashboardCreateData = (DashboardCreateData) result;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shop, container, false);
        
        initView(view);

    setRecyclerview();

        return view;
    }

    private void initView(View view) {

        recyclerView = view.findViewById(R.id.rv_shopcenter);
        avi = view.findViewById(R.id.avi_loading_fr_shop);

    }




    private void setRecyclerview() {

        shopCenterModels = new ArrayList<>();
        shopCenterModels.addAll(dashboardCreateData.data.shopCenter.data) ;

        linearLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ShopAdapter(shopCenterModels, getContext());
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);  // important to set onclick or else the app will crashed
        adapter.notifyDataSetChanged();

    }


    @Override
    public void shopItemOnClicked(ShopCenterModel model, int position) {

        goToHtmlActivity(model.url);
    }

    private void goToHtmlActivity(String url) {

        Intent intent = new Intent(getContext(), HtmlLoaderActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
       getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose(); //very important

    }

}
