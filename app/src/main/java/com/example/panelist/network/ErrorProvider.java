package com.example.panelist.network;

import com.example.panelist.utilities.App;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ErrorProvider {
    public static Retrofit.Builder builder =new Retrofit.Builder()
            .baseUrl(App.ServerURL)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit = builder.build();
}
