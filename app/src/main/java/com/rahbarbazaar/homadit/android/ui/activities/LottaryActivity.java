package com.rahbarbazaar.homadit.android.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryPastAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastItemInteraction;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class LottaryActivity extends CustomBaseActivity implements LottaryPastItemInteraction {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    LottaryPastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottary);


        initView();

        setPastLottaryList();

    }

    private void setPastLottaryList() {

        List<String> list = new ArrayList<>();
        list.add("khkjhjhjkh");
        list.add("khkjhjhjkh");
        list.add("khkjhjhjkh");
        list.add("khkjhjhjkh");
        list.add("khkjhjhjkh");
        list.add("khkjhjhjkh");
        list.add("khkjhjhjkh");
        list.add("khkjhjhjkh");
        list.add("khkjhjhjkh");

        linearLayoutManager = new LinearLayoutManager(LottaryActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LottaryPastAdapter(list, LottaryActivity.this);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_past_lottary);
    }

    @Override
    public void pastLottaryItemOnClicked(String model, int position) {

        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
    }
}