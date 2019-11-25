package com.example.panelist.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.panelist.R;
import com.example.panelist.models.api_error.APIError;
import com.example.panelist.models.api_error.ErrorUtils;

import okhttp3.Response;

public class ErrorToastActivity extends AppCompatActivity {

    public static Response response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_toast);


//        APIError apiError = ErrorUtils.parseError(response);

    }
}
