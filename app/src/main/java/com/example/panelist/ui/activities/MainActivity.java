package com.example.panelist.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
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
import com.example.panelist.R;
import com.example.panelist.ui.fragments.ShopFragment;
import com.example.panelist.ui.fragments.HomeFragment;
import com.example.panelist.ui.fragments.RegisterFragment;
import com.example.panelist.ui.fragments.AccountFragment;
import com.example.panelist.utilities.Cache;
import com.example.panelist.utilities.CustomBaseActivity;
import com.example.panelist.utilities.DialogFactory;
import com.example.panelist.utilities.GeneralTools;

public class MainActivity extends CustomBaseActivity implements View.OnClickListener, AHBottomNavigation.OnTabSelectedListener {


    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    AHBottomNavigation bottom_navigation;

    ImageView image_drawer, image_instagram, image_telegram, img_backbtmbar_left, img_backbtmbar_centerleft,
            img_backbtmbar_centerright, img_backbtmbar_right, img_arrow;

    LinearLayout linear_invite_friend, linear_exit, linear_shopping, linear_notify_drawer, linear_change_lang,
            linear_support, linear_report_issue, linear_faq, linear_submenu, linear_graph, ll_drawer;

     TextView txt_exit;
    DialogFactory dialogFactory;


    RelativeLayout rl_notification, rl_curvedbottom;
    DrawerLayout drawer_layout_home;
    RecyclerView drawer_rv;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Pushe.initialize(this,true);
//        String pusheId = Pushe.getPusheId(MainActivity.this);





        //check network broadcast reciever
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(MainActivity.this, findViewById(R.id.drawer_layout_home));
            }
        };

        
        initView();

        //initial Dialog factory
        dialogFactory = new DialogFactory(MainActivity.this);

            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            ll_drawer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        initializeBottomNavigation();


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
        linear_notify_drawer = findViewById(R.id.linear_notify_drawer);
        linear_support = findViewById(R.id.linear_support);
        linear_report_issue = findViewById(R.id.linear_report_issue);
        linear_change_lang = findViewById(R.id.linear_change_lang);
        linear_graph = findViewById(R.id.linear_graph);
        linear_faq = findViewById(R.id.linear_faq);
        linear_submenu = findViewById(R.id.linear_submenu);
        linear_exit = findViewById(R.id.linear_exit);
        ll_drawer = findViewById(R.id.ll_drawer);
        rl_notification=findViewById(R.id.rl_notification);
        drawer_layout_home=findViewById(R.id.drawer_layout_home);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        drawer_rv = findViewById(R.id.drawer_rv);

        txt_exit = findViewById(R.id.txt_exit);

        image_drawer.setOnClickListener(this);
        image_instagram.setOnClickListener(this);
        image_telegram.setOnClickListener(this);
        rl_notification.setOnClickListener(this);
        linear_shopping.setOnClickListener(this);
        linear_exit.setOnClickListener(this);
        linear_faq.setOnClickListener(this);
        linear_graph.setOnClickListener(this);
        linear_support.setOnClickListener(this);
        linear_change_lang.setOnClickListener(this);
        linear_report_issue.setOnClickListener(this);
        linear_invite_friend.setOnClickListener(this);
        linear_notify_drawer.setOnClickListener(this);
        txt_exit.setOnClickListener(this);
        bottom_navigation.setOnTabSelectedListener(this);


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

        switch (view.getId()){

            case R.id.image_drawer:
                drawer_layout_home.openDrawer(Gravity.END);
                break;

            case R.id.linear_invite_friend:

                drawer_layout_home.closeDrawers();

//                if (prefrence != null && prefrence.getType().equals("1")) //user guest state
//                    dialogFactory.createNoRegisterDialog1(drawer_layout_home, MainActivity.this);
//                else
//                    generateInviteLink();
                break;

            case R.id.txt_exit:
                createConfirmExitDialog();
                drawer_layout_home.openDrawer(Gravity.END);
                break;

            case R.id.linear_graph:
                drawer_layout_home.closeDrawer(Gravity.END);
                startActivity(new Intent(MainActivity.this,GraphActivity.class));
                MainActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
    }

    private void createConfirmExitDialog() {

        Context context = MainActivity.this;
        dialogFactory.createConfirmExitDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

                drawer_layout_home.closeDrawers();

                Cache.setString("access_token","");
                Cache.setString("refresh_token","");
                Cache.setString("expireAt","");

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



            AccountFragment accountFragment = new AccountFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame_layout, accountFragment, "tag").commit();


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
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        drawer_layout_home.closeDrawer(Gravity.END);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(connectivityReceiver);
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {


        if (drawer_layout_home.isDrawerOpen(Gravity.END)) {
            drawer_layout_home.closeDrawers();
        }else {
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
