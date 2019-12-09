package com.example.panelist.network;


import com.example.panelist.models.activelist.ActiveList;
import com.example.panelist.models.activelist.ActiveListData;
import com.example.panelist.models.dashboard.DashboardModel;
import com.example.panelist.models.latlng.LatLng;
import com.example.panelist.models.login.LoginModel;
import com.example.panelist.models.refresh.RefreshTokenModel;
import com.example.panelist.models.register.GetShopId;
import com.example.panelist.models.register.RegisterModel;
import com.example.panelist.models.register.SendRegisterTotalData;
import com.example.panelist.models.verify.VerifyModel;

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

}
