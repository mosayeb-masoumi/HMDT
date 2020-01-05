package com.rahbarbazaar.checkpanel.ui.activities;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.os.ConfigurationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.controllers.adapters.DrawerAdapter;
import com.rahbarbazaar.checkpanel.controllers.interfaces.DrawerItemClicked;
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create.DashboardCreateData;
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create.DrawerItems;
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_update.DashboardUpdateData;
import com.rahbarbazaar.checkpanel.models.issue.ReportIssue;
import com.rahbarbazaar.checkpanel.network.Service;
import com.rahbarbazaar.checkpanel.network.ServiceProvider;
import com.rahbarbazaar.checkpanel.ui.fragments.ShopFragment;
import com.rahbarbazaar.checkpanel.ui.fragments.HomeFragment;
import com.rahbarbazaar.checkpanel.ui.fragments.RegisterFragment;
import com.rahbarbazaar.checkpanel.ui.fragments.TransactionFragment;
import com.rahbarbazaar.checkpanel.utilities.Cache;
import com.rahbarbazaar.checkpanel.utilities.ConvertEnDigitToFa;
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity;
import com.rahbarbazaar.checkpanel.utilities.DialogFactory;
import com.rahbarbazaar.checkpanel.utilities.GeneralTools;
import com.rahbarbazaar.checkpanel.utilities.RxBus;

import java.util.ArrayList;
import java.util.List;

import co.ronash.pushe.Pushe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends CustomBaseActivity implements View.OnClickListener,
        AHBottomNavigation.OnTabSelectedListener, DrawerItemClicked {


    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    AHBottomNavigation bottom_navigation;

    ImageView image_drawer, image_instagram, image_telegram, img_backbtmbar_left, img_backbtmbar_centerleft,
            img_backbtmbar_centerright, img_backbtmbar_right, img_arrow;

    LinearLayout linear_invite_friend, linear_exit, linear_shopping, linear_message_drawer,
            linear_support, linear_report_issue, linear_faq, linear_submenu, linear_profile_drawer, ll_drawer;
    RelativeLayout ll_notify_count;

    TextView txt_exit, text_notify_count, text_follow_us;
    DialogFactory dialogFactory;

    RelativeLayout rl_notification, rl_curvedbottom;
    DrawerLayout drawer_layout_home;

    RecyclerView drawer_rv;
    DrawerAdapter adapter_drawer;
    LinearLayoutManager linearLayoutManager;
    List<DrawerItems> drawerItems;

    boolean doubleBackToExitPressedOnce = false;
    boolean isSupportLayoutClicked = false;

    Disposable disposable = new CompositeDisposable();
    DashboardCreateData dashboardCreateData;

    String locale_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pushe.initialize(this, true);
        String pusheId = Pushe.getPusheId(MainActivity.this);


        locale_name = ConfigurationCompat.getLocales(getResources().getConfiguration()).get(0).getLanguage();

        //check network broadcast reciever
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(MainActivity.this, findViewById(R.id.drawer_layout_home));
            }
        };


        initView();

        disposable = RxBus.DashboardModel.subscribeDashboardModel(result -> {
            if (result instanceof DashboardCreateData) {
                dashboardCreateData = (DashboardCreateData) result;
            }
        });

        //initial Dialog factory
        dialogFactory = new DialogFactory(MainActivity.this);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        ll_drawer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        initializeBottomNavigation();

//        if (locale_name.equals("fa"))
        img_arrow.setImageResource(R.drawable.arrow_left);
//        else
//            img_arrow.setImageResource(R.drawable.arrow_right);


        if (tools.checkPackageInstalled("org.telegram.messenger", this)) {
            image_telegram.setVisibility(View.INVISIBLE);
        }
        if (tools.checkPackageInstalled("com.instagram.android", this)) {
            image_instagram.setVisibility(View.INVISIBLE);
        }


        if (tools.checkPackageInstalled("org.telegram.messenger", this)) { //no telegram
            if (tools.checkPackageInstalled("com.instagram.android", this)) { // no instagram
                text_follow_us.setVisibility(View.INVISIBLE);
            }
        }
        if (tools.checkPackageInstalled("com.instagram.android", this)) { //no instagram
            if (tools.checkPackageInstalled("org.telegram.messenger", this)) { // no telegram
                text_follow_us.setVisibility(View.INVISIBLE);
            }
        }


        setDrawerRecycler();


    }

    private void initView() {

        img_backbtmbar_left = findViewById(R.id.img_backbtmbar_left);
        img_backbtmbar_centerleft = findViewById(R.id.img_backbtmbar_centerleft);
        img_backbtmbar_centerright = findViewById(R.id.img_backbtmbar_centerright);
        img_backbtmbar_right = findViewById(R.id.img_backbtmbar_right);
        image_instagram = findViewById(R.id.image_instagram);
        image_telegram = findViewById(R.id.image_telegram);
        img_arrow = findViewById(R.id.img_arrow);
        image_drawer = findViewById(R.id.image_drawer);

        linear_invite_friend = findViewById(R.id.linear_invite_friend);
        linear_shopping = findViewById(R.id.linear_shopping);
        linear_message_drawer = findViewById(R.id.linear_message_drawer);
        linear_support = findViewById(R.id.linear_support);
        linear_report_issue = findViewById(R.id.linear_report_issue);
        linear_profile_drawer = findViewById(R.id.linear_profile_drawer);

        linear_faq = findViewById(R.id.linear_faq);
        linear_submenu = findViewById(R.id.linear_submenu);
        linear_exit = findViewById(R.id.linear_exit);
        ll_drawer = findViewById(R.id.ll_drawer);
        ll_notify_count = findViewById(R.id.ll_notify_count);
        rl_notification = findViewById(R.id.rl_notification);
        drawer_layout_home = findViewById(R.id.drawer_layout_home);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        drawer_rv = findViewById(R.id.drawer_rv);
        text_notify_count = findViewById(R.id.text_notify_count);
        txt_exit = findViewById(R.id.txt_exit);
        text_follow_us = findViewById(R.id.text_follow_us);

        image_drawer.setOnClickListener(this);
        image_instagram.setOnClickListener(this);
        image_telegram.setOnClickListener(this);
        rl_notification.setOnClickListener(this);
        linear_shopping.setOnClickListener(this);
        linear_exit.setOnClickListener(this);
        linear_faq.setOnClickListener(this);
        linear_support.setOnClickListener(this);
        linear_report_issue.setOnClickListener(this);
        linear_invite_friend.setOnClickListener(this);
        linear_message_drawer.setOnClickListener(this);
        linear_profile_drawer.setOnClickListener(this);
        txt_exit.setOnClickListener(this);
        bottom_navigation.setOnTabSelectedListener(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        drawer_layout_home.closeDrawer(Gravity.END);
        updateDashboard();


    }

    private void updateDashboard() {

        Service service = new ServiceProvider(MainActivity.this).getmService();
        Call<DashboardUpdateData> call = service.dashboardUpdateData();
        call.enqueue(new Callback<DashboardUpdateData>() {
            @Override
            public void onResponse(Call<DashboardUpdateData> call, Response<DashboardUpdateData> response) {
                if (response.code() == 200) {

                    DashboardUpdateData updateData = response.body();
                    setNotifyCount(updateData);

                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DashboardUpdateData> call, Throwable t) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setNotifyCount(DashboardUpdateData updateData) {

        if (updateData.data.getUnread() > 0) {
            ll_notify_count.setVisibility(View.VISIBLE);
            if (updateData.data.getUnread() > 999) {
                text_notify_count.setText("...");
            } else {
                String count = ConvertEnDigitToFa.convert(String.valueOf(updateData.data.getUnread()));
                text_notify_count.setText(count);
            }
        } else if (updateData.data.getUnread() == 0) {
            ll_notify_count.setVisibility(View.GONE);
        }
    }


    private void setDrawerRecycler() {

        drawerItems = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        // to show list of member items
//        List<ActiveList> activeList = new ArrayList<>();

        drawerItems.addAll(dashboardCreateData.data.drawerMenu.data);

//        for (int i = 0; i < activeListData.data.size(); i++) {
//            activeList.add(new ActiveList(activeListData.data.get(i).id, activeListData.data.get(i).date
//                    , activeListData.data.get(i).title));
//        }

        drawer_rv.setLayoutManager(linearLayoutManager);
        adapter_drawer = new DrawerAdapter(drawerItems, MainActivity.this);
        drawer_rv.setAdapter(adapter_drawer);
        adapter_drawer.setListener(MainActivity.this);  // important to set or else the app will crashed
        adapter_drawer.notifyDataSetChanged();

    }


    private void initializeBottomNavigation() {
        // Create items
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.home_tab, R.drawable.home_icon, 0);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.register_tab, R.drawable.purchase_icon, 0);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.myaccount_tab, R.drawable.transaction_icon, 0);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.store_tab, R.drawable.shop_icon, 0);

        // Add items
        bottom_navigation.addItem(item1);
        bottom_navigation.addItem(item2);
        bottom_navigation.addItem(item3);
        bottom_navigation.addItem(item4);


        bottom_navigation.setAccentColor(Color.parseColor("#212b5e"));
        bottom_navigation.setInactiveColor(Color.parseColor("#FFFFFF"));

        bottom_navigation.setDefaultBackgroundResource(R.drawable.bg_toolbar);


        //requred api level min 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bottom_navigation.setElevation(0f);
        }

        // Manage titles
        bottom_navigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // Set current item programmatically
        bottom_navigation.setCurrentItem(3);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.image_drawer:
                drawer_layout_home.openDrawer(Gravity.END);
                break;

            case R.id.linear_invite_friend:
                drawer_layout_home.closeDrawers();
                generateInviteLink();
                break;

            case R.id.txt_exit:
                createConfirmExitDialog();
                drawer_layout_home.openDrawer(Gravity.END);
                break;


            case R.id.linear_support:

                if (!isSupportLayoutClicked) {
                    tools.expand(linear_submenu);
                    img_arrow.setImageResource(R.drawable.arrow_down);
                } else {
                    tools.collapse(linear_submenu);
//                    if (locale_name.equals("fa"))
                    img_arrow.setImageResource(R.drawable.arrow_left);
//                    else
//                        img_arrow.setImageResource(R.drawable.arrow_right);

                }

                isSupportLayoutClicked = !isSupportLayoutClicked;
                break;


            case R.id.linear_faq:
                drawer_layout_home.closeDrawers();
                goToHtmlActivity(dashboardCreateData.data.faqPage);
                break;


            case R.id.linear_report_issue:
                drawer_layout_home.closeDrawers();
                dialogFactory.createReportIssueDialog(new DialogFactory.DialogFactoryInteraction() {
                    @Override
                    public void onAcceptButtonClicked(String... params) {

                        if (params[0].equals("")) {
                            Toast.makeText(MainActivity.this, R.string.empetyReportIssue, Toast.LENGTH_SHORT).show();
                        } else {
                            sendReportIssueRequest(params[0]);
                            drawer_layout_home.closeDrawer(Gravity.END);
                        }

                    }

                    @Override
                    public void onDeniedButtonClicked(boolean cancel_dialog) {
                        drawer_layout_home.closeDrawer(Gravity.END);
                    }
                }, drawer_layout_home);
                break;

            case R.id.linear_message_drawer:
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
                MainActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                drawer_layout_home.closeDrawer(Gravity.END);
                break;

            case R.id.linear_profile_drawer:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                MainActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                drawer_layout_home.closeDrawer(Gravity.END);
                break;


            case R.id.rl_notification:
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
                MainActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                break;


            case R.id.image_instagram:

                drawer_layout_home.closeDrawers();
                Uri uriInstagram = Uri.parse("http://instagram.com/_u/poller.ir");
                Intent intentInstagram = new Intent(Intent.ACTION_VIEW, uriInstagram);
                intentInstagram.setPackage("com.instagram.android");

                try {
                    startActivity(intentInstagram);

                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }
                break;

            case R.id.image_telegram:
                drawer_layout_home.closeDrawers();
                Uri uriTelegram = Uri.parse("https://t.me/Polleriran");
                Intent intentTelegram = new Intent(Intent.ACTION_VIEW, uriTelegram);
                intentTelegram.setPackage("org.telegram.messenger");

                try {
                    startActivity(intentTelegram);

                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }

                break;
        }
    }

    private void generateInviteLink() {

        String user_name = Cache.getString(MainActivity.this,"user_name");
        String share_url = Cache.getString(MainActivity.this,"share_url");

        ShareCompat.IntentBuilder
                .from(MainActivity.this)
                .setText(new StringBuilder().append(getString(R.string.text_invite_from)).append(" ").append(user_name).append(" ").
                        append(getString(R.string.text_invite_friend))
                        .append("\n").append(share_url))
                .setType("text/plain")
                .setChooserTitle(R.string.share_poller)
                .startChooser();
    }

    private void sendReportIssueRequest(String str_issue) {

        Service service = new ServiceProvider(MainActivity.this).getmService();
        Call<ReportIssue> call = service.reportIssue(str_issue);
        call.enqueue(new Callback<ReportIssue>() {
            @Override
            public void onResponse(Call<ReportIssue> call, Response<ReportIssue> response) {
                if (response.code() == 200) {

//                    Boolean result = response.body().data;
                    Toast.makeText(MainActivity.this, "" + getResources().getString(R.string.text_request_submitted), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportIssue> call, Throwable t) {

                Toast.makeText(MainActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createConfirmExitDialog() {

        Context context = MainActivity.this;
        dialogFactory.createConfirmExitDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

                drawer_layout_home.closeDrawers();
                Cache.setString(MainActivity.this, "access_token", "");
                Cache.setString(MainActivity.this, "refresh_token", "");
                Cache.setString(MainActivity.this, "expireAt", "");
                startActivity(new Intent(context, SplashActivity.class));
                MainActivity.this.finish();
            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {
                drawer_layout_home.closeDrawers();
            }
        }, drawer_layout_home);
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {

        if (position == 3) {
            img_backbtmbar_right.setVisibility(View.VISIBLE);
            img_backbtmbar_centerleft.setVisibility(View.GONE);
            img_backbtmbar_centerright.setVisibility(View.GONE);
            img_backbtmbar_left.setVisibility(View.GONE);
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame_layout, homeFragment, "tag").commit();

        } else if (position == 2) {
            img_backbtmbar_right.setVisibility(View.GONE);
            img_backbtmbar_centerleft.setVisibility(View.GONE);
            img_backbtmbar_centerright.setVisibility(View.VISIBLE);
            img_backbtmbar_left.setVisibility(View.GONE);
            RegisterFragment registerFragment = new RegisterFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame_layout, registerFragment, "tag").commit();

        } else if (position == 1) {
            img_backbtmbar_right.setVisibility(View.GONE);
            img_backbtmbar_centerleft.setVisibility(View.VISIBLE);
            img_backbtmbar_centerright.setVisibility(View.GONE);
            img_backbtmbar_left.setVisibility(View.GONE);
            TransactionFragment transactionFragment = new TransactionFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame_layout, transactionFragment, "tag").commit();

        } else if (position == 0) {
            img_backbtmbar_right.setVisibility(View.GONE);
            img_backbtmbar_centerleft.setVisibility(View.GONE);
            img_backbtmbar_centerright.setVisibility(View.GONE);
            img_backbtmbar_left.setVisibility(View.VISIBLE);
            ShopFragment shopFragment = new ShopFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame_layout, shopFragment, "tag").commit();

        }
        return true;
    }


    @Override
    public void onDrawerItemClicked(String url) {

        goToHtmlActivity(url);

    }

    private void goToHtmlActivity(String url) {

        Intent intent = new Intent(MainActivity.this, HtmlLoaderActivity.class);
        intent.putExtra("url", url);
//        intent.putExtra("surveyDetails", false);
//        intent.putExtra("isShopping", shouldBeLoadUrl);
        startActivity(intent);
        MainActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(connectivityReceiver);
        super.onDestroy();
        disposable.dispose();
    }


    @Override
    public void onBackPressed() {


        if (drawer_layout_home.isDrawerOpen(Gravity.END)) {
            drawer_layout_home.closeDrawers();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();

                exitApp();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.text_double_click_exit, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }

    }

    private void exitApp() {
        finish();
        startActivity(new Intent(Intent.ACTION_MAIN).
                addCategory(Intent.CATEGORY_HOME).
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        }
        Process.killProcess(Process.myPid());
        super.finish();
    }


}
