package com.rahbarbazaar.homadit.android.ui.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryLinkWinnerAdapter;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryPastWinnerAdapter;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryPrizeAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastLinkIteminteraction;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.Link;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.OldDetail;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.Prize;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.Winner;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.rahbarbazaar.homadit.android.utilities.GeneralTools;
import com.rahbarbazaar.homadit.android.utilities.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LottaryWinnersActivity extends CustomBaseActivity implements LottaryPastLinkIteminteraction , View.OnClickListener {

    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    OldDetail oldDetail ;



    TextView txt_start_date_winners,txt_finish_date_winners,txt_lottary_date_winners
            ,txt_lottary_minimum_winners,txt_title_winners,txt_header;
    RecyclerView rv_prize_winners,rv_past_winners,rv_past_links;
    LinearLayoutManager linearLayoutManager;
    LottaryPrizeAdapter lottaryPrizeAdapter;
    LottaryPastWinnerAdapter pastWinnerAdapter;
    LottaryLinkWinnerAdapter linkAdapter;

    LinearLayout linear_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottary_winners);

        initView();

        // to check internet connection
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tools.doCheckNetwork(LottaryWinnersActivity.this, findViewById(R.id.root_lottary_winner));
            }
        };

        //get data from register fragment
        disposable = RxBus.LottaryOldDetail.subscribeLottaryOldDetail(result -> {
            if (result instanceof OldDetail) {
                oldDetail = new OldDetail();
                oldDetail = (OldDetail) result;
            }
        });

        setTexts();
        setPrizeList();
        setWinnerList();
        setLinkList();
    }

    @SuppressLint("SetTextI18n")
    private void setTexts() {

        txt_header.setText( "قرعه کشی "+oldDetail.data.detail.data.get(0).title);
        txt_lottary_date_winners.setText("تاریخ قرعه کشی : "+oldDetail.data.detail.data.get(0).eventDate);
        txt_start_date_winners.setText("شروع ثبت نام : "+oldDetail.data.detail.data.get(0).startDate);
        txt_finish_date_winners.setText("پایان ثبت نام : "+oldDetail.data.detail.data.get(0).endDate);
        txt_lottary_minimum_winners.setText("حداقل مشارکت : "+oldDetail.data.detail.data.get(0).minimum);
    }

    private void setPrizeList() {

        List<Prize> prizeLists = oldDetail.data.prizeList.data;
        List<String> prizeList = new ArrayList<>();
        for (int i = 0; i <prizeLists.size(); i++) {
            prizeList.add(prizeLists.get(i).prize);
        }

        linearLayoutManager = new LinearLayoutManager(LottaryWinnersActivity.this);
        rv_prize_winners.setLayoutManager(linearLayoutManager);
        lottaryPrizeAdapter = new LottaryPrizeAdapter(prizeList, LottaryWinnersActivity.this);
        rv_prize_winners.setAdapter(lottaryPrizeAdapter);
    }
    private void setWinnerList() {
        List<Winner> winnerList = oldDetail.data.winner.data;
        linearLayoutManager = new LinearLayoutManager(LottaryWinnersActivity.this);
        rv_past_winners.setLayoutManager(linearLayoutManager);
        pastWinnerAdapter = new LottaryPastWinnerAdapter(winnerList, LottaryWinnersActivity.this);
        rv_past_winners.setAdapter(pastWinnerAdapter);
    }


    private void setLinkList() {

        List<Link> linkList = oldDetail.data.link.data;
        linearLayoutManager = new LinearLayoutManager(LottaryWinnersActivity.this);
        rv_past_links.setLayoutManager(linearLayoutManager);
        linkAdapter = new LottaryLinkWinnerAdapter(linkList, LottaryWinnersActivity.this);
        linkAdapter.setListener(this);
        rv_past_links.setAdapter(linkAdapter);

        
    }


    private void initView() {
        txt_title_winners = findViewById(R.id.txt_title_winners);
        txt_start_date_winners = findViewById(R.id.txt_start_date_winners);
        txt_finish_date_winners = findViewById(R.id.txt_finish_date_winners);
        txt_lottary_date_winners = findViewById(R.id.txt_lottary_date_winners);
        txt_lottary_minimum_winners = findViewById(R.id.txt_lottary_minimum_winners);
        txt_header = findViewById(R.id.txt_header_winnerdetail);
        linear_return = findViewById(R.id.linear_return_lottary_winner);

        rv_prize_winners = findViewById(R.id.rv_prize_winners);
        rv_past_winners = findViewById(R.id.rv_past_winners);
        rv_past_links = findViewById(R.id.rv_past_links);

        linear_return.setOnClickListener(this);
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
    public void pastLottaryLinkItemOnClicked(Link model, int position) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(model.link)));
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.linear_return_lottary_winner) {
//            startActivity(new Intent(LottaryWinnersActivity.this, LottaryActivity.class));
            finish();
        }
    }
}