package com.example.panelist.network;


import com.example.panelist.models.dashboard.DashboardModel;
import com.example.panelist.models.login.LoginModel;
import com.example.panelist.models.refresh.RefreshTokenModel;
import com.example.panelist.models.verify.VerifyModel;

import retrofit2.Call;
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

}
