package com.rahbarbazaar.shopper.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mScannerView = new ZXingScannerView(getActivity());  // Programmatically initialize the scanner view
        return mScannerView;// Set the scanner view as the content view

    }


    @Override
    public void handleResult(Result rawResult) {
//        SecondActivity.ResultScan=rawResult.getText();
//        startActivity(new Intent(getContext(), SecondActivity.class));

        Toast.makeText(getContext(), ""+rawResult.getText(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
