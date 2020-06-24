package com.rahbarbazaar.shopper.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.models.activelist.ActiveListData;
import com.rahbarbazaar.shopper.models.api_error.ErrorUtils;
import com.rahbarbazaar.shopper.models.api_error206.APIError406;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_create.DashboardCreateData;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_home.HomeData;
import com.rahbarbazaar.shopper.models.latlng.LatLng;
import com.rahbarbazaar.shopper.models.register.RegisterModel;
import com.rahbarbazaar.shopper.models.shopping_edit.ShoppingEdit;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.ui.activities.HistoryActivity;
import com.rahbarbazaar.shopper.ui.activities.HtmlLoaderActivity;
import com.rahbarbazaar.shopper.ui.activities.MainActivity;
import com.rahbarbazaar.shopper.ui.activities.NewRegisterActivity;
import com.rahbarbazaar.shopper.ui.activities.NewRegisterListActivity;
import com.rahbarbazaar.shopper.utilities.Cache;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.GpsTracker;
import com.rahbarbazaar.shopper.utilities.RxBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private GpsTracker gpsTracker;
    Disposable disposable = new CompositeDisposable();
    DashboardCreateData dashboardCreateData;
    CardView crd_purchases;
    ImageView img_myshop;
    TextView txt_balance, txt_papasi, txt_total_purchase, txt_left_days,txt_msg;

    ActiveListData activeListData;

    RelativeLayout rl_avi_dashboard_new_register;
    LinearLayout rl_root;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable = RxBus.DashboardModel.subscribeDashboardModel(result -> {
            if (result instanceof DashboardCreateData) {
                dashboardCreateData = (DashboardCreateData) result;

            }
        });


    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Cache.setString(getContext(), "user_name", dashboardCreateData.data.userName);
        Cache.setString(getContext(), "share_url", dashboardCreateData.data.shareUrl);

        initViews(view);
        setContentView();
//        getNewPurchaseList0();


        return view;
    }



    private void initViews(View view) {
        crd_purchases = view.findViewById(R.id.crd_purchases);
        img_myshop = view.findViewById(R.id.img_myshop);
        txt_balance = view.findViewById(R.id.txt_balance);
        txt_papasi = view.findViewById(R.id.txt_papasi);
        txt_msg = view.findViewById(R.id.txt_msg);
        txt_left_days = view.findViewById(R.id.txt_left_days);
        rl_avi_dashboard_new_register = view.findViewById(R.id.rl_avi_dashboard_new_register);
        rl_root =view.findViewById(R.id.homeFragment1);
        crd_purchases.setOnClickListener(this);
    }

    private void setContentView() {

        Glide.with(getActivity())
                .load(dashboardCreateData.data.myshop_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .centerCrop()
                .into(img_myshop);

        txt_balance.setText(dashboardCreateData.data.one);
        txt_papasi.setText(dashboardCreateData.data.two);
        txt_left_days.setText(dashboardCreateData.data.four);
        txt_msg.setText(dashboardCreateData.data.board);
    }

//    private void getNewPurchaseList0() {
//
//        Service service = new ServiceProvider(getContext()).getmService();
//        Call<ActiveListData> call = service.getActiveList(0);
//        call.enqueue(new Callback<ActiveListData>() {
//            @Override
//            public void onResponse(Call<ActiveListData> call, Response<ActiveListData> response) {
//                if (response.code() == 200) {
//
//                    activeListData = new ActiveListData();
//                    activeListData = response.body();
//                    RxBus.ActiveList0.publishActiveList0(activeListData);
//
//                } else if (response.code() == 204) {
//                    // send zero item
//                    activeListData = new ActiveListData();
//                    RxBus.ActiveList0.publishActiveList0(activeListData);
//
//                } else {
//                    Toast.makeText(getContext(), "" +getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ActiveListData> call, Throwable t) {
//                Toast.makeText(getContext(), "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.crd_purchases:
//                getContext().startActivity(new Intent(getContext(), NewRegisterListActivity.class));
//                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                requestRegistration();
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

    private void gpsDialog() {

        Toast.makeText(getContext(), "برای ثبت خرید لازم است GPS خود را روشن نمایید, صبور باشید ...", Toast.LENGTH_LONG).show();
        //     show waiting AVI
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setNumUpdates(2);

        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        builder.setNeedBle(true);
        SettingsClient client = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), locationSettingsResponse -> {
            hasLocationPermission();
            sendLatLng();
        });
        task.addOnFailureListener(getActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(getActivity(),
                            12);
                } catch (IntentSender.SendIntentException e1) {

                    e.printStackTrace();
                }
            }
        });
    }

    String strLat, strLng;

    private void sendLatLng() {

        rl_avi_dashboard_new_register.setVisibility(View.VISIBLE);

        getLocation();

        String gps_avi_loading = "show_loading";
        EventBus.getDefault().postSticky(gps_avi_loading);

        Service service = new ServiceProvider(getContext()).getmService();
        Call<LatLng> call = service.latLng(strLat, strLng);
        call.enqueue(new Callback<LatLng>() {
            @Override
            public void onResponse(Call<LatLng> call, Response<LatLng> response) {
                if (response.code() == 200) {
                    Boolean validate = response.body().data;
                    String validate_area = String.valueOf(response.body().data);

                    Cache.setString(getContext(), "lat", strLat);
                    Cache.setString(getContext(), "lng", strLng);
                    Cache.setString(getContext(), "validate_area", validate_area);

                    if (validate) {
                        getNewRegisterData();
                    } else {
                        rl_avi_dashboard_new_register.setVisibility(View.GONE);
                        outOfAreaDialog();

                    }

                } else {
                    rl_avi_dashboard_new_register.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LatLng> call, Throwable t) {
                rl_avi_dashboard_new_register.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }

    int a = 0;

    public void getLocation() {
        gpsTracker = new GpsTracker(getContext());
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            strLat = (String.valueOf(latitude));
            strLng = (String.valueOf(longitude));

            // to handle getting gps in first calculate after turning on gps
            if (a < 2) {
                a++;
                getLocation();
            }

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void outOfAreaDialog() {
        DialogFactory dialogFactory = new DialogFactory(getActivity());
        dialogFactory.createOutOfAreaDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... strings) {

                getNewRegisterData();
            }

            @Override
            public void onDeniedButtonClicked(boolean cancel_dialog) {

            }
        }, rl_root);
    }

    private void getNewRegisterData() {
        Service service = new ServiceProvider(getContext()).getmService();
        Call<RegisterModel> call = service.getRegisterData();
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {


                if (response.code() == 200) {


                    String gps_avi_loading = "hide_loading";
                    EventBus.getDefault().postSticky(gps_avi_loading);
                    // publish null
                    ShoppingEdit shoppingEdit = new ShoppingEdit();
                    RxBus.ShoppingEdit.publishShoppingEdit(shoppingEdit);

                    RegisterModel registerModel;
                    registerModel = response.body();
                    RxBus.RegisterModel.publishRegisterModel(registerModel);
                    startActivity(new Intent(getContext(), NewRegisterActivity.class));
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    rl_avi_dashboard_new_register.setVisibility(View.GONE);

                } else if (response.code() == 406) {
                    rl_avi_dashboard_new_register.setVisibility(View.GONE);
                    APIError406 apiError = ErrorUtils.parseError406(response);
                    showError406Dialog(apiError.message);

                } else {
                    rl_avi_dashboard_new_register.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                rl_avi_dashboard_new_register.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
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
        }, rl_root, message);
    }

    private void askLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGpsON() {
        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

//    private void goToHtmlActivity(String url) {
//        Intent intent = new Intent(getContext(), HtmlLoaderActivity.class);
//        intent.putExtra("url", url);
//        startActivity(intent);
//        Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//    }
//    private void getRefreshHomeData() {
//        Service service = new ServiceProvider(getContext()).getmService();
//        Call<HomeData> call = service.getRefreshHomeData();
//        call.enqueue(new Callback<HomeData>() {
//            @Override
//            public void onResponse(Call<HomeData> call, Response<HomeData> response) {
//
//                if (response.code() == 200) {
//
//                    txt_balance.setText(response.body().data.one);
//                    txt_papasi.setText(response.body().data.two);
//                    txt_left_days.setText(response.body().data.four);
//
//                } else {
//                    Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HomeData> call, Throwable t) {
//                Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }





    @Override
    public void onResume() {
        super.onResume();
//        getRefreshHomeData();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose(); //very important
    }


}
