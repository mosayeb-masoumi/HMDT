package com.rahbarbazaar.shopper.ui.activities;


import android.content.Intent;
import android.os.Bundle;

import com.rahbarbazaar.shopper.utilities.CustomBaseActivity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanActivity extends CustomBaseActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

//        QRCodeActivity.edtQR.setText(rawResult.getText());
        QRcodeActivity.ResultScan=rawResult.getText();
//        onBackPressed();
        startActivity(new Intent(ScanActivity.this,QRcodeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        QRcodeActivity.ResultScan="";
        startActivity(new Intent(ScanActivity.this,QRcodeActivity.class));
        finish();

    }
}
