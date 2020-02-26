package com.rahbarbazaar.shopper.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.models.barcodlist.Barcode;
import com.rahbarbazaar.shopper.models.barcodlist.BarcodeLoadingState;
import com.rahbarbazaar.shopper.models.search_goods.GroupsData;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.ui.activities.PurchasedItemActivity;
import com.rahbarbazaar.shopper.utilities.RxBus;
import org.greenrobot.eventbus.EventBus;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    String barcode_digit;
    BarcodeLoadingState state = new BarcodeLoadingState();

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
        barcode_digit = rawResult.getText();
        getListOfProducts(barcode_digit);
    }

    private void getListOfProducts(String barcode_digit) {


        state.setState("show_loading");
        EventBus.getDefault().postSticky(state);

        Service service = new ServiceProvider(getContext()).getmService();
        Call<Barcode> call = service.getBarcodeList(barcode_digit);
        call.enqueue(new Callback<Barcode>() {
            @Override
            public void onResponse(Call<Barcode> call, Response<Barcode> response) {
                if (response.code() == 200) {
                    Barcode barcode = new Barcode();
                    barcode = response.body();

                    RxBus.BarcodeList.publishBarcodeList(barcode);

                    int size = barcode.getData().size();
                    if (size > 1) {
                        state.setState("stop_loading");
                        EventBus.getDefault().postSticky(state);
                        EventBus.getDefault().post(barcode);
                        onResume();
                    } else if (size == 1) {

                        Intent intent = new Intent(getContext(), PurchasedItemActivity.class);
                        intent.putExtra("barcodeList", barcode);
                        intent.putExtra("product_id", barcode.getData().get(0).getId());
                        intent.putExtra("mygroup", barcode.getData().get(0).getMygroup());

                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                        state.setState("stop_loading");
                        EventBus.getDefault().postSticky(state);
                        getActivity().finish();
                    }
                } else if (response.code() == 204) {
                    getSpinneList(barcode_digit);

                } else {
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    state.setState("stop_loading");
                    EventBus.getDefault().postSticky(state);
                    onResume();
                }
            }

            @Override
            public void onFailure(Call<Barcode> call, Throwable t) {
                Toast.makeText(getContext(), "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                state.setState("stop_loading");
                EventBus.getDefault().postSticky(state);
                onResume();
            }
        });
    }

    private void getSpinneList(String barcode_digit) {
        Service service = new ServiceProvider(getContext()).getmService();
        Call<GroupsData> call = service.getCategorySpnData();
        call.enqueue(new Callback<GroupsData>() {
            @Override
            public void onResponse(Call<GroupsData> call, Response<GroupsData> response) {
                if (response.code() == 200) {

                    GroupsData groupsData = new GroupsData();
                    groupsData = response.body();

                    Intent intent = new Intent(getContext(), PurchasedItemActivity.class);
                    intent.putExtra("groupsData", groupsData);
                    intent.putExtra("barcode_digit", barcode_digit);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    getActivity().finish();
                    state.setState("stop_loading");
                    EventBus.getDefault().postSticky(state);
                } else {
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    state.setState("stop_loading");
                    EventBus.getDefault().postSticky(state);
                    onResume();
                }
            }

            @Override
            public void onFailure(Call<GroupsData> call, Throwable t) {
                Toast.makeText(getContext(), "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                state.setState("stop_loading");
                EventBus.getDefault().postSticky(state);
                onResume();
            }
        });
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
