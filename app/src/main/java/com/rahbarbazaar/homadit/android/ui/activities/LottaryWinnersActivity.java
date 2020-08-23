package com.rahbarbazaar.homadit.android.ui.activities;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryPrizeAdapter;
import com.rahbarbazaar.homadit.android.models.Lottary.ActivePrizeDetail;
import com.rahbarbazaar.homadit.android.models.Lottary.LottaryModel;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.OldDetail;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.Prize;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.rahbarbazaar.homadit.android.utilities.GeneralTools;
import com.rahbarbazaar.homadit.android.utilities.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LottaryWinnersActivity extends CustomBaseActivity {

    GeneralTools tools;
    Disposable disposable = new CompositeDisposable();
    OldDetail oldDetail ;

    TextView txt_start_date_winners,txt_finish_date_winners,txt_lottary_date_winners
            ,txt_lottary_minimum_winners,txt_title_winners;
    RecyclerView rv_prize_winners,rv_past_winners,rv_past_links;

    LinearLayoutManager linearLayoutManager;
    LottaryPrizeAdapter lottaryPrizeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottary_winners);

        initView();

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
        txt_title_winners.setText(oldDetail.data.detail.data.get(0).title);
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
    }
    private void setLinkList() {
    }







    private void initView() {
        txt_title_winners = findViewById(R.id.txt_title_winners);
        txt_start_date_winners = findViewById(R.id.txt_start_date_winners);
        txt_finish_date_winners = findViewById(R.id.txt_finish_date_winners);
        txt_lottary_date_winners = findViewById(R.id.txt_lottary_date_winners);
        txt_lottary_minimum_winners = findViewById(R.id.txt_lottary_minimum_winners);

        rv_prize_winners = findViewById(R.id.rv_prize_winners);
        rv_past_winners = findViewById(R.id.rv_past_winners);
        rv_past_links = findViewById(R.id.rv_past_links);
    }


    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose(); //very important  to avoid memory leak
    }
}