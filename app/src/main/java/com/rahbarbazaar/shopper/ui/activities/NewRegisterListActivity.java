package com.rahbarbazaar.shopper.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.adapters.ActiveListAdapter;
import com.rahbarbazaar.shopper.controllers.interfaces.ActiveListItemInteraction;
import com.rahbarbazaar.shopper.models.activelist.ActiveListData;
import com.rahbarbazaar.shopper.models.activelist.ActiveListModel;
import com.rahbarbazaar.shopper.models.api_error.ErrorUtils;
import com.rahbarbazaar.shopper.models.api_error206.APIError406;
import com.rahbarbazaar.shopper.models.history.HistoryData;
import com.rahbarbazaar.shopper.models.latlng.LatLng;
import com.rahbarbazaar.shopper.models.register.RegisterModel;
import com.rahbarbazaar.shopper.models.shopping_edit.ShoppingEdit;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.utilities.Cache;
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.DownloadManager;
import com.rahbarbazaar.shopper.utilities.GeneralTools;
import com.rahbarbazaar.shopper.utilities.GpsTracker;
import com.rahbarbazaar.shopper.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRegisterListActivity extends CustomBaseActivity implements View.OnClickListener , ActiveListItemInteraction {

//    Disposable disposable = new CompositeDisposable();

    private GpsTracker gpsTracker;
    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    String strLat, strLng ,shopping_id;
    AVLoadingIndicatorView avi,avi_load_list;
    RecyclerView recyclerView;
    ActiveListAdapter adapter;
    ActiveListData activeListData = new ActiveListData();
    List<ActiveListModel> activeListModel ;

    LinearLayout ll_return_newRegisterList;
    RelativeLayout root_new_register_list, rl_btn_register;
    TextView txt_no_shop;

    LinearLayoutManager linearLayoutManager;
    Boolean isScrolling = false;
    int totalPage = 0;
    int page = 0;
    int currentItems, totalItems, scrollOutItems;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register_list);


        //check network broadcast reciever
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(NewRegisterListActivity.this, findViewById(R.id.root_new_register_list));
            }
        };


//        disposable = RxBus.ActiveList0.subscribeActiveList0(result -> {
//            if (result instanceof ActiveListData) {
//                activeListData = (ActiveListData) result;
//            }
//        });


        initView();

//        if(activeListData.data==null){
//            txt_no_shop.setVisibility(View.VISIBLE);
//        }else{
//            txt_no_shop.setVisibility(View.GONE);
//            page=0;
//            setRecyclerview(activeListData);
//        }
//
    }

    private void initView() {
        avi = findViewById(R.id.avi);
        avi_load_list = findViewById(R.id.avi_loading_fr_register);
        recyclerView = findViewById(R.id.recyclere_register_fragment);
        root_new_register_list = findViewById(R.id.root_new_register_list);
        rl_btn_register = findViewById(R.id.rl_btn_register);
        txt_no_shop = findViewById(R.id.txt_no_shop);
        ll_return_newRegisterList = findViewById(R.id.ll_return_newRegisterList);

        rl_btn_register.setOnClickListener(this);
        ll_return_newRegisterList.setOnClickListener(this);
    }

    private void getActiveList(int page) {

        avi_load_list.setVisibility(View.VISIBLE);

        Service service = new ServiceProvider(this).getmService();
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
                    }

                } else {
                    avi_load_list.setVisibility(View.GONE);
                    Toast.makeText(NewRegisterListActivity.this, "" +getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ActiveListData> call, Throwable t) {
                avi_load_list.setVisibility(View.GONE);
                Toast.makeText(NewRegisterListActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setRecyclerview(ActiveListData activeListData) {
        totalPage = activeListData.total;
        if (page == 0) {
            activeListModel.clear();
        }

        activeListModel.addAll(activeListData.data);

        linearLayoutManager = new LinearLayoutManager(NewRegisterListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ActiveListAdapter(activeListModel, NewRegisterListActivity.this);
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

            case R.id.ll_return_newRegisterList:
                finish();
                break;

        }
    }

    private void requestRegistration() {

        if (hasLocationPermission()) {
            if (checkGpsON()) {
                sendLatLng();
            } else {
                gpsDialog();
            }
        } else {
            askLocationPermission();
        }
    }


    private void sendLatLng() {

        rl_btn_register.setVisibility(View.GONE);
        avi.setVisibility(View.VISIBLE);

        getLocation();

        Service service = new ServiceProvider(this).getmService();
        Call<LatLng> call = service.latLng(strLat, strLng);
        call.enqueue(new Callback<LatLng>() {
            @Override
            public void onResponse(Call<LatLng> call, Response<LatLng> response) {
                if (response.code() == 200) {
                    Boolean validate = response.body().data;
                    String validate_area = String.valueOf(response.body().data);

                    Cache.setString(NewRegisterListActivity.this,"lat", strLat);
                    Cache.setString(NewRegisterListActivity.this,"lng", strLng);
                    Cache.setString(NewRegisterListActivity.this,"validate_area", validate_area);

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
                    Toast.makeText(NewRegisterListActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LatLng> call, Throwable t) {
                rl_btn_register.setVisibility(View.VISIBLE);
                avi.setVisibility(View.GONE);
                Toast.makeText(NewRegisterListActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void outOfAreaDialog() {
        DialogFactory dialogFactory = new DialogFactory(this);
        dialogFactory.createOutOfAreaDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... strings) {
                getNewRegisterData();
            }

            @Override
            public void onDeniedButtonClicked(boolean cancel_dialog) {

            }
        }, root_new_register_list);
    }


    private void getNewRegisterData() {
        Service service = new ServiceProvider(this).getmService();
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
                   startActivity(new Intent(NewRegisterListActivity.this, NewRegisterActivity.class));
                   overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    hideLoading();

                } else if (response.code() == 406) {
                    APIError406 apiError = ErrorUtils.parseError406(response);
                    showError406Dialog(apiError.message);

                    hideLoading();
                } else {
                    Toast.makeText(NewRegisterListActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Toast.makeText(NewRegisterListActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        });
    }

    private void showError406Dialog(String message) {
        //initial Dialog factory
        DialogFactory dialogFactory = new DialogFactory(this);
        dialogFactory.createError406Dialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {
            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {
            }
        }, root_new_register_list , message);
    }


    private void hideLoading() {
        rl_btn_register.setVisibility(View.VISIBLE);
        avi.setVisibility(View.GONE);
    }


    private void gpsDialog() {
        //     show waiting AVI
        Toast.makeText(this, "برای ثبت خرید لازم است GPS خود را روشن نمایید, صبور باشید ...", Toast.LENGTH_SHORT).show();
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setNumUpdates(2);

        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        builder.setNeedBle(true);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, locationSettingsResponse -> {
            hasLocationPermission();
            sendLatLng();
        });
        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this,
                            12);
                } catch (IntentSender.SendIntentException e1) {

                    e.printStackTrace();
                }
            }
        });
    }


    private void askLocationPermission() {
        ActivityCompat.requestPermissions((Activity) Objects.requireNonNull(this)
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGpsON() {
        final LocationManager manager = (LocationManager) Objects.requireNonNull(this).getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    int a = 0;
    public void getLocation() {
        gpsTracker = new GpsTracker(this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            strLat = (String.valueOf(latitude));
            strLng = (String.valueOf(longitude));
            // to handle getting gps in first calculate after turning on gps
            if(a < 2){
                a ++;
                getLocation();
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    @Override
    public void activeListOnClicked(String title, String id, String action) {
        shopping_id = id;
        Cache.setString(this,"shopping_id",shopping_id);
        if(action.equals("edit_shop")){
            getShoppingEditInfo(shopping_id);
        }else if(action.equals("register")){

            if (cameraPermissionGranted()) {
                Intent intent = new Intent(this, QRcodeActivity.class);
                Cache.setString(this,"shopping_id",shopping_id);
                intent.putExtra("static_barcode","static_barcode");
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            } else {
                askCameraPermission();
            }

        }else if(action.equals("edit_product")){
            Intent intent = new Intent(this, EditProductsActivity.class);
            intent.putExtra("shopping_id",shopping_id);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }


    private boolean cameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 25);
    }


    private void getShoppingEditInfo(String id) {

        Service service = new ServiceProvider(this).getmService();
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
                    Cache.setString(NewRegisterListActivity.this,"shopping_id",id);
                    RxBus.ShoppingEdit.publishShoppingEdit(shoppingEdit);
                    startActivity(new Intent(NewRegisterListActivity.this, NewRegisterActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    hideLoading();

                } else if (response.code() == 204) {
                    Toast.makeText(NewRegisterListActivity.this, "" + getResources().getString(R.string.error204), Toast.LENGTH_SHORT).show();
                    hideLoading();
                } else {
                    Toast.makeText(NewRegisterListActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<ShoppingEdit> call, Throwable t) {
                Toast.makeText(NewRegisterListActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                hasLocationPermission();
                sendLatLng();
            } else {
                //User clicks No
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 3:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkGpsON()) {
                        sendLatLng();
                    } else {
                        gpsDialog();
                    }
                }

            case 25:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(this, QRcodeActivity.class);
                    Cache.setString(this,"shopping_id",shopping_id);
                    intent.putExtra("static_barcode","static_barcode");
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String gps_avi_loading){

        if(gps_avi_loading.equals("show_loading")){
            rl_btn_register.setVisibility(View.GONE);
            avi.setVisibility(View.VISIBLE);

        }else if(gps_avi_loading.equals("hide_loading")){
            rl_btn_register.setVisibility(View.VISIBLE);
            avi.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        activeListModel = new ArrayList<>();
        getActiveList(page);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityReceiver);
        EventBus.getDefault().unregister(this);
//        disposable.dispose();
    }
}
