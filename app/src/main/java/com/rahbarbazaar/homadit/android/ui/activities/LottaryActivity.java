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
import com.rahbarbazaar.homadit.android.models.Lottary.cancel.LottaryCancelModel;
import com.rahbarbazaar.homadit.android.models.Lottary.create.LottaryCreateModel;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.OldDetail;
import com.rahbarbazaar.homadit.android.models.api_error.APIError422;
import com.rahbarbazaar.homadit.android.models.api_error.ErrorUtils;
import com.rahbarbazaar.homadit.android.models.register.GetShopId;
import com.rahbarbazaar.homadit.android.network.Service;
import com.rahbarbazaar.homadit.android.network.ServiceProvider;
import com.rahbarbazaar.homadit.android.utilities.Cache;
import com.rahbarbazaar.homadit.android.utilities.ConvertEnDigitToFa;
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

public class LottaryActivity extends CustomBaseActivity implements LottaryPastItemInteraction, View.OnClickListener {

    GeneralTools tools;
    RecyclerView recyclerView;
    LinearLayout ll_current_lottary, ll_no_current_lottary, ll_header_info;
    LinearLayoutManager linearLayoutManager;
    LottaryPastAdapter adapter;
    RelativeLayout rl_condition, rl_takepart, rl_cancel, lottary_root;

    LinearLayout linear_return_lottary;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();

    LottaryModel lottaryModel;

    TextView txt_title_active_lottary, txt_amount_active_lottary, txt_current, txt_max, txt_cancel, txt_takepart, txt_no_pastlist, txt_no_current_lottary;
    AVLoadingIndicatorView avi_takepart, avi_cancel;
    DialogFactory dialogFactory;

    String strAmount;

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

//        getLottary();


        setTexts();
        setPastLottaryList();


        if (Cache.getString(this, "takepart").equals("yes")) {
            // user participated
            rl_cancel.setVisibility(View.VISIBLE);
            rl_takepart.setVisibility(View.GONE);
        } else {
            // user not participated
            rl_cancel.setVisibility(View.GONE);
            rl_takepart.setVisibility(View.VISIBLE);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        String current = Cache.getString(this, "current");
        String maximum =Cache.getString(this, "maximum");

        if(current != null && maximum !=null){
            txt_current.setText("موجودی : " + ConvertEnDigitToFa.convert(current) + " " + "پاپاسی");
            txt_max.setText("حداکثر مشارکت : " + ConvertEnDigitToFa.convert(maximum) + " " + "پاپاسی");
        }
    }


    @SuppressLint("SetTextI18n")
    private void setTexts() {
        if (lottaryModel.data.active.data != null && lottaryModel.data.active.data.size() > 0) {

            ll_current_lottary.setVisibility(View.VISIBLE);
            ll_no_current_lottary.setVisibility(View.GONE);
            ll_header_info.setVisibility(View.VISIBLE);

            txt_title_active_lottary.setText("قرعه کشی " + lottaryModel.data.active.data.get(0).title + "");

//


            strAmount = Cache.getString(this, "amount");
            if (strAmount.equals("0")) {
                txt_amount_active_lottary.setVisibility(View.GONE);
            } else {
                txt_amount_active_lottary.setVisibility(View.VISIBLE);
                txt_amount_active_lottary.setText("شانس قرعه کشی : " + strAmount + " " + "پاپاسی");
            }


            if (Cache.getString(this, "maximum") == null && Cache.getString(this, "current") == null) {

                txt_max.setText("حداکثر مشارکت : " + lottaryModel.data.active.data.get(0).maximum + " " + "پاپاسی");
                txt_current.setText("موجودی : " + lottaryModel.data.active.data.get(0).current + " " + "پاپاسی");
                Cache.setString(this, "maximum", lottaryModel.data.active.data.get(0).maximum);
                Cache.setString(this, "current", lottaryModel.data.active.data.get(0).current);
            } else {

                String current = Cache.getString(this, "current");
                String maximum =Cache.getString(this, "maximum");
                txt_current.setText("موجودی : " + ConvertEnDigitToFa.convert(current) + " " + "پاپاسی");
                txt_max.setText("حداکثر مشارکت : " + ConvertEnDigitToFa.convert(maximum) + " " + "پاپاسی");

//                txt_max.setText("حداکثر مشارکت : " + ConvertEnDigitToFa.convert(Cache.getString(this, "maximum")+"") + " " + "پاپاسی");
//                txt_current.setText("موجودی : " +ConvertEnDigitToFa.convert(+"")  + " " + "پاپاسی");
            }


        } else {
            ll_current_lottary.setVisibility(View.GONE);
            ll_header_info.setVisibility(View.GONE);
            ll_no_current_lottary.setVisibility(View.VISIBLE);
        }
    }

    private void setPastLottaryList() {

//        List<ActiveLinkDetail> list = lottaryModel.data.activeLink.data;
        List<OldMeDetail> oldMeDetailList = lottaryModel.data.oldMe.data;

        if (oldMeDetailList.size() == 0) {
            txt_no_pastlist.setVisibility(View.VISIBLE);
        } else {
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
        rl_takepart = findViewById(R.id.rl_takepart);
        txt_title_active_lottary = findViewById(R.id.txt_title_active_lottary);
        txt_amount_active_lottary = findViewById(R.id.txt_amount_active_lottary);
        txt_current = findViewById(R.id.txt_current);
        txt_max = findViewById(R.id.txt_max);
        rl_cancel = findViewById(R.id.rl_cancel);
        avi_cancel = findViewById(R.id.avi_cancel_lottary);
        avi_takepart = findViewById(R.id.avi_takepart);
        txt_cancel = findViewById(R.id.txt_cancel_lottary);
        txt_takepart = findViewById(R.id.txt_takepart);
        txt_no_pastlist = findViewById(R.id.txt_no_pastlist);
        linear_return_lottary = findViewById(R.id.linear_return_lottary);
        txt_no_current_lottary = findViewById(R.id.txt_no_current_lottary);
        ll_current_lottary = findViewById(R.id.ll_current_lottary);
        ll_no_current_lottary = findViewById(R.id.ll_no_current_lottary);
        ll_header_info = findViewById(R.id.ll_header_info);

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
                if (response.code() == 200) {
                    OldDetail oldDetail = response.body();
                    RxBus.LottaryOldDetail.publishLottaryOldDetail(oldDetail);
                    startActivity(new Intent(LottaryActivity.this, LottaryWinnersActivity.class));

                } else {
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

        switch (view.getId()) {

            case R.id.rl_condition:
                startActivity(new Intent(LottaryActivity.this, LottaryConditionActivity.class));
                break;

            case R.id.rl_takepart:
                takepartDialog();
                break;

            case R.id.rl_cancel:
                cancelRequest();
                break;

            case R.id.linear_return_lottary:
                startActivity(new Intent(LottaryActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void cancelRequest() {

        avi_cancel.setVisibility(View.VISIBLE);
        txt_cancel.setVisibility(View.GONE);

        Service service = new ServiceProvider(this).getmService();
        Call<LottaryCancelModel> call = service.cancelLottery(lottaryModel.data.active.data.get(0).lotteryId);
        call.enqueue(new Callback<LottaryCancelModel>() {

            @Override
            public void onResponse(Call<LottaryCancelModel> call, Response<LottaryCancelModel> response) {

                avi_cancel.setVisibility(View.GONE);
                txt_cancel.setVisibility(View.VISIBLE);

                if (response.code() == 200) {
                    Toast.makeText(LottaryActivity.this, "قرعه کشی با موفقیت لغو شد.", Toast.LENGTH_SHORT).show();

                    rl_takepart.setVisibility(View.VISIBLE);
                    rl_cancel.setVisibility(View.GONE);

                    LottaryCancelModel lottaryCancelModel = response.body();

                    Cache.setString(LottaryActivity.this, "maximum", String.valueOf(lottaryCancelModel.maximum));
                    Cache.setString(LottaryActivity.this, "current", String.valueOf(lottaryCancelModel.current));

                    Cache.setString(LottaryActivity.this, "takepart", "no");


                    String current = String.valueOf(lottaryCancelModel.current);
                    String maximum = String.valueOf(lottaryCancelModel.maximum);
                    txt_current.setText("موجودی : " + ConvertEnDigitToFa.convert(current) + " " + "پاپاسی");
                    txt_max.setText("حداکثر مشارکت : " + ConvertEnDigitToFa.convert(maximum) + " " + "پاپاسی");

                    Cache.setString(LottaryActivity.this, "amount", "0");
                    txt_amount_active_lottary.setVisibility(View.GONE);


                } else {
                    Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LottaryCancelModel> call, Throwable t) {
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

                strAmount = String.valueOf(amount);
                strAmount = ConvertEnDigitToFa.convert(strAmount);

            }

            @Override
            public void onDeniedButtonClicked(boolean cancel_dialog) {

            }
        }, lottary_root);
    }

    @SuppressLint("SetTextI18n")
    private void takepartRequest(int amount) {

        avi_takepart.setVisibility(View.VISIBLE);
        txt_takepart.setVisibility(View.GONE);

        Service service = new ServiceProvider(this).getmService();
        Call<LottaryCreateModel> call = service.createLottery(lottaryModel.data.active.data.get(0).lotteryId, amount);
        call.enqueue(new Callback<LottaryCreateModel>() {

            @Override
            public void onResponse(Call<LottaryCreateModel> call, Response<LottaryCreateModel> response) {
                avi_takepart.setVisibility(View.GONE);
                txt_takepart.setVisibility(View.VISIBLE);

                if (response.code() == 200) {
                    Toast.makeText(LottaryActivity.this, "قرعه کشی با موفقیت ثبت شد.", Toast.LENGTH_SHORT).show();

                    rl_takepart.setVisibility(View.GONE);
                    rl_cancel.setVisibility(View.VISIBLE);

                    LottaryCreateModel lottaryCreateModel = response.body();

                    String current = String.valueOf(lottaryCreateModel.current);
                    String maximum = String.valueOf(lottaryCreateModel.maximum);

                    txt_current.setText("موجودی : " + ConvertEnDigitToFa.convert(current) + " " + "پاپاسی");
                    txt_max.setText("حداکثر مشارکت : " + ConvertEnDigitToFa.convert(maximum) + " " + "پاپاسی");

                    Cache.setString(LottaryActivity.this, "maximum", String.valueOf(lottaryCreateModel.maximum));
                    Cache.setString(LottaryActivity.this, "current", String.valueOf(lottaryCreateModel.current));

                    Cache.setString(LottaryActivity.this, "takepart", "yes");

                    txt_amount_active_lottary.setVisibility(View.VISIBLE);
                    Cache.setString(LottaryActivity.this, "amount", strAmount);
                    txt_amount_active_lottary.setText("شانس قرعه کشی : " + strAmount + " " + "پاپاسی");

                } else if (response.code() == 422) {

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


                } else {
                    Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LottaryCreateModel> call, Throwable t) {
                avi_takepart.setVisibility(View.GONE);
                txt_takepart.setVisibility(View.VISIBLE);
                Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose(); //very important  to avoid memory leak
        unregisterReceiver(connectivityReceiver);
    }


//    private void getLottary() {
//
//        Service service = new ServiceProvider(this).getmService();
//        Call<LottaryModel> call = service.getLottaryMain();
//
//        call.enqueue(new Callback<LottaryModel>() {
//            @Override
//            public void onResponse(Call<LottaryModel> call, Response<LottaryModel> response) {
//                if (response.code() == 200) {
//                    LottaryModel lottaryModel = new LottaryModel();
//                    lottaryModel = response.body();
//
//                    if (lottaryModel.data.activeMe.data.size() > 0) {
//                        // user participated
//                        rl_cancel.setVisibility(View.VISIBLE);
//                        rl_takepart.setVisibility(View.GONE);
//
//                    } else {
//                        // user not participated
//                        rl_cancel.setVisibility(View.GONE);
//                        rl_takepart.setVisibility(View.VISIBLE);
//                    }
//
//                } else {
//                    Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LottaryModel> call, Throwable t) {
//                Toast.makeText(LottaryActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//    }
}