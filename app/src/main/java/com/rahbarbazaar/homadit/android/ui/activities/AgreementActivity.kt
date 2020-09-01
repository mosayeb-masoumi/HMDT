package com.rahbarbazaar.homadit.android.ui.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.Html
import android.view.View
import android.widget.TextView
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.models.dashboard.dashboard_create.DashboardCreateData
import com.rahbarbazaar.homadit.android.utilities.Cache
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity
import com.rahbarbazaar.homadit.android.utilities.GeneralTools
import com.rahbarbazaar.homadit.android.utilities.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_agreement.*

class AgreementActivity : CustomBaseActivity() {

    lateinit var connectivityReceiver: BroadcastReceiver

    var disposable: Disposable = CompositeDisposable()
    lateinit var dashboardCreateData: DashboardCreateData

    @RequiresApi(Build.VERSION_CODES.N)
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
//        webview_agreement.settings.javaScriptEnabled = true
//        webview_agreement.settings.domStorageEnabled = true
//        webview_agreement.settings.databaseEnabled = true
//        webview_agreement.settings.allowFileAccess = true
//        webview_agreement.settings.allowContentAccess = true
//        webview_agreement.webChromeClient = WebChromeClient()
//        webview_agreement.settings.allowFileAccessFromFileURLs = true
//        webview_agreement.settings.allowUniversalAccessFromFileURLs = true
//        webview_agreement.settings.minimumFontSize = 1
//        webview_agreement.settings.minimumLogicalFontSize = 1
//        webview_agreement.isClickable = true
//        webview_agreement.clearCache(true)
//
//        webview_agreement.loadUrl(dashboardCreateData.data.agreementPage)
//        webview_agreement.setBackgroundColor(Color.TRANSPARENT)
//
//        webview_agreement.webViewClient = object : WebViewClient() {
//
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                view.loadUrl(url)
//                return false
//            }
//
//            override fun onPageFinished(view: WebView, url: String) {
//                super.onPageFinished(view, url)
//                av_loading.smoothToHide()
//                llcheckbox.visibility = View.VISIBLE
//
//            }
//        }


        // to detect the end of scroll
        scroll_view.viewTreeObserver.addOnScrollChangedListener {
            if (scroll_view != null) {
                if (scroll_view.getChildAt(0).bottom <= scroll_view.height + scroll_view.scrollY) { //scroll view is at bottom

                    llcheckbox.visibility = View.VISIBLE

                } else { //scroll view is not at bottom
                }
            }
        }





        rl_login_dialog.setOnClickListener {
            if (checkbox_agreement.isChecked) {
               startActivity(Intent(this@AgreementActivity,MainActivity::class.java))
                this@AgreementActivity.finish()
                Cache.setString(this@AgreementActivity,"agreement","done")
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
                Cache.setString(this@AgreementActivity,"agreement","undone")
            }
        }

        if (checkbox_agreement.isChecked) {
            img_rules_enter_icon.visibility = View.VISIBLE
            img_page_icon_rules.visibility = View.GONE
        } else {
            img_page_icon_rules.visibility = View.VISIBLE
            img_rules_enter_icon.visibility = View.GONE
        }


        //https://test.rahbarbazar.com/cp/content/getpage?page=agreement&language=fa

      

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
