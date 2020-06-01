package com.rahbarbazaar.shopper.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.models.activelist.ActiveListData;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_create.DashboardCreateData;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_home.HomeData;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.ui.activities.HistoryActivity;
import com.rahbarbazaar.shopper.ui.activities.HtmlLoaderActivity;
import com.rahbarbazaar.shopper.ui.activities.MainActivity;
import com.rahbarbazaar.shopper.ui.activities.NewRegisterListActivity;
import com.rahbarbazaar.shopper.utilities.Cache;
import com.rahbarbazaar.shopper.utilities.RxBus;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    Disposable disposable = new CompositeDisposable();
    DashboardCreateData dashboardCreateData;
    CardView crd_news, crd_video, crd_purchases;
    ImageView img_news, img_video, img_myshop;
    TextView txt_balance, txt_papasi, txt_total_purchase, txt_left_days,txt_msg;

    ActiveListData activeListData;

    public HomeFragment() {
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Cache.setString(getContext(), "user_name", dashboardCreateData.data.userName);
        Cache.setString(getContext(), "share_url", dashboardCreateData.data.shareUrl);

        initViews(view);
        setContentView();
//        getNewPurchaseList0();
        return view;
    }



    private void initViews(View view) {
        crd_purchases = view.findViewById(R.id.crd_purchases);
        img_myshop = view.findViewById(R.id.img_myshop);
        txt_balance = view.findViewById(R.id.txt_balance);
        txt_papasi = view.findViewById(R.id.txt_papasi);
        txt_msg = view.findViewById(R.id.txt_msg);
        txt_left_days = view.findViewById(R.id.txt_left_days);
        crd_purchases.setOnClickListener(this);
    }

    private void setContentView() {

        Glide.with(getActivity()).load(dashboardCreateData.data.myshop_image).centerCrop().into(img_myshop);
        txt_balance.setText(dashboardCreateData.data.one);
        txt_papasi.setText(dashboardCreateData.data.two);
        txt_left_days.setText(dashboardCreateData.data.four);
        txt_msg.setText(dashboardCreateData.data.board);
    }

//    private void getNewPurchaseList0() {
//
//        Service service = new ServiceProvider(getContext()).getmService();
//        Call<ActiveListData> call = service.getActiveList(0);
//        call.enqueue(new Callback<ActiveListData>() {
//            @Override
//            public void onResponse(Call<ActiveListData> call, Response<ActiveListData> response) {
//                if (response.code() == 200) {
//
//                    activeListData = new ActiveListData();
//                    activeListData = response.body();
//                    RxBus.ActiveList0.publishActiveList0(activeListData);
//
//                } else if (response.code() == 204) {
//                    // send zero item
//                    activeListData = new ActiveListData();
//                    RxBus.ActiveList0.publishActiveList0(activeListData);
//
//                } else {
//                    Toast.makeText(getContext(), "" +getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ActiveListData> call, Throwable t) {
//                Toast.makeText(getContext(), "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.crd_purchases:
//                getContext().startActivity(new Intent(getContext(), HistoryActivity.class));
                getContext().startActivity(new Intent(getContext(), NewRegisterListActivity.class));
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
    }

//    private void goToHtmlActivity(String url) {
//        Intent intent = new Intent(getContext(), HtmlLoaderActivity.class);
//        intent.putExtra("url", url);
//        startActivity(intent);
//        Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//    }
    private void getRefreshHomeData() {
        Service service = new ServiceProvider(getContext()).getmService();
        Call<HomeData> call = service.getRefreshHomeData();
        call.enqueue(new Callback<HomeData>() {
            @Override
            public void onResponse(Call<HomeData> call, Response<HomeData> response) {

                if (response.code() == 200) {

                    txt_balance.setText(response.body().data.one);
                    txt_papasi.setText(response.body().data.two);
                    txt_left_days.setText(response.body().data.four);

                } else {
                    Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HomeData> call, Throwable t) {
                Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getRefreshHomeData();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose(); //very important
    }


}
