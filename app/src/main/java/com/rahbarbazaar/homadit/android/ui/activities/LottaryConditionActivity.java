package com.rahbarbazaar.homadit.android.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryLinkAdapter;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryPrizeAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryLinkItemInteraction;
import com.rahbarbazaar.homadit.android.models.Lottary.ActiveLinkDetail;
import com.rahbarbazaar.homadit.android.models.Lottary.LottaryModel;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.rahbarbazaar.homadit.android.utilities.GeneralTools;
import com.rahbarbazaar.homadit.android.utilities.RxBus;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LottaryConditionActivity extends CustomBaseActivity implements View.OnClickListener, LottaryLinkItemInteraction {
    GeneralTools tools;

    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    LottaryModel lottaryModel;
    TextView txt_condition,txt_prize1,txt_prize2,txt_prize3,
            txt_start_date,txt_finish_date,txt_lottary_date,txt_lottary_minimum;

    LinearLayout linear_return_lottary_condition;
    RecyclerView rl_prize_condition,rl_link_condition;
    LottaryPrizeAdapter adapter;
    LottaryLinkAdapter linkAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottary_condition);


        // to check internet connection
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(LottaryConditionActivity.this, findViewById(R.id.lottary_condition_root));
            }
        };

        //get data from register fragment
        disposable = RxBus.Lottary.subscribeLottary(result -> {
            if (result instanceof LottaryModel) {
                lottaryModel = new LottaryModel();
                lottaryModel = (LottaryModel) result;
            }
        });

        initView();
        setTexts();
        setPrizeList();
        setLinkList();
    }

    private void initView() {
        txt_condition = findViewById(R.id.txt_condition);
        txt_start_date = findViewById(R.id.txt_start_date);
        txt_finish_date = findViewById(R.id.txt_finish_date);
        txt_lottary_date = findViewById(R.id.txt_lottary_date);
        txt_lottary_minimum = findViewById(R.id.txt_lottary_minimum);
        rl_prize_condition = findViewById(R.id.rl_prize_condition);
        rl_link_condition = findViewById(R.id.rl_link_condition);
        linear_return_lottary_condition =findViewById(R.id.linear_return_lottary_condition);
        linear_return_lottary_condition.setOnClickListener(this);
    }

    private void setPrizeList() {
//        List<String> prizeList = lottaryModel.data.active.data.get(0).prize;
//        linearLayoutManager = new LinearLayoutManager(LottaryConditionActivity.this);
//        rl_prize_condition.setLayoutManager(linearLayoutManager);
//        adapter = new LottaryPrizeAdapter(prizeList, LottaryConditionActivity.this);
//        rl_prize_condition.setAdapter(adapter);
    }

    private void setLinkList() {

        List<ActiveLinkDetail> linkList = lottaryModel.data.activeLink.data;
        linearLayoutManager = new LinearLayoutManager(LottaryConditionActivity.this);
        rl_link_condition.setLayoutManager(linearLayoutManager);
        linkAdapter = new LottaryLinkAdapter(linkList, LottaryConditionActivity.this);
        linkAdapter.setListener(this);
        rl_link_condition.setAdapter(linkAdapter);
    }

    private void setTexts() {

        txt_condition.setText(lottaryModel.data.active.data.get(0).description);
//        txt_prize2.setText(lottaryModel.data.active.data.get(0).prize);
        txt_lottary_date.setText("تاریخ قرعه کشی : "+lottaryModel.data.active.data.get(0).eventDate);
        txt_start_date.setText("شروع ثبت نام : "+lottaryModel.data.active.data.get(0).startDate);
        txt_finish_date.setText("پایان ثبت نام : "+lottaryModel.data.active.data.get(0).endDate);
        txt_lottary_minimum.setText("حداقل مشارکت : "+lottaryModel.data.active.data.get(0).minimum +" "+"پاپاسی");
    }


    @Override
    public void onClick(View view) {
          switch (view.getId()){
              case R.id.linear_return_lottary_condition:
                  startActivity(new Intent(LottaryConditionActivity.this,LottaryActivity.class));
                  finish();
          }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose(); //very important  to avoid memory leak
        unregisterReceiver(connectivityReceiver);
    }


    @Override
    public void pastLottaryItemOnClicked(ActiveLinkDetail model, int position) {

        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
    }
}