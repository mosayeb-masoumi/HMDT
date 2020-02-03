package com.rahbarbazaar.shopper.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.ui.fragments.ScanFragment;
import com.rahbarbazaar.shopper.utilities.Cache;
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.GeneralTools;

public class QRcodeActivity1 extends CustomBaseActivity implements View.OnClickListener {

    LinearLayout linear_return_qrcode,ll_root;
    RelativeLayout rl_home_qrcode;
    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8;

    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    DialogFactory dialogFactory;

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

        initView();

        ScanFragment scanFragment = new ScanFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frg_holder, scanFragment);
        ft.commit();



    }

    private void initView() {

        linear_return_qrcode = findViewById(R.id.linear_return_qrcode);
        rl_home_qrcode=findViewById(R.id.rl_home_qrcode);
        ll_root = findViewById(R.id.root_qrcode_scanner);

        btn_1 = findViewById(R.id.btn1_register_barcode);
        btn_2 = findViewById(R.id.btn2_register_barcode);
        btn_3 = findViewById(R.id.btn3_register_barcode);
        btn_4 = findViewById(R.id.btn4_register_barcode);
        btn_5 = findViewById(R.id.btn5_register_barcode);
        btn_6 = findViewById(R.id.btn6_register_barcode);
        btn_7 = findViewById(R.id.btn7_register_barcode);
        btn_8 = findViewById(R.id.btn8_register_barcode);


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
                startActivity(new Intent(QRcodeActivity1.this,MainActivity.class));
                finish();
                break;

            case R.id.btn1_register_barcode:

                showInfoDialog();

                break;

            case R.id.btn2_register_barcode:
                showInfoDialog();

                break;

            case R.id.btn3_register_barcode:

                showInfoDialog();
                break;

            case R.id.btn4_register_barcode:
                showInfoDialog();
                break;

            case R.id.btn5_register_barcode:
                showInfoDialog();
                break;

            case R.id.btn6_register_barcode:
                showInfoDialog();
                break;

            case R.id.btn7_register_barcode:
                showInfoDialog();
                break;

            case R.id.btn8_register_barcode:
                showInfoDialog();
                break;
        }
    }

    private void showInfoDialog() {

        dialogFactory = new DialogFactory(QRcodeActivity1.this);
        dialogFactory.createQrcodeInfoBtnsDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {


            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {
            }
        }, ll_root);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(connectivityReceiver);
        super.onDestroy();
    }
}
