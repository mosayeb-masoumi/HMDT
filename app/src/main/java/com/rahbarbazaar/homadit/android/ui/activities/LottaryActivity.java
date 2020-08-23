package com.rahbarbazaar.homadit.android.ui.activities;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.LottaryPastAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.LottaryPastItemInteraction;
import com.rahbarbazaar.homadit.android.models.Lottary.ActiveLinkDetail;
import com.rahbarbazaar.homadit.android.models.Lottary.LottaryModel;
import com.rahbarbazaar.homadit.android.models.Lottary.OldMeDetail;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.OldDetail;
import com.rahbarbazaar.homadit.android.models.api_error.APIError422;
import com.rahbarbazaar.homadit.android.models.api_error.ErrorUtils;
import com.rahbarbazaar.homadit.android.network.Service;
import com.rahbarbazaar.homadit.android.network.ServiceProvider;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.rahbarbazaar.homadit.android.utilities.DialogFactory;
import com.rahbarbazaar.homadit.android.utilities.GeneralTools;
import com.rahbarbazaar.homadit.android.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LottaryActivity extends CustomBaseActivity implements LottaryPastItemInteraction , View.OnClickListener{

    GeneralTools tools;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    LottaryPastAdapter adapter;
    RelativeLayout rl_condition ,rl_takepart ,rl_cancel,lottary_root;

    LinearLayout linear_return_lottary;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();

    LottaryModel lottaryModel;

    TextView txt_title_active_lottary,txt_amount_active_lottary ,txt_current,txt_max ,txt_cancel,txt_takepart ,txt_no_pastlist;
    AVLoadingIndicatorView avi_takepart,avi_cancel;
    DialogFactory dialogFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottary);

        initView();

        //initial Dialog factory
        dialogFactory = new DialogFactory(LottaryActivity.this);


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


        setTexts();

        setPastLottaryList();

    }

    @SuppressLint("SetTextI18n")
    private void setTexts() {
        txt_title_active_lottary.setText("قرعه کشی "+lottaryModel.data.active.data.get(0).title+"");
        txt_amount_active_lottary.setText("شانس قرعه کشی : "+lottaryModel.data.active.data.get(0).minimum);
        txt_max.setText("حداکثر مشارکت : "+lottaryModel.data.active.data.get(0).maximum+" "+"پاپاسی");
        txt_current.setText("موجودی : "+lottaryModel.data.active.data.get(0).current+" "+"پاپاسی");
    }

    private void setPastLottaryList() {

        List<ActiveLinkDetail> list = lottaryModel.data.activeLink.data;
        List<OldMeDetail> oldMeDetailList = lottaryModel.data.oldMe.data;

        if(oldMeDetailList.size()==0){
            txt_no_pastlist.setVisibility(View.VISIBLE);
        }else{
            txt_no_pastlist.setVisibility(View.GONE);
            linearLayoutManager = new LinearLayoutManager(LottaryActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new LottaryPastAdapter(oldMeDetailList, LottaryActivity.this);
            adapter.setListener(this);
            recyclerView.setAdapter(adapter);
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_past_lottary);
        rl_condition = findViewById(R.id.rl_condition);
        rl_takepart =findViewById(R.id.rl_takepart);
        txt_title_active_lottary = findViewById(R.id.txt_title_active_lottary);
        txt_amount_active_lottary = findViewById(R.id.txt_amount_active_lottary);
        txt_current = findViewById(R.id.txt_current);
        txt_max = findViewById(R.id.txt_max);
        rl_cancel =findViewById(R.id.rl_cancel);
        avi_cancel = findViewById(R.id.avi_cancel_lottary);
        avi_takepart = findViewById(R.id.avi_takepart);
        txt_cancel = findViewById(R.id.txt_cancel_lottary);
        txt_takepart = findViewById(R.id.txt_takepart);
        txt_no_pastlist = findViewById(R.id.txt_no_pastlist);
        linear_return_lottary = findViewById(R.id.linear_return_lottary);
        linear_return_lottary.setOnClickListener(this);
        rl_cancel.setOnClickListener(this);
        rl_takepart.setOnClickListener(this);
        rl_condition.setOnClickListener(this);
    }

    @Override
    public void pastLottaryItemOnClicked(OldMeDetail model, int position, AVLoadingIndicatorView avi) {

        avi.setVisibility(View.VISIBLE);

        Service service = new ServiceProvider(this).getmService();
        Call<OldDetail> call = service.getOldDetail(model.id);
        call.enqueue(new Callback<OldDetail>() {
            @Override
            public void onResponse(Call<OldDetail> call, Response<OldDetail> response) {
                avi.setVisibility(View.GONE);
                if(response.code()==200){
                    OldDetail oldDetail = response.body();
                    RxBus.LottaryOldDetail.publishLottaryOldDetail(oldDetail);
                    startActivity(new Intent(LottaryActivity.this,LottaryWinnersActivity.class));

                }else{
                    Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OldDetail> call, Throwable t) {
                avi.setVisibility(View.GONE);
                Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.rl_condition:
                startActivity(new Intent(LottaryActivity.this,LottaryConditionActivity.class));
                break;

            case R.id.rl_takepart:
                takepartDialog();
                break;

            case R.id.rl_cancel:
                cancelRequest();
                break;

            case R.id.linear_return_lottary:
                startActivity(new Intent(LottaryActivity.this,MainActivity.class));
                finish();
                break;
        }
    }



    private void cancelRequest() {

        avi_cancel.setVisibility(View.VISIBLE);
        txt_cancel.setVisibility(View.GONE);

        Service service = new ServiceProvider(this).getmService();
        Call<Boolean> call = service.cancelLottery(lottaryModel.data.active.data.get(0).lotteryId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                avi_cancel.setVisibility(View.GONE);
                txt_cancel.setVisibility(View.VISIBLE);

                if(response.code()==200){
                    Toast.makeText(LottaryActivity.this, "قرعه کشی با موفقیت لغو شد." , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                avi_cancel.setVisibility(View.GONE);
                txt_cancel.setVisibility(View.VISIBLE);
                Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void takepartDialog() {

        dialogFactory.createLottaryDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... strings) {

                int amount = Integer.parseInt(strings[0]);
                takepartRequest(amount);

            }

            @Override
            public void onDeniedButtonClicked(boolean cancel_dialog) {

            }
        }, lottary_root);
    }

    private void takepartRequest(int amount) {

        avi_takepart.setVisibility(View.VISIBLE);
        txt_takepart.setVisibility(View.GONE);

        Service service = new ServiceProvider(this).getmService();
        Call<Boolean> call = service.createLottery(lottaryModel.data.active.data.get(0).lotteryId ,amount);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                avi_takepart.setVisibility(View.GONE);
                txt_takepart.setVisibility(View.VISIBLE);

                if(response.code()==200){
                    Toast.makeText(LottaryActivity.this, "قرعه کشی با موفقیت ثبت شد." , Toast.LENGTH_SHORT).show();
                }else if(response.code()==422){

                    APIError422 apiError = ErrorUtils.parseError422(response);
                     StringBuilder builderAmount = null;
                    if (apiError.errors.amount != null) {
                        builderAmount = new StringBuilder();
                        for (String b : apiError.errors.amount) {
                            builderAmount.append("").append(b).append(" ");
                        }
                    }
                    if (builderAmount != null) {
                        Toast.makeText(LottaryActivity.this, "" + builderAmount, Toast.LENGTH_SHORT).show();
                    }

                    int a = 5;

                }else{
                    Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                avi_takepart.setVisibility(View.GONE);
                txt_takepart.setVisibility(View.VISIBLE);
                Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });


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