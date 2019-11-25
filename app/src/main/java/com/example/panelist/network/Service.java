package com.example.panelist.network;


import com.example.panelist.models.login.LoginModel;
import com.example.panelist.models.verify.VerifyModel;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    //    @POST("Login/Login")
//    Single<LoginModel> login(@Query("mobile") String mobile);
    @POST("Login/Login")
    Call<LoginModel> login(@Query("mobile") String mobile);


    @POST("Login/Verify")
    Call<VerifyModel> verify(@Query("code") String code);

}
