package com.rahbarbazaar.shopper.ui.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.codesgood.views.JustifiedTextView;
import com.codesgood.views.JustifiedTextView;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.adapters.EditPrizeAdapter;
import com.rahbarbazaar.shopper.controllers.adapters.PrizeAdapter;
import com.rahbarbazaar.shopper.controllers.adapters.RegisterMemberDialogAdapter;
import com.rahbarbazaar.shopper.controllers.adapters.RegisterMemberEditAdapter;
import com.rahbarbazaar.shopper.controllers.interfaces.PrizeItemInteraction;
import com.rahbarbazaar.shopper.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.shopper.models.api_error.APIError422;
import com.rahbarbazaar.shopper.models.api_error.ErrorUtils;
import com.rahbarbazaar.shopper.models.edit_products.BoughtMember;
import com.rahbarbazaar.shopper.models.edit_products.BoughtPrize;
import com.rahbarbazaar.shopper.models.edit_products.EditProductDetailUpdateSendData;
import com.rahbarbazaar.shopper.models.edit_products.EditProducts;
import com.rahbarbazaar.shopper.models.edit_products.TotalEditProductData;
import com.rahbarbazaar.shopper.models.edit_products.UpdateEditProductDetailResult;
import com.rahbarbazaar.shopper.models.register.Member;
import com.rahbarbazaar.shopper.models.register.Prize;
import com.rahbarbazaar.shopper.models.register.RegisterMemberEditModel;
import com.rahbarbazaar.shopper.models.register.SendPrize;
import com.rahbarbazaar.shopper.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.EditTextWatcher;
import com.rahbarbazaar.shopper.utilities.GeneralTools;
import com.rahbarbazaar.shopper.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductsDetailActivity extends CustomBaseActivity
        implements View.OnClickListener, RegisterItemInteraction, PrizeItemInteraction {


    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    DialogFactory dialogFactory;
    TotalEditProductData totalEditProductData;
    EditProducts editProducts;
    RecyclerView recyclerEditedMember, recycler_prize;
    ArrayList<RegisterMemberEditModel> editMembers;
    RegisterMemberEditAdapter adapter_edited;
    RegisterMemberDialogAdapter adapter_member;
    List<SendPrize> sendPrizes;
    EditPrizeAdapter editPrizeAdapter;
    PrizeAdapter adapter_prize;
    RelativeLayout rl_spn_shop, rl_addmember, rl_prize, rl_root;
    Button btn_register;
    EditText edt_discount, edt_total_amount, edt_paid, edt_amount;
    TextView txt_unit, txt_amount_title_chkbox,txt_total_amount_title_edit,txt_paid_edit_product,txt_discount_edit_product_detail;
    JustifiedTextView txt_desc;
    LinearLayout ll_return;
    RelativeLayout rl_home,rl_member_info,rl_prize_info;
    CheckBox checkBox_precentage, checkBox_amount;
    AVLoadingIndicatorView avi;
    String checkbox_text = "" , info_type,description,bought_id;

    // for handling422
    private StringBuilder builderPaid, builderCost, builderDiscountAmount,
            builderShopId, builderMember, builderDate, buliderPrize;
    MemberPrize initMemberPrizeLists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products_detail);

        // to check internet connection
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(EditProductsDetailActivity.this, findViewById(R.id.layout_edit_product_detail));
            }
        };

        //get data from register fragment
        disposable = RxBus.TotalEditProductData.subscribeTotalEditProductData(result -> {
            if (result instanceof TotalEditProductData) {
                totalEditProductData = (TotalEditProductData) result;
            }
        });


        disposable = RxBus.MemberPrizeLists.subscribeMemberPrizeLists(result -> {
            if (result instanceof MemberPrize) {
                initMemberPrizeLists = (MemberPrize) result;
            }
        });

        bought_id = getIntent().getStringExtra("id_editProductItem");
        description = getIntent().getStringExtra("edit_product_description");
        int position = getIntent().getIntExtra("position", 555);
        editProducts = totalEditProductData.data.bought.data.get(position);


        initView();

        //initial Dialog factory
        dialogFactory = new DialogFactory(EditProductsDetailActivity.this);


        setEditMemberRecyclere(editProducts.boughtMemberData.data);
//        setEditPrizeRecycler(editProducts.boughtPrizeData.data);


        if (editProducts.discount_type.equals("مبلغی")) {
            checkBox_amount.setChecked(true);
        } else if (editProducts.discount_type.equals("درصدی")) {
            checkBox_precentage.setChecked(true);
        }


//        if (!checkBox_precentage.isChecked() && !checkBox_amount.isChecked()) {
//            edt_discount.setHint(getResources().getString(R.string.percent_amount));
//            edt_discount.setEnabled(false);
//        }

//        txt_amount_title.setText(String.format("%s(%s) ", getResources().getString(R.string.amount), editProducts.currency));
//        txt_amount_title_chkbox.setText(String.format("%s (%s)", getResources().getString(R.string.amount), editProducts.currency));

        edt_amount.setText(editProducts.amount);
//        edt_discount.setText(editProducts.discount);
//        edt_paid.setText(editProducts.paid);
        edt_total_amount.setText(editProducts.cost);
        txt_unit.setText(editProducts.unit);


        txt_total_amount_title_edit.setText(String.format("%s (%s)", getResources().getString(R.string.unit_price), editProducts.currency));
        txt_paid_edit_product.setText(String.format("%s (%s)", getResources().getString(R.string.paid_amount), editProducts.currency));

//        txt_discount_edit_product_detail.setText(String.format("%s (در صورت تخفیف داشتن کالا)", getResources().getString(R.string.discount_amount)));

        txt_desc.setText(description);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        Typeface tf = Typeface.createFromAsset(getAssets(), "BYekan.ttf");
        edt_amount.setTypeface(tf);
        edt_total_amount.setTypeface(tf);
//        edt_paid.setTypeface(tf);
//        edt_discount.setTypeface(tf);


        edt_amount.addTextChangedListener(new EditTextWatcher(edt_amount));
        edt_total_amount.addTextChangedListener(new EditTextWatcher(edt_total_amount));
//        edt_paid.addTextChangedListener(new EditTextWatcher(edt_paid));
//        edt_discount.addTextChangedListener(new EditTextWatcher(edt_discount));



    }

    private void initView() {

        recyclerEditedMember = findViewById(R.id.recycler_edited_members);
//        recycler_prize = findViewById(R.id.recycler_prize);
        rl_addmember = findViewById(R.id.rl_addmember);
//        rl_spn_shop = findViewById(R.id.rl_spn_shop);
//        rl_prize = findViewById(R.id.rl_prize);
        rl_root = findViewById(R.id.layout_edit_product_detail);
        rl_home = findViewById(R.id.rl_home_edit_product_detail);
        rl_member_info = findViewById(R.id.rl_info_member_edit_detail);
//        rl_prize_info = findViewById(R.id.rl_info_prize_edit_detail);
        ll_return = findViewById(R.id.linear_return_editProductDetail);

        btn_register = findViewById(R.id.btn_register_edit_product_detail);

        avi = findViewById(R.id.avi_edit_product_detail);
//        edt_discount = findViewById(R.id.edt_discount);
        edt_total_amount = findViewById(R.id.edt_total_amount);
//        edt_paid = findViewById(R.id.edt_paid);
        edt_amount = findViewById(R.id.edt_amount);
//        txt_amount_title_chkbox = findViewById(R.id.txt_amount_title_chkbox);
        txt_paid_edit_product = findViewById(R.id.txt_paid_edit_product);
//        txt_discount_edit_product_detail=findViewById(R.id.txt_discount_edit_product_detail);

        txt_desc = findViewById(R.id.txt_desc_editProductDetail);
        txt_total_amount_title_edit = findViewById(R.id.txt_total_amount_title_edit);

        txt_unit = findViewById(R.id.txt_unit);
//        checkBox_precentage = findViewById(R.id.checkBox_precentage);
//        checkBox_amount = findViewById(R.id.checkBox_amount);

        rl_addmember.setOnClickListener(this);
//        rl_prize.setOnClickListener(this);
        rl_member_info.setOnClickListener(this);
//        rl_prize_info.setOnClickListener(this);

//        checkBox_precentage.setOnCheckedChangeListener(this);
//        checkBox_amount.setOnCheckedChangeListener(this);
        btn_register.setOnClickListener(this);
        ll_return.setOnClickListener(this);
        rl_home.setOnClickListener(this);


    }


    private void setEditMemberRecyclere(List<BoughtMember> data) {

        editMembers = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            editMembers.add(new RegisterMemberEditModel(data.get(i).name,
                    editProducts.boughtMemberData.data.get(i).id));
        }
        updateEditMemberList(editMembers);
    }


    // initializing edited member list
    public void updateEditMemberList(ArrayList<RegisterMemberEditModel> editMembers) {

        recyclerEditedMember.setLayoutManager(new GridLayoutManager(EditProductsDetailActivity.this, 3));
        adapter_edited = new RegisterMemberEditAdapter(editMembers, EditProductsDetailActivity.this);
        recyclerEditedMember.setAdapter(adapter_edited);
    }

    private void setEditPrizeRecycler(List<BoughtPrize> data) {

        sendPrizes = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            sendPrizes.add(new SendPrize(data.get(i).prize,
                    data.get(i).prizeTypeId));
        }
        updateEditPrizeList(sendPrizes);
    }

    private void updateEditPrizeList(List<SendPrize> sendPrizes) {
        recycler_prize.setLayoutManager(new GridLayoutManager(EditProductsDetailActivity.this, 2));
        editPrizeAdapter = new EditPrizeAdapter(sendPrizes, EditProductsDetailActivity.this);
        recycler_prize.setAdapter(editPrizeAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_addmember:
                showAddMemberDialog();
                break;

            case R.id.btn_register_edit_product_detail:
                sendRegisterData();
                break;


//            case R.id.rl_prize:
//                showPrizeDialog();
//                break;

            case R.id.linear_return_editProductDetail:
                finish();
                break;

            case R.id.rl_home_edit_product_detail:
                startActivity(new Intent(EditProductsDetailActivity.this,MainActivity.class));
                finish();
                break;

            case R.id.rl_info_member_edit_detail:
                info_type = "member_info_edit_product_detail";
                showInfoDialog(info_type);
                break;

//            case R.id.rl_info_prize_edit_detail:
//                info_type = "prize_info_edit_product_detail";
//                showInfoDialog(info_type);
//                break;

        }
    }
    private void showInfoDialog(String info_type) {
        dialogFactory.createInfoMemberPrizeDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {


            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_root ,info_type);
    }

    private void showAddMemberDialog() {
        editMembers = new ArrayList<>();
        final Dialog dialog = new Dialog(EditProductsDetailActivity.this);
        dialog.setContentView(R.layout.register_members_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // to show list of member items
        List<Member> members = new ArrayList<>();

//        for (int i = 0; i < totalEditProductData.data.member.size(); i++) {
//            for (int j = 0; j < totalEditProductData.data.member.get(i).size(); j++) {
//                members.add(new Member(totalEditProductData.data.member.get(i).get(j).name
//                        , totalEditProductData.data.member.get(i).get(j).id));
//            }
//        }
        for (int i = 0; i < initMemberPrizeLists.data.member.size(); i++) {
            for (int j = 0; j < initMemberPrizeLists.data.member.get(i).size(); j++) {
                members.add(new Member(initMemberPrizeLists.data.member.get(i).get(j).name
                        , initMemberPrizeLists.data.member.get(i).get(j).id));
            }
        }



        CheckBox checkBoxAll = dialog.findViewById(R.id.checkbox_all);
        RecyclerView recyclerview_members = dialog.findViewById(R.id.recyclerview_members);
        Button btn_exit_dialog = dialog.findViewById(R.id.btn_exit_dialog);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        recyclerview_members.setLayoutManager(new LinearLayoutManager(EditProductsDetailActivity.this));
        adapter_member = new RegisterMemberDialogAdapter(members, EditProductsDetailActivity.this);
        adapter_member.setListener(this);  // important or else the app will crashed
        recyclerview_members.setAdapter(adapter_member);

        // to select all members
        checkBoxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editMembers = new ArrayList<>();

                for (int i = 0; i < initMemberPrizeLists.data.member.size(); i++) {
                    for (int j = 0; j < initMemberPrizeLists.data.member.get(i).size(); j++) {
                        editMembers.add(new RegisterMemberEditModel(initMemberPrizeLists.data.member.get(i).get(j).name,
                                initMemberPrizeLists.data.member.get(i).get(j).id));
                    }
                }

                updateEditMemberList(editMembers);
                dialog.dismiss();
            }
        });

        img_close.setOnClickListener(v -> dialog.dismiss());
        btn_exit_dialog.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

//    private void showPrizeDialog() {
//        sendPrizes = new ArrayList<>();
//        final Dialog dialog = new Dialog(EditProductsDetailActivity.this);
//        dialog.setContentView(R.layout.prize_dialog);
//
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//
//        // to show list of member items
//        List<Prize> prizes = new ArrayList<>();
//        for (int i = 0; i < initMemberPrizeLists.data.prize.size(); i++) {
//            for (int j = 0; j < initMemberPrizeLists.data.prize.get(i).size(); j++) {
//                prizes.add(new Prize(initMemberPrizeLists.data.prize.get(i).get(j).title
//                        , initMemberPrizeLists.data.prize.get(i).get(j).id));
//            }
//        }
//
//        RecyclerView recycler_prize = dialog.findViewById(R.id.recycler_prize);
//        Button btn_exit_dialog = dialog.findViewById(R.id.btn_exit_dialog);
//        ImageView img_close = dialog.findViewById(R.id.img_close);
//        recycler_prize.setLayoutManager(new LinearLayoutManager(EditProductsDetailActivity.this));
//        adapter_prize = new PrizeAdapter(prizes, EditProductsDetailActivity.this);
//        adapter_prize.setListener(this);  // important or else the app will crashed
////        adapter_prize.setListener(this);  // important or else the app will crashed
//        recycler_prize.setAdapter(adapter_prize);
//
//        img_close.setOnClickListener(v -> dialog.dismiss());
//        btn_exit_dialog.setOnClickListener(v -> dialog.dismiss());
//
//        dialog.show();
//    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//        if (!checkBox_precentage.isChecked() && !checkBox_amount.isChecked()) {
//            edt_discount.setHint(getResources().getString(R.string.percent_amount));
//            edt_discount.setEnabled(false);
//            edt_discount.setText("");
//        }
//
//        switch (buttonView.getId()) {
//            case R.id.checkBox_amount:
//                if (isChecked) {
//                    checkBox_precentage.setChecked(false);
//                    edt_discount.setHint(getResources().getString(R.string.amount2));
//                    edt_discount.setText("");
//                    checkbox_text = getResources().getString(R.string.amount2_);
//                    edt_discount.setEnabled(true);
//                }
//                break;
//
//            case R.id.checkBox_precentage:
//
//                if (isChecked) {
//                    checkBox_amount.setChecked(false);
//                    edt_discount.setHint(getResources().getString(R.string.percent));
//                    edt_discount.setText("");
//                    checkbox_text = getResources().getString(R.string.percent);
//                    edt_discount.setEnabled(true);
//                }
//                break;
//        }
//    }

    @Override
    public void prizeOnClicked(String title, String id, Boolean chkbox) {

        if (chkbox) {
            createPrizeDetailDialog(title, id);
        } else {
            if (sendPrizes.size() > 0) {
                for (int i = 0; i < sendPrizes.size(); i++) {
                    if (sendPrizes.get(i).getId().equals(id)) {
                        sendPrizes.remove(i);
                    }
                }
                updateEditPrizeList(sendPrizes);
            }
        }
    }

    private void createPrizeDetailDialog(String title, String id) {

        dialogFactory.createPrizeDetailDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {
                String desc = params[0];
//                String title = params[1];
                String id = params[2];
                sendPrizes.add(new SendPrize(desc, id));

                updateEditPrizeList(sendPrizes);
            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, title, id, rl_root);
    }

    @Override
    public void onClicked(String name, String id, Boolean chkbox) {
        if (chkbox) {
            editMembers.add(new RegisterMemberEditModel(name, id));

        } else {

            if (editMembers.size() > 0) {
                for (int i = 0; i < editMembers.size(); i++) {
                    if (editMembers.get(i).txt_name.equals(name)) {
                        editMembers.remove(i);
                    }
                }
            }
        }
        updateEditMemberList(editMembers);
    }

    private void sendRegisterData() {

        avi.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.GONE);

        EditProductDetailUpdateSendData sendData = new EditProductDetailUpdateSendData();
        sendData.setMember(editMembers);
        sendData.setPrize(sendPrizes);
        sendData.setAmount(edt_amount.getText().toString());

        if(!edt_total_amount.getText().toString().equals("ثبت نشده")){
            sendData.setCost(edt_total_amount.getText().toString());
        }


//        if(!edt_paid.getText().toString().equals("ثبت نشده")){
//            sendData.setPaid(edt_paid.getText().toString());
//        }

        sendData.setBought_id(bought_id);


//        if (checkBox_precentage.isChecked()) {
//            sendData.setDiscount_type("percent");
//
//            if(!edt_discount.getText().toString().equals("ثبت نشده")){
//                sendData.setDiscount_amount(edt_discount.getText().toString());
//            }else{
//                sendData.setDiscount_amount("not_set");
//            }
//
//
//        } else if (checkBox_amount.isChecked()) {
//            sendData.setDiscount_type("amount");
//            if(!edt_discount.getText().toString().equals("ثبت نشده")){
//                sendData.setDiscount_amount(edt_discount.getText().toString());
//            }else{
//                sendData.setDiscount_amount("not_set");
//            }
//
//
//        }else{
//            sendData.setDiscount_type("not_set");
//        }


        Service service = new ServiceProvider(this).getmService();
        Call<UpdateEditProductDetailResult> call = service.updateEditProductDetail(sendData);
        call.enqueue(new Callback<UpdateEditProductDetailResult>() {
            @Override
            public void onResponse(Call<UpdateEditProductDetailResult> call, Response<UpdateEditProductDetailResult> response) {
                if (response.code() == 200) {

                    avi.setVisibility(View.GONE);
                    btn_register.setVisibility(View.VISIBLE);
//                    UpdateEditProductDetailResult getResult = response.body();
                    Toast.makeText(EditProductsDetailActivity.this, getResources().getString(R.string.update_done), Toast.LENGTH_SHORT).show();
                    finish();

                } else if(response.code()==422) {

                    avi.setVisibility(View.GONE);
                    btn_register.setVisibility(View.VISIBLE);

                    builderMember = null;
                    builderShopId = null;
                    builderCost = null;
                    builderPaid = null;
                    builderDiscountAmount = null;
                    builderDate = null;
                    buliderPrize = null;
                    APIError422 apiError = ErrorUtils.parseError422(response);


                    if (apiError.errors.shopId != null) {
                        builderShopId = new StringBuilder();
                        for (String a : apiError.errors.shopId) {
                            builderShopId.append("").append(a).append(" ");
                        }
                    }

                    if (apiError.errors.cost != null) {
                        builderCost = new StringBuilder();
                        for (String b : apiError.errors.cost) {
                            builderCost.append("").append(b).append(" ");
                        }
                    }

                    if (apiError.errors.paid != null) {
                        builderPaid = new StringBuilder();
                        for (String a : apiError.errors.paid) {
                            builderPaid.append("").append(a).append(" ");
                        }
                    }

                    if (apiError.errors.member != null) {
                        builderMember = new StringBuilder();
                        for (String b : apiError.errors.member) {
                            builderMember.append("").append(b).append(" ");
                        }
                    }

                    if (apiError.errors.discountAmount != null) {
                        builderDiscountAmount = new StringBuilder();
                        for (String c : apiError.errors.discountAmount) {
                            builderDiscountAmount.append("").append(c).append(" ");
                        }
                    }

                    if (apiError.errors.date != null) {
                        builderDate = new StringBuilder();
                        for (String c : apiError.errors.date) {
                            builderDate.append("").append(c).append(" ");
                        }
                    }

                    if (apiError.errors.prize != null) {
                        buliderPrize = new StringBuilder();
                        for (String b : apiError.errors.prize) {
                            buliderPrize.append("").append(b).append(" ");
                        }
                    }

                    if (builderMember != null) {
                        Toast.makeText(EditProductsDetailActivity.this, "" + builderMember, Toast.LENGTH_SHORT).show();
                    }
                    if (builderShopId != null) {
                        Toast.makeText(EditProductsDetailActivity.this, "" + builderShopId, Toast.LENGTH_SHORT).show();
                    }
                    if (builderCost != null) {
                        Toast.makeText(EditProductsDetailActivity.this, "" + builderCost, Toast.LENGTH_SHORT).show();
                    }
                    if (builderPaid != null) {
                        Toast.makeText(EditProductsDetailActivity.this, "" + builderPaid, Toast.LENGTH_SHORT).show();
                    }
                    if (builderDiscountAmount != null) {
                        Toast.makeText(EditProductsDetailActivity.this, "" + builderDiscountAmount, Toast.LENGTH_SHORT).show();
                    }
                    if (builderDate != null) {
                        Toast.makeText(EditProductsDetailActivity.this, "" + builderDate, Toast.LENGTH_SHORT).show();
                    }
                    if (buliderPrize != null) {
                        Toast.makeText(EditProductsDetailActivity.this, "" + buliderPrize, Toast.LENGTH_SHORT).show();
                    }


                }else{
                    avi.setVisibility(View.GONE);
                    btn_register.setVisibility(View.VISIBLE);
                    Toast.makeText(EditProductsDetailActivity.this, getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateEditProductDetailResult> call, Throwable t) {
                avi.setVisibility(View.GONE);
                btn_register.setVisibility(View.VISIBLE);
                Toast.makeText(EditProductsDetailActivity.this, getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
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
}
