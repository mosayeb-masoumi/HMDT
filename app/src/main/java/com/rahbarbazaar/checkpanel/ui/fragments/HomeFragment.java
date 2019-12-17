package com.rahbarbazaar.checkpanel.ui.fragments;


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

import com.bumptech.glide.Glide;
import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.models.dashboard.DashboardModel;
import com.rahbarbazaar.checkpanel.ui.activities.HtmlLoaderActivity;
import com.rahbarbazaar.checkpanel.utilities.RxBus;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    Disposable disposable = new CompositeDisposable();
    DashboardModel dashboardModel;



    CardView crd_news, crd_video, crd_purchases;
    ImageView img_news, img_video, img_myshop;
    TextView txt_balance, txt_incomplete_purchase, txt_total_purchase, txt_registered_products;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = RxBus.DashboardModel.subscribeDashboardModel(result -> {
            if (result instanceof DashboardModel) {
                dashboardModel = (DashboardModel) result;
            }
        });

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        initViews(view);

        setContentView();

        return view;

    }

    private void initViews(View view) {

        crd_news = view.findViewById(R.id.crd_news);
        crd_video = view.findViewById(R.id.crd_video);
        crd_purchases = view.findViewById(R.id.crd_purchases);
        img_news = view.findViewById(R.id.img_news);
        img_video = view.findViewById(R.id.img_video);
        img_myshop = view.findViewById(R.id.img_myshop);
        txt_balance = view.findViewById(R.id.txt_balance);
        txt_incomplete_purchase = view.findViewById(R.id.txt_incomplete_purchase);
        txt_total_purchase = view.findViewById(R.id.txt_total_purchase);
        txt_registered_products = view.findViewById(R.id.txt_registered_products);


        crd_news.setOnClickListener(this);
        crd_video.setOnClickListener(this);
        crd_purchases.setOnClickListener(this);

    }

    private void setContentView() {

        Glide.with(Objects.requireNonNull(getActivity())).load(dashboardModel.data.news_image).centerCrop().into(img_news);
        Glide.with(getActivity()).load(dashboardModel.data.video_image).centerCrop().into(img_video);
        Glide.with(getActivity()).load(dashboardModel.data.myshop_image).centerCrop().into(img_myshop);
        txt_balance.setText(String.valueOf(dashboardModel.data.one));
        txt_incomplete_purchase.setText(String.valueOf(dashboardModel.data.two));
        txt_total_purchase.setText(String.valueOf(dashboardModel.data.three));
        txt_registered_products.setText(String.valueOf(dashboardModel.data.four));
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.crd_news:
                goToHtmlActivity(dashboardModel.data.news_content, true);
                break;

            case R.id.crd_video:
                goToHtmlActivity(dashboardModel.data.video_content, true);
                break;

            case R.id.crd_purchases:

                break;
        }

    }


    private void goToHtmlActivity(String url, boolean shouldBeLoadUrl) {

        Intent intent = new Intent(getContext(), HtmlLoaderActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("surveyDetails", false);
        intent.putExtra("isShopping", shouldBeLoadUrl);
        startActivity(intent);
//        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose(); //very important

    }


}
