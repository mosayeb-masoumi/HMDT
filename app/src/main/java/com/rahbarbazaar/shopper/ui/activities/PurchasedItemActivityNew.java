package com.rahbarbazaar.shopper.ui.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.interfaces.SearchItemInteraction;
import com.rahbarbazaar.shopper.models.barcodlist.Barcode;
import com.rahbarbazaar.shopper.models.search_goods.GroupsData;
import com.rahbarbazaar.shopper.models.searchable.SearchModel;
import com.rahbarbazaar.shopper.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.GeneralTools;
import com.rahbarbazaar.shopper.utilities.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class PurchasedItemActivityNew extends CustomBaseActivity implements View.OnClickListener , SearchItemInteraction {


    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    RelativeLayout rl_home, rl_readable_barcode,rl_description_purchased,rl_spn_group,rl_spn_brand,rl_spn_type,rl_spn_amount,rl_root;
    LinearLayout rl_return,ll_texts,ll_spinners,ll_barcode;
    Integer position;
    String state;

    EditText edt_barcode;
    MemberPrize initMemberPrizeLists;
    Barcode barcode;

    String spn_name ,str_spn_group_id, str_spn_brand_id,str_spn_type_id,str_spn_amount_id;
    String str_spn_group_title, str_spn_brand_title,str_spn_type_title,str_spn_amount_title;




    GroupsData spinnerList;

    TextView txt_description_purchased ,txt_group_purchase,txt_type_purchase,txt_brand_purchase,
            txt_amount_purchase,txt_barcode,txt_spn_group_title,txt_spn_brand_title,txt_spn_type_title,txt_spn_amount_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_item_new);



        // to check internet connection
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(PurchasedItemActivityNew.this, findViewById(R.id.root_purchased_item));
            }
        };

        //get data from register fragment
        disposable = RxBus.BarcodeList.subscribeBarcodeList(result -> {
            if (result instanceof Barcode) {
                barcode = (Barcode) result;
            }
        });

        disposable = RxBus.MemberPrizeLists.subscribeMemberPrizeLists(result -> {
            if (result instanceof MemberPrize) {
                initMemberPrizeLists = (MemberPrize) result;
            }
        });

        initView();

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 5000);
        state = intent.getStringExtra("unreadable_barcode");

        spinnerList = intent.getParcelableExtra("groupsData");






        if (state != null && state.equals("unreadable_barcode")) {
            edt_barcode.setVisibility(View.VISIBLE);
            rl_readable_barcode.setVisibility(View.GONE);
        } else {
            edt_barcode.setVisibility(View.GONE);
            rl_readable_barcode.setVisibility(View.VISIBLE);
        }

        if(position == 5000){ // list just has one item

            if(spinnerList==null && state.equals(null)){
                if(barcode.getData().get(0).getMygroup().equals("product")){
                    ll_texts.setVisibility(View.VISIBLE);
                    ll_spinners.setVisibility(View.GONE);

                    txt_group_purchase.setText(barcode.getData().get(0).getBarcodeDetail().get(0).getValue());
                    txt_type_purchase.setText(barcode.getData().get(0).getBarcodeDetail().get(1).getValue());
                    txt_brand_purchase.setText(barcode.getData().get(0).getBarcodeDetail().get(2).getValue());
                    txt_amount_purchase.setText(barcode.getData().get(0).getBarcodeDetail().get(3).getValue());

                    txt_barcode.setText(barcode.getData().get(0).getBarcode());

                }else if(barcode.getData().get(0).getMygroup().equals("wait")){
                    ll_texts.setVisibility(View.GONE);
                    ll_spinners.setVisibility(View.GONE);
                    rl_description_purchased.setVisibility(View.VISIBLE);
                    txt_description_purchased.setText(barcode.getData().get(0).getDecription());
                    txt_barcode.setText(barcode.getData().get(0).getBarcode());
                }
                int a = 5;
            }

        }else if(position != 5000 && state.equals(null)){  //selected from list
            if(barcode.getData().get(position).getMygroup().equals("wait")){
                ll_texts.setVisibility(View.GONE);
                ll_spinners.setVisibility(View.GONE);
                rl_description_purchased.setVisibility(View.VISIBLE);
                txt_description_purchased.setText(barcode.getData().get(position).getDecription());
                txt_barcode.setText(barcode.getData().get(position).getBarcode());
            }else if(barcode.getData().get(position).getMygroup().equals("product")){
                ll_texts.setVisibility(View.VISIBLE);
                ll_spinners.setVisibility(View.GONE);

                txt_group_purchase.setText(barcode.getData().get(position).getBarcodeDetail().get(0).getValue());
                txt_type_purchase.setText(barcode.getData().get(position).getBarcodeDetail().get(1).getValue());
                txt_brand_purchase.setText(barcode.getData().get(position).getBarcodeDetail().get(2).getValue());
                txt_amount_purchase.setText(barcode.getData().get(position).getBarcodeDetail().get(3).getValue());

                txt_barcode.setText(barcode.getData().get(position).getBarcode());
            }
        }


        if(spinnerList!=null){

            ll_texts.setVisibility(View.GONE);
            ll_spinners.setVisibility(View.VISIBLE);
            ll_barcode.setVisibility(View.GONE);
            rl_description_purchased.setVisibility(View.GONE);

            setGroupsSpn(spinnerList);
        }


    }

    private void setGroupsSpn(GroupsData spinnerList) {

//        ArrayList<String> groupsTitleList = new ArrayList<>();
//        for (int i = 0; i <spinnerList.getData().size() ; i++) {
//            groupsTitleList.add(spinnerList.getData().get(i).getTitle());
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, groupsTitleList);
//        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
//        spn_group.setAdapter(adapter);
    }




    private void initView() {

        rl_home = findViewById(R.id.rl_home_Purchased_items);
        rl_return = findViewById(R.id.linear_return_qrcode);
        edt_barcode = findViewById(R.id.edt_barcode);
        rl_readable_barcode = findViewById(R.id.rl_readable_barcode);
        ll_texts = findViewById(R.id.ll_texts);
        ll_spinners = findViewById(R.id.ll_spinners);
        rl_description_purchased = findViewById(R.id.rl_description_purchased);
        txt_description_purchased=findViewById(R.id.txt_description_purchased);
        txt_barcode=findViewById(R.id.txt_barcode);
        ll_barcode=findViewById(R.id.ll_barcode);
        rl_root = findViewById(R.id.root_purchased_item);

        txt_spn_group_title=findViewById(R.id.txt_group_spn_title);
        txt_spn_brand_title=findViewById(R.id.txt_brand_spn_title);
        txt_spn_type_title=findViewById(R.id.txt_type_spn_title);
        txt_spn_amount_title=findViewById(R.id.txt_amount_spn_title);


        txt_group_purchase=findViewById(R.id.txt_group_purchase);
        txt_type_purchase=findViewById(R.id.txt_type_purchase);
        txt_brand_purchase=findViewById(R.id.txt_brand_purchase);
        txt_amount_purchase=findViewById(R.id.txt_amount_purchase);

        rl_spn_group=findViewById(R.id.rl_spn_group);
        rl_spn_brand=findViewById(R.id.rl_spn_brand);
        rl_spn_type=findViewById(R.id.rl_spn_type);
        rl_spn_amount=findViewById(R.id.rl_spn_amount);

        rl_home.setOnClickListener(this);
        rl_return.setOnClickListener(this);
        rl_spn_group.setOnClickListener(this);
        rl_spn_brand.setOnClickListener(this);
        rl_spn_type.setOnClickListener(this);
        rl_spn_amount.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_home_Purchased_items:
                startActivity(new Intent(PurchasedItemActivityNew.this, MainActivity.class));
                finish();
                break;

            case R.id.linear_return_qrcode:
                startActivity(new Intent(PurchasedItemActivityNew.this, QRcodeActivity1.class));
                finish();
                break;

            case R.id.rl_spn_group:
                spn_name = "spn_group";
                showSpnGroupListDialog(spn_name);
                break;
            case R.id.rl_spn_brand:
                 spn_name = "spn_brand";
                showSpnGroupListDialog(spn_name);
                break;
            case R.id.rl_spn_type:
                spn_name = "spn_type";
                showSpnGroupListDialog(spn_name);
                break;
            case R.id.rl_spn_amount:
                spn_name = "spn_amount";
                showSpnGroupListDialog(spn_name);
                break;
        }
    }

    private void showSpnGroupListDialog(String spn_name) {

        List<SearchModel> searchList = new ArrayList<>();
        for (int i = 0; i <spinnerList.getData().size() ; i++) {
            searchList.add(new SearchModel(spinnerList.getData().get(i).getTitle(),spinnerList.getData().get(i).getId()));
        }


       DialogFactory dialogFactory = new DialogFactory(PurchasedItemActivityNew.this);

       dialogFactory.createSpnListDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {


            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_root,searchList,this ,spn_name);

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
        disposable.dispose(); //very important  to avoid memory leak

    }


    @Override
    public void searchListItemOnClick(SearchModel model, AlertDialog dialog, String spn_name) {

        if(spn_name.equals("spn_group")){

            str_spn_group_title = model.getTitle();
            str_spn_group_id = model.getId();
            txt_spn_group_title.setText(model.getTitle());
            dialog.dismiss();

        }else if(spn_name.equals("spn_brand")){

            str_spn_brand_title = model.getTitle();
            str_spn_brand_id = model.getId();
            txt_spn_brand_title.setText(model.getTitle());

        }else if(spn_name.equals("spn_type")){
            str_spn_type_title = model.getTitle();
            str_spn_type_id = model.getId();
            txt_spn_type_title.setText(model.getTitle());

        }else if(spn_name.equals("spn_amount")){
            str_spn_amount_title = model.getTitle();
            str_spn_amount_id = model.getId();
            txt_spn_amount_title.setText(model.getTitle());
        }

    }
}
