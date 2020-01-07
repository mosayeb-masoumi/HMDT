package com.rahbarbazaar.checkpanel.ui.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.codesgood.views.JustifiedTextView;
import com.codesgood.views.JustifiedTextView;
import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.controllers.adapters.EditPrizeAdapter;
import com.rahbarbazaar.checkpanel.controllers.adapters.PrizeAdapter;
import com.rahbarbazaar.checkpanel.controllers.adapters.RegisterMemberDialogAdapter;
import com.rahbarbazaar.checkpanel.controllers.adapters.RegisterMemberEditAdapter;
import com.rahbarbazaar.checkpanel.controllers.interfaces.PrizeItemInteraction;
import com.rahbarbazaar.checkpanel.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.checkpanel.models.edit_products.BoughtMember;
import com.rahbarbazaar.checkpanel.models.edit_products.BoughtPrize;
import com.rahbarbazaar.checkpanel.models.edit_products.EditProductDetailUpdateSendData;
import com.rahbarbazaar.checkpanel.models.edit_products.EditProducts;
import com.rahbarbazaar.checkpanel.models.edit_products.TotalEditProductData;
import com.rahbarbazaar.checkpanel.models.edit_products.UpdateEditProductDetailResult;
import com.rahbarbazaar.checkpanel.models.register.Member;
import com.rahbarbazaar.checkpanel.models.register.Prize;
import com.rahbarbazaar.checkpanel.models.register.RegisterMemberEditModel;
import com.rahbarbazaar.checkpanel.models.register.SendPrize;
import com.rahbarbazaar.checkpanel.network.Service;
import com.rahbarbazaar.checkpanel.network.ServiceProvider;
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity;
import com.rahbarbazaar.checkpanel.utilities.DialogFactory;
import com.rahbarbazaar.checkpanel.utilities.GeneralTools;
import com.rahbarbazaar.checkpanel.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductsDetailActivity extends CustomBaseActivity
        implements View.OnClickListener, RegisterItemInteraction, PrizeItemInteraction, CompoundButton.OnCheckedChangeListener {


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
    Button btn_register, btn_cancel;
    EditText edt_discount, edt_total_amount, edt_paid, edt_amount;
    TextView txt_unit, txt_amount_title, txt_currency,txt_currency2;
    JustifiedTextView txt_desc;

    CheckBox checkBox_precentage, checkBox_amount;

    AVLoadingIndicatorView avi;
    String bought_id;
    String checkbox_text = "";
    String description ;



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


        bought_id = getIntent().getStringExtra("id_editProductItem");
        description = getIntent().getStringExtra("edit_product_description");
        int position = getIntent().getIntExtra("position", 555);
        editProducts = totalEditProductData.data.bought.data.get(position);


        initView();

        //initial Dialog factory
        dialogFactory = new DialogFactory(EditProductsDetailActivity.this);


        setEditMemberRecyclere(editProducts.boughtMemberData.data);
        setEditPrizeRecycler(editProducts.boughtPrizeData.data);


        if (editProducts.discount_type.equals("مبلغی")) {
            checkBox_amount.setChecked(true);
        } else if (editProducts.discount_type.equals("درصدی")) {
            checkBox_precentage.setChecked(true);
        }


        if (!checkBox_precentage.isChecked() && !checkBox_amount.isChecked()) {
            edt_discount.setHint(getResources().getString(R.string.percent_amount));
            edt_discount.setEnabled(false);
        }

        txt_amount_title.setText(String.format("%s(%s)", getResources().getString(R.string.amount), editProducts.currency));
        edt_amount.setText(editProducts.amount);
        edt_discount.setText(editProducts.discount);
        edt_paid.setText(editProducts.paid);
        edt_total_amount.setText(editProducts.cost);
        txt_unit.setText(editProducts.unit);
        txt_currency.setText(String.format("(%s)", editProducts.currency));
        txt_currency2.setText(String.format("(%s)", editProducts.currency));

        txt_desc.setText(description);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


    }

    private void initView() {

        recyclerEditedMember = findViewById(R.id.recycler_edited_members);
        recycler_prize = findViewById(R.id.recycler_prize);
        rl_addmember = findViewById(R.id.rl_addmember);
        rl_spn_shop = findViewById(R.id.rl_spn_shop);
        rl_prize = findViewById(R.id.rl_prize);
        rl_root = findViewById(R.id.layout_edit_product_detail);

        btn_register = findViewById(R.id.btn_register_edit_product_detail);
        btn_cancel = findViewById(R.id.btn_cancel_editProductDetail);
        avi = findViewById(R.id.avi_edit_product_detail);
        edt_discount = findViewById(R.id.edt_discount);
        edt_total_amount = findViewById(R.id.edt_total_amount);
        edt_paid = findViewById(R.id.edt_paid);
        edt_amount = findViewById(R.id.edt_amount);
        txt_amount_title = findViewById(R.id.txt_amount_title);
        txt_currency = findViewById(R.id.txt_currency);
        txt_currency2= findViewById(R.id.txt_currency2);
        txt_desc = findViewById(R.id.txt_desc_editProductDetail);

        txt_unit = findViewById(R.id.txt_unit);
        checkBox_precentage = findViewById(R.id.checkBox_precentage);
        checkBox_amount = findViewById(R.id.checkBox_amount);

        rl_addmember.setOnClickListener(this);
        rl_prize.setOnClickListener(this);
        checkBox_precentage.setOnCheckedChangeListener(this);
        checkBox_amount.setOnCheckedChangeListener(this);
        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
//        rl_calander.setOnClickListener(this);
//        btn_register.setOnClickListener(this);
//        btn_cancel_register.setOnClickListener(this);

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
        recycler_prize.setLayoutManager(new GridLayoutManager(EditProductsDetailActivity.this, 3));
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


            case R.id.rl_prize:
                showPrizeDialog();
                break;

            case R.id.btn_cancel_editProductDetail:
                finish();
                break;
        }
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

        for (int i = 0; i < totalEditProductData.data.member.size(); i++) {
            for (int j = 0; j < totalEditProductData.data.member.get(i).size(); j++) {
                members.add(new Member(totalEditProductData.data.member.get(i).get(j).name
                        , totalEditProductData.data.member.get(i).get(j).id));
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

                for (int i = 0; i < totalEditProductData.data.member.size(); i++) {
                    for (int j = 0; j < totalEditProductData.data.member.get(i).size(); j++) {
                        editMembers.add(new RegisterMemberEditModel(totalEditProductData.data.member.get(i).get(j).name,
                                totalEditProductData.data.member.get(i).get(j).id));
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

    private void showPrizeDialog() {
        sendPrizes = new ArrayList<>();
        final Dialog dialog = new Dialog(EditProductsDetailActivity.this);
        dialog.setContentView(R.layout.prize_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // to show list of member items
        List<Prize> prizes = new ArrayList<>();

        for (int i = 0; i < totalEditProductData.data.prize.size(); i++) {
            for (int j = 0; j < totalEditProductData.data.prize.get(i).size(); j++) {
                prizes.add(new Prize(totalEditProductData.data.prize.get(i).get(j).title
                        , totalEditProductData.data.prize.get(i).get(j).id));
            }
        }

        RecyclerView recycler_prize = dialog.findViewById(R.id.recycler_prize);
        Button btn_exit_dialog = dialog.findViewById(R.id.btn_exit_dialog);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        recycler_prize.setLayoutManager(new LinearLayoutManager(EditProductsDetailActivity.this));
        adapter_prize = new PrizeAdapter(prizes, EditProductsDetailActivity.this);
        adapter_prize.setListener(this);  // important or else the app will crashed
//        adapter_prize.setListener(this);  // important or else the app will crashed
        recycler_prize.setAdapter(adapter_prize);

        img_close.setOnClickListener(v -> dialog.dismiss());
        btn_exit_dialog.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (!checkBox_precentage.isChecked() && !checkBox_amount.isChecked()) {
            edt_discount.setHint(getResources().getString(R.string.percent_amount));
            edt_discount.setEnabled(false);
            edt_discount.setText("");
        }

        switch (buttonView.getId()) {
            case R.id.checkBox_amount:
                if (isChecked) {
                    checkBox_precentage.setChecked(false);
                    edt_discount.setHint(getResources().getString(R.string.amount2));
                    edt_discount.setText("");
                    checkbox_text = getResources().getString(R.string.amount2_);
                    edt_discount.setEnabled(true);
                }
                break;

            case R.id.checkBox_precentage:

                if (isChecked) {
                    checkBox_amount.setChecked(false);
                    edt_discount.setHint(getResources().getString(R.string.percent));
                    edt_discount.setText("");
                    checkbox_text = getResources().getString(R.string.percent);
                    edt_discount.setEnabled(true);
                }
                break;
        }
    }

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
        sendData.setCost(edt_total_amount.getText().toString());
        sendData.setPaid(edt_paid.getText().toString());
        sendData.setBought_id(bought_id);


        if (checkBox_precentage.isChecked()) {
            sendData.setDiscount_type("percent");
            sendData.setDiscount_amount(edt_discount.getText().toString());
        } else if (checkBox_amount.isChecked()) {
            sendData.setDiscount_type("amount");
            sendData.setDiscount_amount(edt_discount.getText().toString());
        }


        Service service = new ServiceProvider(this).getmService();
        Call<UpdateEditProductDetailResult> call = service.updateEditProductDetail(sendData);
        call.enqueue(new Callback<UpdateEditProductDetailResult>() {
            @Override
            public void onResponse(Call<UpdateEditProductDetailResult> call, Response<UpdateEditProductDetailResult> response) {
                if (response.code() == 200) {

                    avi.setVisibility(View.GONE);
                    btn_register.setVisibility(View.VISIBLE);
                    UpdateEditProductDetailResult getResult = response.body();
                    Toast.makeText(EditProductsDetailActivity.this, getResources().getString(R.string.update_done), Toast.LENGTH_SHORT).show();
                    finish();

                } else {
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
