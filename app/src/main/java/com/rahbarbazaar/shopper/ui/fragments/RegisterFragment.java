package com.rahbarbazaar.shopper.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.adapters.ActiveListAdapter;
import com.rahbarbazaar.shopper.controllers.interfaces.ActiveListItemInteraction;
import com.rahbarbazaar.shopper.models.activelist.ActiveListModel;
import com.rahbarbazaar.shopper.models.activelist.ActiveListData;
import com.rahbarbazaar.shopper.models.api_error.ErrorUtils;
import com.rahbarbazaar.shopper.models.api_error206.APIError406;
import com.rahbarbazaar.shopper.models.latlng.LatLng;
import com.rahbarbazaar.shopper.models.register.RegisterModel;
import com.rahbarbazaar.shopper.models.shopping_edit.ShoppingEdit;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.ui.activities.EditProductsActivity;
import com.rahbarbazaar.shopper.ui.activities.NewRegisterActivity;
import com.rahbarbazaar.shopper.ui.activities.QRcodeActivity;
import com.rahbarbazaar.shopper.utilities.Cache;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.GpsTracker;
import com.rahbarbazaar.shopper.utilities.RxBus;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener , ActiveListItemInteraction {

    private GpsTracker gpsTracker;
    String strLat, strLng;
    AVLoadingIndicatorView avi,avi_load_list;
    RecyclerView recyclerView;
    ActiveListAdapter adapter;
    ActiveListData activeListData = new ActiveListData();

    RelativeLayout rl_fr_register, rl_btn_register;
    TextView txt_no_shop;

    LinearLayoutManager linearLayoutManager;
    Boolean isScrolling = false;
    int totalPage = 0;
    int page = 0;
    int currentItems, totalItems, scrollOutItems;

    List<ActiveListModel> activeListModel ;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {

        avi = view.findViewById(R.id.avi);
        avi_load_list = view.findViewById(R.id.avi_loading_fr_register);
        recyclerView = view.findViewById(R.id.recyclere_register_fragment);
        rl_fr_register = view.findViewById(R.id.rl_fr_register);
        rl_btn_register = view.findViewById(R.id.rl_btn_register);
        txt_no_shop = view.findViewById(R.id.txt_no_shop);
        rl_btn_register.setOnClickListener(this);
    }

    private void getActiveList(int page) {

        avi_load_list.setVisibility(View.VISIBLE);

        Service service = new ServiceProvider(getContext()).getmService();
        Call<ActiveListData> call = service.getActiveList(page);
        call.enqueue(new Callback<ActiveListData>() {
            @Override
            public void onResponse(Call<ActiveListData> call, Response<ActiveListData> response) {
                if (response.code() == 200) {

                    avi_load_list.setVisibility(View.GONE);
                    txt_no_shop.setVisibility(View.GONE);
                    activeListData = response.body();
                    setRecyclerview(activeListData);
                } else if (response.code() == 204) {
                    avi_load_list.setVisibility(View.GONE);
                    if (page == 0) {
                        txt_no_shop.setVisibility(View.VISIBLE);
                    } else {
                        txt_no_shop.setVisibility(View.GONE);
//                       Toast.makeText(getContext(), "پایان لیست", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    avi_load_list.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ActiveListData> call, Throwable t) {
                avi_load_list.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setRecyclerview(ActiveListData activeListData) {


        totalPage = activeListData.total;

        // todo check below clause
        // to clear list ites of the fragment for first time
        if (page == 0) {
            activeListModel.clear();
        }


        linearLayoutManager = new LinearLayoutManager(getContext());
        // to show list of member items

//        for (int i = 0; i < activeListData.data.size(); i++) {
//            activeListModel.add(new ActiveListModel(activeListData.data.get(i).id, activeListData.data.get(i).date
//                    , activeListData.data.get(i).title));
//        }


        activeListModel.addAll(activeListData.data);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ActiveListAdapter(activeListModel, getContext());
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);  // important to set or else the app will crashed
        adapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {

                    isScrolling = false;
                    page++;


                    if(page<=totalPage){
                        //data fetch
                          getActiveList(page);
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rl_btn_register:

                requestRegistration();

                break;
        }
    }

    private void requestRegistration() {

        if (checkGpsPermission()) {
            if (checkGpsON()) {

                sendLatLng();
//                        getNewRegisterData();
            } else {
                displayLocationSettingsRequest(getContext(), 123);
            }
        } else {
            askGpsPermission();
        }
    }

    private void sendLatLng() {

        rl_btn_register.setVisibility(View.GONE);
        avi.setVisibility(View.VISIBLE);

        getLocation();

        Service service = new ServiceProvider(getContext()).getmService();
        Call<LatLng> call = service.latLng(strLat, strLng);
        call.enqueue(new Callback<LatLng>() {
            @Override
            public void onResponse(Call<LatLng> call, Response<LatLng> response) {
                if (response.code() == 200) {
                    Boolean validate = response.body().data;
                    String validate_area = String.valueOf(response.body().data);

                    Cache.setString(getContext(),"lat", strLat);
                    Cache.setString(getContext(),"lng", strLng);
                    Cache.setString(getContext(),"validate_area", validate_area);


                    if (validate) {
                        getNewRegisterData();
                    } else {
                        outOfAreaDialog();
                        rl_btn_register.setVisibility(View.VISIBLE);
                        avi.setVisibility(View.GONE);
                    }


                } else {
                    rl_btn_register.setVisibility(View.VISIBLE);
                    avi.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LatLng> call, Throwable t) {
                rl_btn_register.setVisibility(View.VISIBLE);
                avi.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void outOfAreaDialog() {
        DialogFactory dialogFactory = new DialogFactory(getContext());
        dialogFactory.createOutOfAreaDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... strings) {
                getNewRegisterData();
            }

            @Override
            public void onDeniedButtonClicked(boolean cancel_dialog) {

            }
        }, rl_fr_register);
    }

    private void getNewRegisterData() {



        Service service = new ServiceProvider(getContext()).getmService();
        Call<RegisterModel> call = service.getRegisterData();
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (response.code() == 200) {

                    // publish null
                    ShoppingEdit shoppingEdit = new ShoppingEdit();
                    RxBus.ShoppingEdit.publishShoppingEdit(shoppingEdit);

                    RegisterModel registerModel;
                    registerModel = response.body();
                    RxBus.RegisterModel.publishRegisterModel(registerModel);
                    getContext().startActivity(new Intent(getContext(), NewRegisterActivity.class));
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    hideLoading();

                } else if (response.code() == 406) {
                    APIError406 apiError = ErrorUtils.parseError406(response);
                    showError406Dialog(apiError.message);

                    hideLoading();
                } else {
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Toast.makeText(getContext(), "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        });
    }

    private void showError406Dialog(String message) {
        //initial Dialog factory
        DialogFactory dialogFactory = new DialogFactory(getContext());
        dialogFactory.createError406Dialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_fr_register , message);
    }



    private void hideLoading() {
        rl_btn_register.setVisibility(View.VISIBLE);
        avi.setVisibility(View.GONE);
    }

    private void displayLocationSettingsRequest(Context context, int requestCode) {


        rl_btn_register.setVisibility(View.GONE);
        avi.setVisibility(View.VISIBLE);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
                try {
                    status.startResolutionForResult((Activity) context, requestCode);

                    rl_btn_register.setVisibility(View.VISIBLE);
                    avi.setVisibility(View.GONE);

                } catch (IntentSender.SendIntentException ignored) {

                }
        });

    }




    private void askGpsPermission() {
        ActivityCompat.requestPermissions((Activity) Objects.requireNonNull(getContext())
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
    }

    private boolean checkGpsPermission() {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGpsON() {
        final LocationManager manager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 3:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkGpsON()) {
                    } else {
                        displayLocationSettingsRequest(getContext(), 123);
                    }
                } else {


                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    public void getLocation() {
        gpsTracker = new GpsTracker(getContext());
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            strLat = (String.valueOf(latitude));
            strLng = (String.valueOf(longitude));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void activeListOnClicked(String title , String id,String action) {

        if(action.equals("edit_shop")){

            getShoppingEditInfo(id);

        }else if(action.equals("register")){

            Intent intent = new Intent(getContext(), QRcodeActivity.class);
//            intent.putExtra("shopping_id",id);
            Cache.setString(getContext(),"shopping_id",id);
            intent.putExtra("static_barcode","static_barcode");
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//            getMemberPrizeLists();


        }else if(action.equals("edit_product")){

            Intent intent = new Intent(getContext(), EditProductsActivity.class);
            intent.putExtra("shopping_id",id);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }

//    private void getMemberPrizeLists() {
//
//        Intent intent = new Intent(getContext(), QRcodeActivity.class);
////            intent.putExtra("shopping_id",id);
//        Cache.setString(getContext(),"shopping_id",id);
//        intent.putExtra("static_barcode","static_barcode");
//        startActivity(intent);
//        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//    }

    private void getShoppingEditInfo(String id) {

        Service service = new ServiceProvider(getContext()).getmService();
        Call<ShoppingEdit> call = service.getShoppingEdit(id);
        call.enqueue(new Callback<ShoppingEdit>() {
            @Override
            public void onResponse(Call<ShoppingEdit> call, Response<ShoppingEdit> response) {
                if (response.code() == 200) {

                    //publish null
                    RegisterModel registerModel  =new RegisterModel();
                    RxBus.RegisterModel.publishRegisterModel(registerModel);

                    ShoppingEdit shoppingEdit;
                    shoppingEdit = response.body();

//                    Cache.setString("shopping_id",id);
                    Cache.setString(getContext(),"shopping_id",id);

                    RxBus.ShoppingEdit.publishShoppingEdit(shoppingEdit);
                    getContext().startActivity(new Intent(getContext(), NewRegisterActivity.class));
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    hideLoading();

                } else if (response.code() == 204) {
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.error204), Toast.LENGTH_SHORT).show();
                    hideLoading();
                } else {
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<ShoppingEdit> call, Throwable t) {
                Toast.makeText(getContext(), "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        activeListModel = new ArrayList<>();
        getActiveList(page);
    }

}
