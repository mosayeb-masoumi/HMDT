package com.rahbarbaazar.checkpanel.network;


import com.rahbarbaazar.checkpanel.models.activelist.ActiveListData;
import com.rahbarbaazar.checkpanel.models.barcodlist.Barcode;
import com.rahbarbaazar.checkpanel.models.dashboard.DashboardModel;
import com.rahbarbaazar.checkpanel.models.latlng.LatLng;
import com.rahbarbaazar.checkpanel.models.login.LoginModel;
import com.rahbarbaazar.checkpanel.models.refresh.RefreshTokenModel;
import com.rahbarbaazar.checkpanel.models.register.GetShopId;
import com.rahbarbaazar.checkpanel.models.register.RegisterModel;
import com.rahbarbaazar.checkpanel.models.register.SendRegisterTotalData;
import com.rahbarbaazar.checkpanel.models.shopping_edit.SendUpdateTotalData;
import com.rahbarbaazar.checkpanel.models.shopping_edit.ShoppingEdit;
import com.rahbarbaazar.checkpanel.models.verify.VerifyModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    //    @POST("Login/Login")
//    Single<LoginModel> login(@Query("mobile") String mobile);
    @POST("Login/Login")
    Call<LoginModel> login(@Query("mobile") String mobile);


    @POST("Login/Verify")
    Call<VerifyModel> verify(@Query("code") String code);


    @GET("Dashboard/Create")
    Call<DashboardModel> getDashboardData();

    @POST("Login/Refresh")
    Call<RefreshTokenModel> refreshToken(@Query("access_token") String access_token,
                                         @Query("refresh_token") String refresh_token);

    @GET("Shopping/Form")
    Call<RegisterModel> getRegisterData();

    @POST("Shopping/Create")
    Call<GetShopId> registerNewShop(@Body SendRegisterTotalData sendRegisterTotalData);

    @POST("Shopping/ActiveIndex")
    Call<ActiveListData> getActiveList(@Query("page") Integer page);


    @POST("Shopping/Latlng")
    Call<LatLng> latLng(@Query("lat") String lat,
                        @Query("lng") String lng);

    @POST("Shopping/Edit")
    Call<ShoppingEdit> getShoppingEdit(@Query("shopping_id") String shopping_id);


    @POST("Shopping/Update")
    Call<GetShopId> update(@Body SendUpdateTotalData sendUpdateTotalData);


    @POST("Product/Barcode")
    Call<Barcode> getBarcodeList(@Query("barcode") String barcode);
}
