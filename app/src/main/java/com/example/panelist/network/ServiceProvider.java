package com.example.panelist.network;


import android.content.Context;
import android.content.Intent;

import com.example.panelist.utilities.App;
import com.example.panelist.utilities.Cache;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceProvider {

    private Service mService;

    public ServiceProvider(Context context) {

        //config client and retrofit:

        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
        clientBuilder.connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        clientBuilder.cache(null);


        if (!Cache.getString("token").equals("")) {
            clientBuilder.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + Cache.getString("email"))
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            });
        }else{

            // for error handling in login request
            clientBuilder.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            });
        }


        //error handlong
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);

//                int a = response.code();
                return response;
            }
        }).build();



//        // handle error 401
//        clientBuilder.authenticator(new Authenticator() {
//
//            @Override
//            public Request authenticate(Route route, Response response) throws IOException {
//                //check if there is mobilephone
//
////                String phone = PreferenceStorage.getInstance(context).retrivePhone();
////                if (!phone.equals("0")) {
//
//                Service service = new ServiceProvider(context).mService;
//                Call<RefreshToken> call = service.requsetRefreshToken(ClientConfig.API_V1);
//                retrofit2.Response<RefreshToken> tokenModelResponse = call.execute();
//                //sync request
//                if (tokenModelResponse.isSuccessful()) {
//                    //save token
//                    PreferenceStorage.getInstance(context).saveToken(tokenModelResponse.body().getToken());
//                    return response.request().newBuilder()
//                            .removeHeader("lang")
//                            .removeHeader("token")
//                            .addHeader("lang", storage.retriveLanguage())
//                            .addHeader("token", PreferenceStorage.getInstance(context).retriveToken())
//                            .build();
//                } else {
//                    return null;
//                }
//
////                } else {
////                    return null;
////                }
//            }
//        });


//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(App.ServerURL).client(clientBuilder.build()).
                addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

        mService = retrofit.create(Service.class);
    }

    public Service getmService() {
        return mService;
    }

}
