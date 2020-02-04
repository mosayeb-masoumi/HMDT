package com.rahbarbazaar.shopper.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.models.barcodlist.Barcode;
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity;

public class PurchasedItemActivityNew extends CustomBaseActivity implements View.OnClickListener {

    RelativeLayout rl_home;
    LinearLayout rl_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_item_new);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);


        initView();

    }

    private void initView() {

        rl_home = findViewById(R.id.rl_home_Purchased_items);
        rl_return = findViewById(R.id.linear_return_qrcode);

        rl_home.setOnClickListener(this);
        rl_return.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.rl_home_Purchased_items:
                startActivity(new Intent(PurchasedItemActivityNew.this,MainActivity.class));
                finish();
                break;

            case R.id.linear_return_qrcode:
                startActivity(new Intent(PurchasedItemActivityNew.this,QRcodeActivity1.class));
                finish();
                break;
        }
    }
}
