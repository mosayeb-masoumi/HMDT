package com.rahbarbazaar.shopper.ui.fragments;

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
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.utilities.RxBus;
import org.greenrobot.eventbus.EventBus;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    String barcode;

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

        barcode = rawResult.getText();

        getListOfProducts(barcode);

    }

    private void getListOfProducts(String barcode) {

        BarcodeLoadingState state = new BarcodeLoadingState();
        state.setState("show_loading");
        EventBus.getDefault().postSticky(state);

        Service service = new ServiceProvider(getContext()).getmService();
        Call<Barcode> call = service.getBarcodeList(barcode);
        call.enqueue(new Callback<Barcode>() {
            @Override
            public void onResponse(Call<Barcode> call, Response<Barcode> response) {
                if(response.code()==200){
                    Barcode barcode = new Barcode();
                    barcode = response.body();



                    RxBus.BarcodeList.publishBarcodeList(barcode);

                    int size = barcode.getData().size();

                    if(size >1){

                        state.setState("stop_loading");
                        EventBus.getDefault().postSticky(state);
                        EventBus.getDefault().post(barcode);
                        onResume();
                    }else if(size==1){
//                        startActivity(new Intent(getContext(),BarcodeListActivity.class));
//                        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                        getActivity().finish();


                        state.setState("stop_loading");
                        EventBus.getDefault().postSticky(state);
                        EventBus.getDefault().post(barcode);

                        onResume();
                    }



                }else if(response.code()==204){

//                    Intent intent = new Intent(getContext(), PurchasedItemActivityNew.class);
//                    intent.putExtra("no_product", "no_product");
//                    intent.putExtra("barcode", barcode);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                    getActivity().finish();

                    Toast.makeText(getContext(), ""+getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().postSticky("stop_loading");


                }else{
                    Toast.makeText(getContext(), ""+getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().postSticky("stop_loading");
                    onResume();
                }
            }

            @Override
            public void onFailure(Call<Barcode> call, Throwable t) {
                Toast.makeText(getContext(), ""+getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
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
