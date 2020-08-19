package com.rahbarbazaar.homadit.android.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryPastAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastItemInteraction;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class LottaryActivity extends CustomBaseActivity implements LottaryPastItemInteraction , View.OnClickListener{

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    LottaryPastAdapter adapter;
    RelativeLayout rl_condition ,rl_takepart;

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
        rl_condition = findViewById(R.id.rl_condition);
        rl_takepart =findViewById(R.id.rl_takepart);
        rl_takepart.setOnClickListener(this);
        rl_condition.setOnClickListener(this);
    }

    @Override
    public void pastLottaryItemOnClicked(String model, int position) {

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
}