package com.rahbarbazaar.homadit.android.ui.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.rahbarbazaar.shopper.R;
import com.bumptech.glide.Glide;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.BarcodeItemInteraction;
import com.rahbarbazaar.homadit.android.models.barcodlist.Barcode;
import com.rahbarbazaar.homadit.android.models.barcodlist.BarcodeData;
import com.rahbarbazaar.homadit.android.models.barcodlist.BarcodeLoadingState;
import com.rahbarbazaar.homadit.android.models.search_goods.GroupsData;
import com.rahbarbazaar.homadit.android.models.searchable.SearchModel;
import com.rahbarbazaar.homadit.android.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.homadit.android.network.Service;
import com.rahbarbazaar.homadit.android.network.ServiceProvider;
import com.rahbarbazaar.homadit.android.ui.fragments.ScanFragment;
import com.rahbarbazaar.homadit.android.utilities.Cache;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.rahbarbazaar.homadit.android.utilities.DialogFactory;
import com.rahbarbazaar.homadit.android.utilities.GeneralTools;
import com.rahbarbazaar.homadit.android.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRcodeActivity extends CustomBaseActivity implements View.OnClickListener, BarcodeItemInteraction {

    LinearLayout linear_return_qrcode, ll_root;
    RelativeLayout rl_home_qrcode;
    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_finish ,btn_unreadable;

    TextView txt_group_title ;
    ImageView img_icon;

    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    DialogFactory dialogFactory;

    Disposable disposable = new CompositeDisposable();
    MemberPrize initMemberPrizeLists;

    String description;
    String name;

    AVLoadingIndicatorView avi_qrcode,avi_unreadable ;

    List<SearchModel> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode1);


        initView();

        //check network broadcast reciever
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tools.doCheckNetwork(QRcodeActivity.this, findViewById(R.id.root_qrcode_scanner));
            }
        };

        disposable = RxBus.MemberPrizeLists.subscribeMemberPrizeLists(result -> {
            if (result instanceof MemberPrize) {
                initMemberPrizeLists = (MemberPrize) result;
            }
        });




//        new Thread (() -> {
//            ScanFragment scanFragment = new ScanFragment();
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.frg_holder, scanFragment);
//            ft.commit();
//        }).start();


        ScanFragment scanFragment = new ScanFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frg_holder, scanFragment);
        ft.commit();


        txt_group_title.setText(Cache.getString(this,"selectedGroupTitle"));
        String url = Cache.getString(this,"selectedGroupIcon");
        Glide
                .with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.color.blue)
                .into(img_icon);

        avi_qrcode.hide();
    }



    private void initView() {

        linear_return_qrcode = findViewById(R.id.linear_return_qrcode);
        rl_home_qrcode = findViewById(R.id.rl_home_qrcode);
        txt_group_title  = findViewById(R.id.txt_group_title);
        img_icon  = findViewById(R.id.img_icon);
        ll_root = findViewById(R.id.root_qrcode_scanner);
        btn_1 = findViewById(R.id.btn1_register_barcode);
        btn_2 = findViewById(R.id.btn2_register_barcode);
        btn_3 = findViewById(R.id.btn3_register_barcode);
        btn_4 = findViewById(R.id.btn4_register_barcode);
        btn_5 = findViewById(R.id.btn5_register_barcode);
        btn_6 = findViewById(R.id.btn6_register_barcode);
        btn_7 = findViewById(R.id.btn7_register_barcode);
        btn_8 = findViewById(R.id.btn8_register_barcode);
        btn_finish = findViewById(R.id.btn_finish_purchase_qrcode);
        avi_qrcode = findViewById(R.id.avi_qrcode);
        avi_unreadable = findViewById(R.id.avi_unreadable);

        btn_unreadable = findViewById(R.id.btn_unreadable);




//        btn_1.setText(initMemberPrizeLists.data.categories.get(0).name);
//        btn_2.setText(initMemberPrizeLists.data.categories.get(1).name);
//        btn_3.setText(initMemberPrizeLists.data.categories.get(2).name);
//        btn_4.setText(initMemberPrizeLists.data.categories.get(3).name);
//        btn_5.setText(initMemberPrizeLists.data.categories.get(4).name);
//        btn_6.setText(initMemberPrizeLists.data.categories.get(5).name);
//        btn_7.setText(initMemberPrizeLists.data.categories.get(6).name);
//        btn_8.setText(initMemberPrizeLists.data.categories.get(7).name);


        btn_finish.setOnClickListener(this);
        linear_return_qrcode.setOnClickListener(this);
        rl_home_qrcode.setOnClickListener(this);
//        btn_1.setOnClickListener(this);
//        btn_2.setOnClickListener(this);
//        btn_3.setOnClickListener(this);
//        btn_4.setOnClickListener(this);
//        btn_5.setOnClickListener(this);
//        btn_6.setOnClickListener(this);
//        btn_7.setOnClickListener(this);
//        btn_8.setOnClickListener(this);
        btn_unreadable.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.linear_return_qrcode:
                Intent intent = new Intent(QRcodeActivity.this,GroupGoodsActivity.class);
                intent.putExtra("new_register","repeat");
                startActivity(intent);

                finish();
                break;

            case R.id.rl_home_qrcode:
            case R.id.btn_finish_purchase_qrcode:
                startActivity(new Intent(QRcodeActivity.this, MainActivity.class));
                finish();
                break;

//            case R.id.btn1_register_barcode:
//                description = initMemberPrizeLists.data.categories.get(0).description;
//                name = initMemberPrizeLists.data.categories.get(0).name;
//                showInfoDialog(description, name);
//                break;
//
//            case R.id.btn2_register_barcode:
//                description = initMemberPrizeLists.data.categories.get(1).description;
//                name = initMemberPrizeLists.data.categories.get(1).name;
//                showInfoDialog(description, name);
//                break;
//
//            case R.id.btn3_register_barcode:
//                description = initMemberPrizeLists.data.categories.get(2).description;
//                name = initMemberPrizeLists.data.categories.get(2).name;
//                showInfoDialog(description, name);
//                break;
//
//            case R.id.btn4_register_barcode:
//                description = initMemberPrizeLists.data.categories.get(3).description;
//                name = initMemberPrizeLists.data.categories.get(3).name;
//                showInfoDialog(description, name);
//                break;
//
//            case R.id.btn5_register_barcode:
//                description = initMemberPrizeLists.data.categories.get(4).description;
//                name = initMemberPrizeLists.data.categories.get(4).name;
//                showInfoDialog(description, name);
//                break;
//
//            case R.id.btn6_register_barcode:
//                description = initMemberPrizeLists.data.categories.get(5).description;
//                name = initMemberPrizeLists.data.categories.get(5).name;
//                showInfoDialog(description, name);
//                break;
//
//            case R.id.btn7_register_barcode:
//                description = initMemberPrizeLists.data.categories.get(6).description;
//                name = initMemberPrizeLists.data.categories.get(6).name;
//                showInfoDialog(description, name);
//                break;
//
//            case R.id.btn8_register_barcode:
//                description = initMemberPrizeLists.data.categories.get(7).description;
//                name = initMemberPrizeLists.data.categories.get(7).name;
//                showInfoDialog(description, name);
//                break;


            case R.id.btn_unreadable:
//                getCategoryList();
                Intent intent2 = new Intent(QRcodeActivity.this,PurchasedItemActivity.class);
                intent2.putExtra("unreadable_barcode","unreadable_barcode");
                startActivity(intent2);
                break;


        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BarcodeLoadingState state) {

        if (state.getState().equals("show_loading")) {
            avi_qrcode.show();
        } else if (state.getState().equals("stop_loading")) {
            avi_qrcode.hide();
        }
    }

    @Subscribe
    public void onEvent(Barcode barcode) {
//        Toast.makeText(this, ""+barcode.getData().get(0).getDecription(), Toast.LENGTH_SHORT).show();
        showBarcodeListDialog(barcode);
    }

    private void showInfoDialog(String description, String name) {

        dialogFactory = new DialogFactory(QRcodeActivity.this);
        dialogFactory.createQrcodeInfoBtnsDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {
            }
        }, ll_root, description, name);

    }

    private void showBarcodeListDialog(Barcode barcode) {
        DialogFactory dialogFactory = new DialogFactory(QRcodeActivity.this);
        dialogFactory.createBarcodeResultListDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {
                Intent intent = new Intent(QRcodeActivity.this, PurchasedItemActivity.class);
                intent.putExtra("unreadable_barcode", "unreadable_barcode");
                startActivity(intent);
                finish();
            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {
            }
        }, ll_root, barcode, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    public void barcodeListOnClicked(@NotNull BarcodeData model, int position, @NotNull Barcode barcode, @NotNull AlertDialog dialog) {
        Intent intent = new Intent(QRcodeActivity.this, PurchasedItemActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("barcodeList", barcode);
        intent.putExtra("suggested_price",model.getPrice());
        intent.putExtra("product_id",barcode.getData().get(position).getId());
        intent.putExtra("mygroup",barcode.getData().get(position).getMygroup());
//        intent.putExtra("min_price",barcode.getData().get(position).getMinPrice());
//        intent.putExtra("max_price",barcode.getData().get(position).getMaxPrice());
//        intent.putExtra("max_amount",barcode.getData().get(position).getMaxAmount());

        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(connectivityReceiver);
        disposable.dispose();
    }


    private void getCategoryList() {

        avi_unreadable.setVisibility(View.VISIBLE);
        btn_unreadable.setVisibility(View.GONE);

        Service service = new ServiceProvider(this).getmService();
        Call<GroupsData> call = service.getCategorySpnData();
        call.enqueue(new Callback<GroupsData>() {
            @Override
            public void onResponse(Call<GroupsData> call, Response<GroupsData> response) {
                if (response.code() == 200) {

                    avi_unreadable.setVisibility(View.GONE);
                    btn_unreadable.setVisibility(View.VISIBLE);

                    searchList = new ArrayList<>();

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        searchList.add(new SearchModel(response.body().getData().get(i).getTitle(),
                                response.body().getData().get(i).getId()));
                    }

                    RxBus.GroupGoodsList.publishGroupGoodsList(response.body());
                    startActivity(new Intent(QRcodeActivity.this,GroupGoodsActivity.class));


                } else {
                    Toast.makeText(QRcodeActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    avi_unreadable.setVisibility(View.GONE);
                    btn_unreadable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GroupsData> call, Throwable t) {
                Toast.makeText(QRcodeActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                avi_unreadable.setVisibility(View.GONE);
                btn_unreadable.setVisibility(View.VISIBLE);
            }
        });

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent intent = new Intent(QRcodeActivity.this,GroupGoodsActivity.class);
        intent.putExtra("new_register","repeat");
        startActivity(intent);

        finish();
    }
}
