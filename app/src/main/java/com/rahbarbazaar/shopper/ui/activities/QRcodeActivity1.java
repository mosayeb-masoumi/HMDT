package com.rahbarbazaar.shopper.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.ui.fragments.ScanFragment;

public class QRcodeActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode1);

        ScanFragment scanFragment = new ScanFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frg_holder, scanFragment);
        ft.commit();

    }
}
