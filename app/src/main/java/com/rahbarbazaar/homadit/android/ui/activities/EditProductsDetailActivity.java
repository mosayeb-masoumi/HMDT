package com.rahbarbazaar.homadit.android.ui.activities;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.codesgood.views.JustifiedTextView;
import com.codesgood.views.JustifiedTextView;
//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.RegisterMemberDialogAdapter;
import com.rahbarbazaar.homadit.android.controllers.adapters.RegisterMemberEditAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.homadit.android.models.api_error.APIError422;
import com.rahbarbazaar.homadit.android.models.api_error.ErrorUtils;
import com.rahbarbazaar.homadit.android.models.edit_products.BoughtMember;
import com.rahbarbazaar.homadit.android.models.edit_products.EditProductDetailUpdateSendData;
import com.rahbarbazaar.homadit.android.models.edit_products.EditProducts;
import com.rahbarbazaar.homadit.android.models.edit_products.TotalEditProductData;
import com.rahbarbazaar.homadit.android.models.edit_products.UpdateEditProductDetailResult;
import com.rahbarbazaar.homadit.android.models.register.Member;
import com.rahbarbazaar.homadit.android.models.register.RegisterMemberEditModel;
import com.rahbarbazaar.homadit.android.models.register.SendPrize;
import com.rahbarbazaar.homadit.android.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.homadit.android.network.Service;
import com.rahbarbazaar.homadit.android.network.ServiceProvider;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.rahbarbazaar.homadit.android.utilities.DialogFactory;
import com.rahbarbazaar.homadit.android.utilities.EditTextWatcher;
import com.rahbarbazaar.homadit.android.utilities.GeneralTools;
import com.rahbarbazaar.homadit.android.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductsDetailActivity extends CustomBaseActivity
        implements View.OnClickListener, RegisterItemInteraction {

    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    DialogFactory dialogFactory;
    TotalEditProductData totalEditProductData;
    EditProducts editProducts;
    RecyclerView recyclerEditedMember;
    ArrayList<RegisterMemberEditModel> editMembers;
    RegisterMemberEditAdapter adapter_edited;
    RegisterMemberDialogAdapter adapter_member;
    List<SendPrize> sendPrizes;
    RelativeLayout rl_addmember, rl_root;
    Button btn_register;
    EditText edt_total_amount, edt_amount;
    TextView txt_unit,txt_total_amount_title_edit;
    JustifiedTextView txt_desc;
    LinearLayout ll_return;
    RelativeLayout rl_home,rl_member_info;
    AVLoadingIndicatorView avi;
    String info_type,description,bought_id;

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

        edt_amount.setText(editProducts.amount);
        edt_total_amount.setText(editProducts.cost);
        txt_unit.setText(editProducts.unit);
//        txt_total_amount_title_edit.setText(String.format("%s (%s)", getResources().getString(R.string.unit_price), editProducts.currency));
        txt_desc.setText(description);
        Typeface tf = Typeface.createFromAsset(getAssets(), "BYekan.ttf");
        edt_amount.setTypeface(tf);
        edt_total_amount.setTypeface(tf);
        edt_amount.addTextChangedListener(new EditTextWatcher(edt_amount));
        edt_total_amount.addTextChangedListener(new EditTextWatcher(edt_total_amount));
    }

    private void initView() {
        recyclerEditedMember = findViewById(R.id.recycler_edited_members);
        rl_addmember = findViewById(R.id.rl_addmember);
        rl_root = findViewById(R.id.layout_edit_product_detail);
        rl_home = findViewById(R.id.rl_home_edit_product_detail);
        rl_member_info = findViewById(R.id.rl_info_member_edit_detail);
        ll_return = findViewById(R.id.linear_return_editProductDetail);
        btn_register = findViewById(R.id.btn_register_edit_product_detail);
        avi = findViewById(R.id.avi_edit_product_detail);
        edt_total_amount = findViewById(R.id.edt_total_amount);
        edt_amount = findViewById(R.id.edt_amount);
        txt_desc = findViewById(R.id.txt_desc_editProductDetail);
        txt_total_amount_title_edit = findViewById(R.id.txt_total_amount_title_edit);
        txt_unit = findViewById(R.id.txt_unit);
        rl_addmember.setOnClickListener(this);
        rl_member_info.setOnClickListener(this);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_addmember:
                showAddMemberDialog();
                break;

            case R.id.btn_register_edit_product_detail:
                sendRegisterData();
                break;

            case R.id.linear_return_editProductDetail:
                finish();
                break;

            case R.id.rl_home_edit_product_detail:
                startActivity(new Intent(EditProductsDetailActivity.this,MainActivity.class));
                finish();
                break;


            case R.id.rl_info_member_edit_detail:
                info_type = "member_info_purchased_item";
                showInfoDialog(info_type);
                break;

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

        for (int i = 0; i < initMemberPrizeLists.data.member.size(); i++) {
            for (int j = 0; j < initMemberPrizeLists.data.member.get(i).size(); j++) {
                members.add(new Member(initMemberPrizeLists.data.member.get(i).get(j).name
                        , initMemberPrizeLists.data.member.get(i).get(j).id ,false));
            }
        }


//        todo check this
        String spn_name = "present";

//        CheckBox checkBoxAll = dialog.findViewById(R.id.checkbox_all);
        RelativeLayout rl_check_all = dialog.findViewById(R.id.rl_check_all);
        RecyclerView recyclerview_members = dialog.findViewById(R.id.recyclerview_members);
        Button btn_exit_dialog = dialog.findViewById(R.id.btn_exit_dialog);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        recyclerview_members.setLayoutManager(new LinearLayoutManager(EditProductsDetailActivity.this));
        adapter_member = new RegisterMemberDialogAdapter(members, spn_name, dialog, EditProductsDetailActivity.this);
        adapter_member.setListener(this);  // important or else the app will crashed
        recyclerview_members.setAdapter(adapter_member);

//        // to select all members
//        checkBoxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                editMembers = new ArrayList<>();
//
//                for (int i = 0; i < initMemberPrizeLists.data.member.size(); i++) {
//                    for (int j = 0; j < initMemberPrizeLists.data.member.get(i).size(); j++) {
//                        editMembers.add(new RegisterMemberEditModel(initMemberPrizeLists.data.member.get(i).get(j).name,
//                                initMemberPrizeLists.data.member.get(i).get(j).id));
//                    }
//                }
//
//                updateEditMemberList(editMembers);
//                dialog.dismiss();
//            }
//        });

        rl_check_all.setOnClickListener(view -> {
            editMembers = new ArrayList<>();
            for (int i = 0; i < initMemberPrizeLists.data.member.size(); i++) {
                for (int j = 0; j < initMemberPrizeLists.data.member.get(i).size(); j++) {
                    editMembers.add(new RegisterMemberEditModel(initMemberPrizeLists.data.member.get(i).get(j).name,
                            initMemberPrizeLists.data.member.get(i).get(j).id));
                }
            }

            updateEditMemberList(editMembers);
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> dialog.dismiss());
        btn_exit_dialog.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    @Override
    public void onClicked(String name, String id, String spn_name, Dialog dialog, Boolean chkbox) {
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
        sendData.setBought_id(bought_id);

        Service service = new ServiceProvider(this).getmService();
        Call<UpdateEditProductDetailResult> call = service.updateEditProductDetail(sendData);
        call.enqueue(new Callback<UpdateEditProductDetailResult>() {
            @Override
            public void onResponse(Call<UpdateEditProductDetailResult> call, Response<UpdateEditProductDetailResult> response) {
                if (response.code() == 200) {

                    avi.setVisibility(View.GONE);
                    btn_register.setVisibility(View.VISIBLE);
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
