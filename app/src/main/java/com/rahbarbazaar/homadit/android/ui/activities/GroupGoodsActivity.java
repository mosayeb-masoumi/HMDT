package com.rahbarbazaar.homadit.android.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.GroupAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.GroupGoodsItemInteraction;
import com.rahbarbazaar.homadit.android.models.group_goods.GroupGoodsModel;
import com.rahbarbazaar.homadit.android.models.search_goods.GroupsData;
import com.rahbarbazaar.homadit.android.utilities.Cache;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.rahbarbazaar.homadit.android.utilities.DialogFactory;
import com.rahbarbazaar.homadit.android.utilities.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class GroupGoodsActivity extends CustomBaseActivity implements View.OnClickListener, GroupGoodsItemInteraction {

    RecyclerView recyclerView ;
    Button btn_new_scan, btn_unknown_goods ,btn_finish_purchase;

    GroupsData groupsData;
    Disposable disposable = new CompositeDisposable();

    List<GroupGoodsModel> searchList;

    RelativeLayout rl_home_group_goods ,root;
    GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_goods);


        initView();

        groupsData = new GroupsData();
        disposable = RxBus.GroupGoodsList.subscribeGroupGoodsList(result -> {
            if (result instanceof GroupsData) {
                groupsData = (GroupsData) result;
            }
        });


        searchList = new ArrayList<>();
//
        for (int i = 0; i < groupsData.getData().size(); i++) {
            searchList.add(new GroupGoodsModel(groupsData.getData().get(i).getTitle(),
                    groupsData.getData().get(i).getId() ,groupsData.getData().get(i).getIcon(), false));
        }




        String state = getIntent().getExtras().getString("new_register");
        if(state!=null && state.equals("new_register")){
            btn_finish_purchase.setVisibility(View.GONE);
        }else{
            btn_finish_purchase.setVisibility(View.VISIBLE);
        }

        setRecyclerView();
    }


    private void initView() {

        recyclerView = findViewById(R.id.rv_group_goods);
        rl_home_group_goods = findViewById(R.id.rl_home_group_goods);
        btn_new_scan = findViewById(R.id.btn_new_scan);
        btn_unknown_goods = findViewById(R.id.btn_unknown_goods);
        btn_finish_purchase = findViewById(R.id.btn_finish_purchase);
        root = findViewById(R.id.root_group_goods);


        btn_new_scan.setOnClickListener(this);
        btn_unknown_goods.setOnClickListener(this);
        btn_finish_purchase.setOnClickListener(this);
        rl_home_group_goods.setOnClickListener(this);

    }


    private void setRecyclerView() {

        //set recyclerview
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new GroupAdapter(searchList, this);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_new_scan:
                startActivity(new Intent(GroupGoodsActivity.this, QRcodeActivity.class));
                finish();
                break;

            case R.id.btn_unknown_goods:
//                checkAndStartRegisterClass();
                break;

            case R.id.rl_home_group_goods:
                startActivity(new Intent(GroupGoodsActivity.this,MainActivity.class));
                finish();
                break;


            case R.id.btn_finish_purchase:
                showEditPopup();
                break;

        }
    }

    private void showEditPopup() {


        DialogFactory dialogFactory = new DialogFactory(this);
        dialogFactory.createGroupGoodsFinishDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... strings) {

                startActivity(new Intent(GroupGoodsActivity.this,MainActivity.class));
                finish();

            }

            @Override
            public void onDeniedButtonClicked(boolean cancel_dialog) {
                startActivity(new Intent(GroupGoodsActivity.this, NewRegisterListActivity.class));
                finish();
            }
        },root);


    }






//    private void checkAndStartRegisterClass() {
//
//        int selectedCheckbox= 0;
//        for (int i = 0; i < searchList.size(); i++) {
//
//            if(searchList.get(i).isChecked()){
//                selectedCheckbox +=1;
//            }
//        }
//
//        if(selectedCheckbox == 0){
//            Toast.makeText(this, "یک گروه کالا را انتخاب کنید!", Toast.LENGTH_SHORT).show();
//        }else if(selectedCheckbox >1){
//            Toast.makeText(this, "فقط مجاز به انتخاب یک گروه کالا می باشید!", Toast.LENGTH_SHORT).show();
//        }else if(selectedCheckbox == 1) {
//
//
//            for (int i = 0; i < searchList.size(); i++) {
//
//                if(searchList.get(i).isChecked()){
//                    String groupId = searchList.get(i).getId();
//                    String groupTitle = searchList.get(i).getTitle();
//                    Cache.setString(this,"selectedGroupId",groupId);
//                    Cache.setString(this,"selectedGroupTitle",groupTitle);
//                }
//            }
//
//            Intent intent = new Intent(GroupGoodsActivity.this, PurchasedItemActivity.class);
//            intent.putExtra("unreadable_barcode", "unreadable_barcode");
//            startActivity(intent);
//            finish();
//        }
//
//    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose();
    }



    @Override
    public void groupGoodsListItemOnClick(GroupGoodsModel model) {
        Log.i("TAG", "groupGoodsListItemOnClick: ");

        Cache.setString(this,"selectedGroupId",model.getId());
        Cache.setString(this,"selectedGroupTitle",model.getTitle());

        startActivity(new Intent(GroupGoodsActivity.this, QRcodeActivity.class)) ;
        finish();

//        final Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//
//
//            }
//        }, 200);







//        Intent intent = new Intent(GroupGoodsActivity.this, QRcodeActivity.class);
//        intent.putExtra("group_title",model.getTitle());
//        startActivity(intent);


    }
}