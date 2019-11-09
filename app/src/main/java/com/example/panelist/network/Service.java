package com.example.panelist.network;


import com.example.panelist.models.login.LoginModel;

import io.reactivex.Single;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @POST("Login/Login")
    Single<LoginModel> login(@Query("mobile") String mobile);
}
