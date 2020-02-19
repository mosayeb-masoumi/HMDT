package com.rahbarbazaar.shopper.ui.activities;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.interfaces.BarcodeItemInteraction;
import com.rahbarbazaar.shopper.models.barcodlist.Barcode;
import com.rahbarbazaar.shopper.models.barcodlist.BarcodeData;
import com.rahbarbazaar.shopper.models.barcodlist.BarcodeLoadingState;
import com.rahbarbazaar.shopper.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.shopper.ui.fragments.ScanFragment;
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.GeneralTools;
import com.rahbarbazaar.shopper.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class QRcodeActivity1 extends CustomBaseActivity implements View.OnClickListener, BarcodeItemInteraction {

    LinearLayout linear_return_qrcode, ll_root;
    RelativeLayout rl_home_qrcode;
    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_finish, btn_unreadable_barcode;

    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    DialogFactory dialogFactory;

    Disposable disposable = new CompositeDisposable();
    MemberPrize initMemberPrizeLists;

    String description;
    String name;

    AVLoadingIndicatorView avi_qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode1);

        //check network broadcast reciever
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tools.doCheckNetwork(QRcodeActivity1.this, findViewById(R.id.root_qrcode_scanner));
            }
        };

        disposable = RxBus.MemberPrizeLists.subscribeMemberPrizeLists(result -> {
            if (result instanceof MemberPrize) {
                initMemberPrizeLists = (MemberPrize) result;
            }
        });

//        EventBus.getDefault().register(this);

        initView();


        ScanFragment scanFragment = new ScanFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frg_holder, scanFragment);
        ft.commit();

        avi_qrcode.hide();

    }

    private void initView() {

        linear_return_qrcode = findViewById(R.id.linear_return_qrcode);
        rl_home_qrcode = findViewById(R.id.rl_home_qrcode);
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
        btn_unreadable_barcode = findViewById(R.id.btn_unreadable_barcode);
        avi_qrcode = findViewById(R.id.avi_qrcode);

        btn_1.setText(initMemberPrizeLists.data.categories.get(0).name);
        btn_2.setText(initMemberPrizeLists.data.categories.get(1).name);
        btn_3.setText(initMemberPrizeLists.data.categories.get(2).name);
        btn_4.setText(initMemberPrizeLists.data.categories.get(3).name);
        btn_5.setText(initMemberPrizeLists.data.categories.get(4).name);
        btn_6.setText(initMemberPrizeLists.data.categories.get(5).name);
        btn_7.setText(initMemberPrizeLists.data.categories.get(6).name);
        btn_8.setText(initMemberPrizeLists.data.categories.get(7).name);


        btn_finish.setOnClickListener(this);
        btn_unreadable_barcode.setOnClickListener(this);
        linear_return_qrcode.setOnClickListener(this);
        rl_home_qrcode.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.linear_return_qrcode:
                finish();
                break;

            case R.id.rl_home_qrcode:
                startActivity(new Intent(QRcodeActivity1.this, MainActivity.class));
                finish();
                break;

            case R.id.btn1_register_barcode:
                description = initMemberPrizeLists.data.categories.get(0).description;
                name = initMemberPrizeLists.data.categories.get(0).name;
                showInfoDialog(description, name);
                break;

            case R.id.btn2_register_barcode:
                description = initMemberPrizeLists.data.categories.get(1).description;
                name = initMemberPrizeLists.data.categories.get(1).name;
                showInfoDialog(description, name);
                break;

            case R.id.btn3_register_barcode:
                description = initMemberPrizeLists.data.categories.get(2).description;
                name = initMemberPrizeLists.data.categories.get(2).name;
                showInfoDialog(description, name);
                break;

            case R.id.btn4_register_barcode:
                description = initMemberPrizeLists.data.categories.get(3).description;
                name = initMemberPrizeLists.data.categories.get(3).name;
                showInfoDialog(description, name);
                break;

            case R.id.btn5_register_barcode:
                description = initMemberPrizeLists.data.categories.get(4).description;
                name = initMemberPrizeLists.data.categories.get(4).name;
                showInfoDialog(description, name);
                break;

            case R.id.btn6_register_barcode:
                description = initMemberPrizeLists.data.categories.get(5).description;
                name = initMemberPrizeLists.data.categories.get(5).name;
                showInfoDialog(description, name);
                break;

            case R.id.btn7_register_barcode:
                description = initMemberPrizeLists.data.categories.get(6).description;
                name = initMemberPrizeLists.data.categories.get(6).name;
                showInfoDialog(description, name);
                break;

            case R.id.btn8_register_barcode:
                description = initMemberPrizeLists.data.categories.get(7).description;
                name = initMemberPrizeLists.data.categories.get(7).name;
                showInfoDialog(description, name);
                break;

            case R.id.btn_finish_purchase_qrcode:
                startActivity(new Intent(QRcodeActivity1.this, MainActivity.class));
                finish();
                break;

            case R.id.btn_unreadable_barcode:
                Intent intent = new Intent(QRcodeActivity1.this, PurchasedItemActivityNew.class);
                intent.putExtra("unreadable_barcode", "unreadable_barcode");
                startActivity(intent);
                finish();
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

        dialogFactory = new DialogFactory(QRcodeActivity1.this);
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
        DialogFactory dialogFactory = new DialogFactory(QRcodeActivity1.this);
        dialogFactory.createBarcodeResultListDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

                Intent intent = new Intent(QRcodeActivity1.this, PurchasedItemActivityNew.class);
                intent.putExtra("unreadable_barcode", "unreadable_barcode");
                startActivity(intent);
                finish();

            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {
//                onResume();
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
    protected void onDestroy() {
        unregisterReceiver(connectivityReceiver);
        super.onDestroy();
    }

    @Override
    public void barcodeListOnClicked(@NotNull BarcodeData model, int position, @NotNull Barcode barcode, @NotNull AlertDialog dialog) {

        Intent intent = new Intent(QRcodeActivity1.this, PurchasedItemActivityNew.class);
        intent.putExtra("position", position);
        intent.putExtra("barcodeList", barcode);
        intent.putExtra("product_id",barcode.getData().get(position).getId());
        intent.putExtra("mygroup",barcode.getData().get(position).getMygroup());
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
