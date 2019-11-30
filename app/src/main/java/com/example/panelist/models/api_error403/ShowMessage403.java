package com.example.panelist.models.api_error403;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.example.panelist.models.api_error.ErrorUtils;
import com.example.panelist.ui.activities.LoginActivity;
import com.example.panelist.utilities.App;

import retrofit2.Response;


public class ShowMessage403 {

//    private Context context;
//
//    public ShowMessage403(Context context) {
//        this.context = context;
//    }

    public static  void message(Response response, Context context) {
        APIError403 apiError403 = ErrorUtils.parseError403(response);
//        context.startActivity(new Intent(context, LoginActivity.class));
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Toast.makeText(context, ""+apiError403.message, Toast.LENGTH_SHORT).show();
    }
}
