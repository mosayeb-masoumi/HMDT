package com.rahbarbazaar.shopper.utilities;

import android.content.Context;
import android.support.multidex.MultiDex;

public class CustomBaseActivity extends PersianAppcompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));

    }
}
