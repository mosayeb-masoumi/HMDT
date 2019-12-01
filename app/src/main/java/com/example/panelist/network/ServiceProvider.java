package com.example.panelist.network;


import android.content.Context;
import android.content.Intent;

import com.example.panelist.models.refresh.RefreshTokenModel;
import com.example.panelist.ui.activities.LoginActivity;
import com.example.panelist.utilities.Cache;
import com.example.panelist.utilities.ClientConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
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


        if (!Cache.getString("access_token").equals("")) {
            clientBuilder.addInterceptor(chain -> {

                String a = Cache.getString("access_token");
                String b = a ;

                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + Cache.getString("access_token"))
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            });
        } else {

            // for login request
            clientBuilder.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            });
        }






        // handle error 401
        clientBuilder.authenticator(new Authenticator() {

            @Override
            public Request authenticate(Route route, Response response) throws IOException {

                String access_token = Cache.getString("access_token");
                String refresh_token = Cache.getString("refresh_token");

                Service service = new ServiceProvider(context).mService;
                Call<RefreshTokenModel> call = service.refreshToken(access_token,refresh_token);
                retrofit2.Response<RefreshTokenModel> tokenModelResponse = call.execute();
                //sync request
                if (tokenModelResponse.isSuccessful()) {



                    //save token
                    Cache.setString("access_token",tokenModelResponse.body().accessToken);
                    Cache.setString("refresh_token",tokenModelResponse.body().refreshToken);
                    Cache.setInt("expireAt",tokenModelResponse.body().expireAt);

//                    String a = tokenModelResponse.body().accessToken;
//                    String b = a ;
//
//                    String c = Cache.getString("access_token");
//                    String v = c;

                    return response.request().newBuilder()
                            .removeHeader("Authorization")
                            .removeHeader("Accept")
                            .addHeader("Authorization", "Bearer " + Cache.getString("access_token"))
                            .addHeader("Accept", "application/json")

                            .build();
                } else {
                    // todo check it
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return null;

                }

            }
        });



        //error handlong
        clientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);

//            int a = response.code();

            return response;


        }).build();




//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ClientConfig.ServerURL).client(clientBuilder.build()).
                addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

        mService = retrofit.create(Service.class);
    }

    public Service getmService() {
        return mService;
    }

}
