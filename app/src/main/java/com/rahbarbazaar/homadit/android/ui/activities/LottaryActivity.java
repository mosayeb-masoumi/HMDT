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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryPastAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastItemInteraction;
import com.rahbarbazaar.homadit.android.models.Lottary.LottaryModel;
import com.rahbarbazaar.homadit.android.models.Lottary.OldMeDetail;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.rahbarbazaar.homadit.android.utilities.GeneralTools;
import com.rahbarbazaar.homadit.android.utilities.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LottaryActivity extends CustomBaseActivity implements LottaryPastItemInteraction , View.OnClickListener{

    GeneralTools tools;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    LottaryPastAdapter adapter;
    RelativeLayout rl_condition ,rl_takepart;

    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();

    LottaryModel lottaryModel;

    TextView txt_title_active_lottary,txt_amount_active_lottary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottary);

        initView();


        // to check internet connection
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(LottaryActivity.this, findViewById(R.id.lottary_root));
            }
        };

        //get data from register fragment
        disposable = RxBus.Lottary.subscribeLottary(result -> {
            if (result instanceof LottaryModel) {
                lottaryModel = new LottaryModel();
                lottaryModel = (LottaryModel) result;
            }
        });


        txt_title_active_lottary.setText("قرعه کشی "+lottaryModel.data.active.data.get(0).title+"");
        txt_amount_active_lottary.setText("شانس قرعه کشی : "+lottaryModel.data.active.data.get(0).minimum+" "+"پاپاسی");
        setPastLottaryList();

    }

    private void setPastLottaryList() {
//
//        List<String> list = new ArrayList<>();
//        list.add("khkjhjhjkh");
//        list.add("khkjhjhjkh");
//        list.add("khkjhjhjkh");
//        list.add("khkjhjhjkh");
//        list.add("khkjhjhjkh");
//        list.add("khkjhjhjkh");
//        list.add("khkjhjhjkh");
//        list.add("khkjhjhjkh");
//        list.add("khkjhjhjkh");


        List<OldMeDetail> oldLottaryList = lottaryModel.data.oldMe.data;
        linearLayoutManager = new LinearLayoutManager(LottaryActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LottaryPastAdapter(oldLottaryList, LottaryActivity.this);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_past_lottary);
        rl_condition = findViewById(R.id.rl_condition);
        rl_takepart =findViewById(R.id.rl_takepart);
        txt_title_active_lottary = findViewById(R.id.txt_title_active_lottary);
        txt_amount_active_lottary = findViewById(R.id.txt_amount_active_lottary);
        rl_takepart.setOnClickListener(this);
        rl_condition.setOnClickListener(this);
    }

    @Override
    public void pastLottaryItemOnClicked(OldMeDetail model, int position) {

        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.rl_condition:
                startActivity(new Intent(LottaryActivity.this,LottaryConditionActivity.class));
                break;

            case R.id.rl_takepart:
                startActivity(new Intent(LottaryActivity.this,LottaryWinnersActivity.class));
                break;

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
}