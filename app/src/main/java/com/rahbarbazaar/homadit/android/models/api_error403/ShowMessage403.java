package com.rahbarbazaar.homadit.android.models.api_error403;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.rahbarbazaar.homadit.android.models.api_error.ErrorUtils;
import com.rahbarbazaar.homadit.android.ui.activities.LoginActivity;
import retrofit2.Response;

public class ShowMessage403 {
    public static  void message(Response response, Context context) {
        APIError403 apiError403 = ErrorUtils.parseError403(response);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Toast.makeText(context, ""+apiError403.message, Toast.LENGTH_SHORT).show();
    }
}
