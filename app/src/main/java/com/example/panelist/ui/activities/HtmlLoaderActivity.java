package com.example.panelist.ui.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.os.ConfigurationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.panelist.R;
import com.example.panelist.utilities.CustomBaseActivity;
import com.example.panelist.utilities.GeneralTools;
import com.wang.avi.AVLoadingIndicatorView;

public class HtmlLoaderActivity extends CustomBaseActivity implements View.OnClickListener {


    //region of view

    WebView webView;
    AVLoadingIndicatorView av_loading;
    LinearLayout linear_exit , web_btnbar;
    //end of region

    //region of property
    int id, url_type;
    BroadcastReceiver connectivityReceiver = null;
    boolean isSurveyDetails, isShopping, isUserStartSurvey = false;
    //end of region

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_loader);

        //initialize view
        defineView();


        String locale_name = ConfigurationCompat.getLocales(getResources().getConfiguration()).get(0).getLanguage();
        if (locale_name.equals("fa")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            web_btnbar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else if (locale_name.equals("en")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            web_btnbar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        //config web view for show url content
        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/BYekan.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";

        //check intent and get url
        String url = null;
        if (getIntent() != null) {

            url = getIntent().getStringExtra("url");
            id = getIntent().getIntExtra("id", 0);
            isSurveyDetails = getIntent().getBooleanExtra("surveyDetails", false);
            url_type = getIntent().getIntExtra("type", 1);
            isShopping = getIntent().getBooleanExtra("isShopping", false);

        }

        //config web view setting for support multi action and java scripts

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setMinimumFontSize(1);
        webView.getSettings().setMinimumLogicalFontSize(1);
        webView.setClickable(true);
        webView.clearCache(true);


        webView.loadUrl(url);

//        if (isSurveyDetails) {
//
////            //check for show token dialog
////            if (url_type != 2)
////                createTokenDialog();
//
//            webView.loadUrl(url);
//
//        } else {
//
//            if (isShopping)
//                webView.loadUrl(url);
//            else
//                webView.loadDataWithBaseURL("", pish + url + pas, "text/html", "UTF-8", "");
//        }

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                av_loading.smoothToHide();
            }
        });

        //check network broadcast reciever
        GeneralTools tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(HtmlLoaderActivity.this, findViewById(R.id.rl_root));
            }
        };

    }


    //define views of activity here
    private void defineView() {

        webView = findViewById(R.id.web_view);
        av_loading = findViewById(R.id.av_loading);
        linear_exit = findViewById(R.id.linear_exit);
        web_btnbar = findViewById(R.id.web_btnbar);
        linear_exit.setOnClickListener(this);
    }






    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.linear_exit) {

            if (isSurveyDetails) {

                if (webView.canGoBack())
                    isUserStartSurvey = true;

                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("isUserStartSurvey", isUserStartSurvey);

                //get q status from current web view url if is not empty so:
                Uri uri = Uri.parse(webView.getUrl());
                String qStatus = uri.getQueryParameter("qstatus");

                if (qStatus == null || qStatus.equals(""))
                    qStatus = "1";

                intent.putExtra("qstatus", qStatus);
                setResult(RESULT_OK, intent);

//                createConfirmExitDialog();
            } else
                finish();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(connectivityReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (isSurveyDetails) {

            if (webView.canGoBack())
                isUserStartSurvey = true;

            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.putExtra("isUserStartSurvey", isUserStartSurvey);

            //get q status from current web view url if is not empty so:
            Uri uri = Uri.parse(webView.getUrl());
            String qStatus = uri.getQueryParameter("qstatus");

            if (qStatus == null || qStatus.equals(""))
                qStatus = "1";

            intent.putExtra("qstatus", qStatus);
            setResult(RESULT_OK, intent);

//            createConfirmExitDialog();
        } else
            finish();
    }


}
