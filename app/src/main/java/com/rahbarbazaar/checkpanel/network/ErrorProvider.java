package com.rahbarbazaar.checkpanel.network;


import com.rahbarbazaar.checkpanel.utilities.ClientConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ErrorProvider {
    public static Retrofit.Builder builder =new Retrofit.Builder()
            .baseUrl(ClientConfig.ServerURL)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit = builder.build();
}
