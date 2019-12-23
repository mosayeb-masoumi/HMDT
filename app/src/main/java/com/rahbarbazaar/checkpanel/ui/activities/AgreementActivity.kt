package com.rahbarbazaar.checkpanel.ui.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create.DashboardCreateData
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbazaar.checkpanel.utilities.GeneralTools
import com.rahbarbazaar.checkpanel.utilities.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_agreement.*

class AgreementActivity : CustomBaseActivity() {

    lateinit var connectivityReceiver: BroadcastReceiver

    var disposable: Disposable = CompositeDisposable()
    lateinit var dashboardCreateData: DashboardCreateData

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)


        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                tools.doCheckNetwork(this@AgreementActivity, findViewById(R.id.rl_root))
            }
        }



        // get data from rxbus
        disposable = CompositeDisposable()
        disposable = RxBus.DashboardModel.subscribeDashboardModel { result ->
            if (result is DashboardCreateData) {
                dashboardCreateData = result
            }
        }



        //config web view setting for support multi action and java scripts
        webview_agreement.settings.javaScriptEnabled = true
        webview_agreement.settings.domStorageEnabled = true
        webview_agreement.settings.databaseEnabled = true
        webview_agreement.settings.allowFileAccess = true
        webview_agreement.settings.allowContentAccess = true
        webview_agreement.webChromeClient = WebChromeClient()
        webview_agreement.settings.allowFileAccessFromFileURLs = true
        webview_agreement.settings.allowUniversalAccessFromFileURLs = true
        webview_agreement.settings.minimumFontSize = 1
        webview_agreement.settings.minimumLogicalFontSize = 1
        webview_agreement.isClickable = true
        webview_agreement.clearCache(true)


        webview_agreement.loadUrl(dashboardCreateData.data.agreementPage)

//        var locale_name = ConfigurationCompat.getLocales(resources.configuration).get(0).language
//
//        var a =locale_name
//        if(locale_name.equals("fa")){
//            webview_agreement.loadUrl(ClientConfig.Html_URL+"content/agreement/fa")
//        }else{
//            webview_agreement.loadUrl(ClientConfig.Html_URL+"content/agreement/en")
//        }




        webview_agreement.setBackgroundColor(Color.TRANSPARENT)



        webview_agreement.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                av_loading.smoothToHide()
                llcheckbox.visibility = View.VISIBLE

            }
        }


//        btn_send.setOnClickListener(view -> {
        rl_login_dialog.setOnClickListener { view ->


            if (checkbox_agreement.isChecked) {

               startActivity(Intent(this@AgreementActivity,MainActivity::class.java))
                this@AgreementActivity.finish()
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            }
        }


        checkbox_agreement.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                img_rules_enter_icon.visibility = View.VISIBLE
                img_page_icon_rules.visibility = View.GONE
            } else {
                img_rules_enter_icon.visibility = View.GONE
                img_page_icon_rules.visibility = View.VISIBLE
            }

        }

        if (checkbox_agreement.isChecked) {
            img_rules_enter_icon.visibility = View.VISIBLE
            img_page_icon_rules.visibility = View.GONE
        } else {
            img_page_icon_rules.visibility = View.VISIBLE
            img_rules_enter_icon.visibility = View.GONE
        }


    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
        disposable.dispose()
    }
}
