package com.example.panelist.ui.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.panelist.R;
import com.example.panelist.controllers.adapters.AdapterEditPrize;
import com.example.panelist.controllers.adapters.AdapterPrize;
import com.example.panelist.controllers.adapters.AdapterRegisterMemberDialog;
import com.example.panelist.controllers.adapters.AdapterRegisterMemberEdit;
import com.example.panelist.controllers.viewholders.PrizeItemInteraction;
import com.example.panelist.controllers.viewholders.RegisterItemInteraction;
import com.example.panelist.models.api_error.APIError422;
import com.example.panelist.models.api_error.ErrorUtils;
import com.example.panelist.models.register.GetShopId;
import com.example.panelist.models.register.Member;
import com.example.panelist.models.register.Prize;
import com.example.panelist.models.register.RegisterMemberEditModel;
import com.example.panelist.models.register.RegisterModel;
import com.example.panelist.models.register.SendPrize;
import com.example.panelist.models.register.SendRegisterTotalData;
import com.example.panelist.network.Service;
import com.example.panelist.network.ServiceProvider;
import com.example.panelist.utilities.DialogFactory;
import com.example.panelist.utilities.GeneralTools;
import com.example.panelist.utilities.RxBus;
import com.example.panelist.utilities.Time;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRegisterActivity extends AppCompatActivity
        implements View.OnClickListener, RegisterItemInteraction, PrizeItemInteraction, CompoundButton.OnCheckedChangeListener {

    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    DialogFactory dialogFactory;
    RegisterModel registerModel;
    Button btn_addMember, btn_register, btn_prize;
    AVLoadingIndicatorView avi;
    AdapterRegisterMemberDialog adapter_member;
    AdapterRegisterMemberEdit adapter_edited;
    AdapterPrize adapter_prize;
    AdapterEditPrize adapterEditPrize;
    List<SendPrize> sendPrizes;
    ArrayList<RegisterMemberEditModel> editMembers;
    RecyclerView recyclerEditedMember, recycler_prize;
    RelativeLayout rl_spn_shop;
    Spinner spn_shop;
    EditText edtDate, edt_discount, edt_total_amount, edt_paid;
    CheckBox checkBox_precentage, checkBox_amount;
    private PersianDatePickerDialog picker;
    String date = "";
    String str_spnItemId;
    String checkbox_text;

    LinearLayout layout_register;
    // for handling422
    private StringBuilder builderPaid, builderCost, builderDiscountAmount,
            builderShopId, builderMember, builderDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);

        // to check internet connection
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(NewRegisterActivity.this, findViewById(R.id.drawer_layout_home));
            }
        };

        //get data from register fragment
        disposable = RxBus.RegisterModel.subscribeRegisterModel(result -> {
            if (result instanceof RegisterModel) {
                registerModel = (RegisterModel) result;
            }
        });

        initView();
        //initial Dialog factory
        dialogFactory = new DialogFactory(NewRegisterActivity.this);
        setSpinner();

        checkBox_precentage.setChecked(true);
        edt_discount.setHint("درصد");
    }

    private void initView() {
        btn_addMember = findViewById(R.id.add_member);
        recyclerEditedMember = findViewById(R.id.recycler_edited_members);
        recycler_prize = findViewById(R.id.recycler_prize);
        rl_spn_shop = findViewById(R.id.rl_spn_shop);
        spn_shop = findViewById(R.id.spn_shop);
        btn_register = findViewById(R.id.btn_register);
        btn_prize = findViewById(R.id.btn_prize);
        avi = findViewById(R.id.avi_register);
        edtDate = findViewById(R.id.edtDate);
        edt_discount = findViewById(R.id.edt_discount);
        edt_total_amount = findViewById(R.id.edt_total_amount);
        edt_paid = findViewById(R.id.edt_paid);
        checkBox_precentage = findViewById(R.id.checkBox_precentage);
        checkBox_amount = findViewById(R.id.checkBox_amount);
        layout_register = findViewById(R.id.layout_register);
        btn_addMember.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        edtDate.setOnClickListener(this);
        btn_prize.setOnClickListener(this);

        checkBox_precentage.setOnCheckedChangeListener(this);
        checkBox_amount.setOnCheckedChangeListener(this);


        edtDate.setText(Time.getNowPersianDate());
    }


    private void setSpinner() {

        List<String> shopList = new ArrayList<>();
        for (int i = 0; i < registerModel.data.shop.size(); i++) {
//            shopList.add(App.provinceList.data.get(i).province);
            for (int j = 0; j < registerModel.data.shop.get(i).size(); j++) {
                shopList.add(registerModel.data.shop.get(i).get(j).title);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewRegisterActivity.this, android.R.layout.simple_spinner_item, shopList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spn_shop.setAdapter(adapter);

        spn_shop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                for (int i = 0; i < registerModel.data.shop.size(); i++) {
                    for (int j = 0; j < registerModel.data.shop.get(i).size(); j++) {
                        str_spnItemId = registerModel.data.shop.get(i).get(position).id;
                    }
                }


//                str_spnItemId = String.valueOf(spn_shop.getSelectedItemId());
//                String a = str_spnItemId;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.add_member:
                showAddMemberDialog();
                break;

            case R.id.btn_register:


                sendData();
//                if((edt_total_amount.getText().toString().length()>0 || edt_paid.getText().toString().length()>0)
//                   || (edt_total_amount.getText().toString().length()>0 && edt_paid.getText().toString().length()>0)){
//
//                }

                break;

            case R.id.edtDate:
                showCalander();
                break;

            case R.id.btn_prize:
                showPrizeDialog();
                break;
        }
    }


    private void showCalander() {
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtDate, InputMethodManager.SHOW_IMPLICIT);

        picker = new PersianDatePickerDialog(this)
                .setPositiveButtonString("تایید")
                .setNegativeButton("انصراف")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1397)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Color.GRAY)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {
                        date = persianCalendar.getPersianYear() + "/" +
                                (String.valueOf(persianCalendar.getPersianMonth()).length() < 2 ? "0" + persianCalendar.getPersianMonth() : persianCalendar.getPersianMonth()) + "/" +
                                (String.valueOf(persianCalendar.getPersianDay()).length() < 2 ? "0" + persianCalendar.getPersianDay() : persianCalendar.getPersianDay());

                        edtDate.setText(date);
                    }

                    @Override
                    public void onDismissed() {
                    }
                });
        picker.show();
    }

    private void showAddMemberDialog() {

        editMembers = new ArrayList<>();

        final Dialog dialog = new Dialog(NewRegisterActivity.this);
        dialog.setContentView(R.layout.register_members_dialog);
        dialog.setTitle("Title...");


        // to show list of member items
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < registerModel.data.member.size(); i++) {
            for (int j = 0; j < registerModel.data.member.get(i).size(); j++) {
                members.add(new Member(registerModel.data.member.get(i).get(j).name
                        , registerModel.data.member.get(i).get(j).id));
            }
        }

        CheckBox checkBoxAll = dialog.findViewById(R.id.checkbox_all);
        RecyclerView recyclerview_members = dialog.findViewById(R.id.recyclerview_members);
        recyclerview_members.setLayoutManager(new LinearLayoutManager(NewRegisterActivity.this));
        adapter_member = new AdapterRegisterMemberDialog(members, NewRegisterActivity.this);
        adapter_member.setListener(this);  // important or else the app will crashed
        recyclerview_members.setAdapter(adapter_member);

        // to select all members
        checkBoxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editMembers = new ArrayList<>();
                for (int i = 0; i < registerModel.data.member.size(); i++) {
                    for (int j = 0; j < registerModel.data.member.get(i).size(); j++) {
                        editMembers.add(new RegisterMemberEditModel(registerModel.data.member.get(i).get(j).name,
                                registerModel.data.member.get(i).get(j).id));
                    }
                }
                updateEditMemberList(editMembers);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void showPrizeDialog() {
        sendPrizes = new ArrayList<>();
        final Dialog dialog = new Dialog(NewRegisterActivity.this);
        dialog.setContentView(R.layout.prize_dialog);
        dialog.setTitle("Title...");

        // to show list of member items
        List<Prize> prizes = new ArrayList<>();
        for (int i = 0; i < registerModel.data.prize.size(); i++) {
            for (int j = 0; j < registerModel.data.prize.get(i).size(); j++) {
                prizes.add(new Prize(registerModel.data.prize.get(i).get(j).title
                        , registerModel.data.prize.get(i).get(j).id));
            }
        }

        RecyclerView recycler_prize = dialog.findViewById(R.id.recycler_prize);
        recycler_prize.setLayoutManager(new LinearLayoutManager(NewRegisterActivity.this));
        adapter_prize = new AdapterPrize(prizes, NewRegisterActivity.this);
        adapter_prize.setListener(this);  // important or else the app will crashed
//        adapter_prize.setListener(this);  // important or else the app will crashed
        recycler_prize.setAdapter(adapter_prize);

        dialog.show();
    }


    // to setCheck single checkbox and show in list
    @Override
    public void onClicked(String name, String id, Boolean chkbox) {

        if (chkbox) {
            editMembers.add(new RegisterMemberEditModel(name, id));
//            updateEditMemberList(editMembers);
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

    // initializing edited member list
    public void updateEditMemberList(ArrayList<RegisterMemberEditModel> editMembers) {

        recyclerEditedMember.setLayoutManager(new GridLayoutManager(NewRegisterActivity.this, 3));
        adapter_edited = new AdapterRegisterMemberEdit(editMembers, NewRegisterActivity.this);
        recyclerEditedMember.setAdapter(adapter_edited);

        if (editMembers.size() > 0) {
            btn_addMember.setText("ویرایش اعضای خانواده");
        } else {
            btn_addMember.setText("افزودن اعضای خانواده");
        }
    }

    private void sendData() {

        String total_amount = edt_total_amount.getText().toString();
        String total_paid = edt_paid.getText().toString();
        String discount_amount = edt_discount.getText().toString();
        String date = edtDate.getText().toString();

        SendRegisterTotalData sendData = new SendRegisterTotalData();
        sendData.setMember(editMembers);
        sendData.setPrize(sendPrizes);
        sendData.setShop_id(str_spnItemId);
        sendData.setCost(total_amount);
        sendData.setPaid(total_paid);

        String chechBox_type = checkbox_text;
        if (chechBox_type.equals("مبلغی")) {
            sendData.setDiscount_type("amount");
        } else {
            sendData.setDiscount_type("percent");
        }
        sendData.setDiscount_amount(discount_amount);
        sendData.setDate(date);

        Service service = new ServiceProvider(this).getmService();
        Call<GetShopId> call = service.registerNewShop(sendData);
        call.enqueue(new Callback<GetShopId>() {
            @Override
            public void onResponse(Call<GetShopId> call, Response<GetShopId> response) {
                if (response.code() == 200) {

                    String shopping_id = response.body().data;


                } else if (response.code() == 422) {

                    builderMember = null;
                    builderShopId = null;
                    builderCost = null;
                    builderPaid = null;
                    builderDiscountAmount = null;
                    builderDate = null;

                    APIError422 apiError = ErrorUtils.parseError422(response);

                    if (apiError.errors.member != null) {
                        builderMember = new StringBuilder();
                        for (String b : apiError.errors.member) {
                            builderMember.append("").append(b).append(" ");
                        }
                    }

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


                    if (builderMember != null) {
                        Toast.makeText(NewRegisterActivity.this, "" + builderMember, Toast.LENGTH_SHORT).show();
                    }
                    if (builderShopId != null) {
                        Toast.makeText(NewRegisterActivity.this, "" + builderShopId, Toast.LENGTH_SHORT).show();
                    }
                    if (builderCost != null) {
                        Toast.makeText(NewRegisterActivity.this, "" + builderCost, Toast.LENGTH_SHORT).show();
                    }
                    if (builderPaid != null) {
                        Toast.makeText(NewRegisterActivity.this, "" + builderPaid, Toast.LENGTH_SHORT).show();
                    }
                    if (builderDiscountAmount != null) {
                        Toast.makeText(NewRegisterActivity.this, "" + builderDiscountAmount, Toast.LENGTH_SHORT).show();
                    }
                    if (builderDate != null) {
                        Toast.makeText(NewRegisterActivity.this, "" + builderDate, Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(NewRegisterActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetShopId> call, Throwable t) {
                Toast.makeText(NewRegisterActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
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

        Context context = NewRegisterActivity.this;
        dialogFactory.createPrizeDetailDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {
                String desc = params[0];
                String title = params[1];
                String id = params[2];
                sendPrizes.add(new SendPrize(desc, id));
                updateEditPrizeList(sendPrizes);
            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, title, id, layout_register);
    }

    private void updateEditPrizeList(List<SendPrize> sendPrizes) {

        recycler_prize.setLayoutManager(new GridLayoutManager(NewRegisterActivity.this, 3));
        adapterEditPrize = new AdapterEditPrize(sendPrizes, NewRegisterActivity.this);
        recycler_prize.setAdapter(adapterEditPrize);
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {

        switch (view.getId()) {
            case R.id.checkBox_amount:
                if (isChecked) {
                    Toast.makeText(this, "amount", Toast.LENGTH_SHORT).show();
                    checkBox_precentage.setChecked(false);
                    edt_discount.setHint("مبلغ");
                    checkbox_text = "مبلغ";
                }
                break;

            case R.id.checkBox_precentage:
                if (isChecked) {
                    Toast.makeText(this, "precentage", Toast.LENGTH_SHORT).show();
                    checkBox_amount.setChecked(false);
                    edt_discount.setHint("درصد");
                    checkbox_text = "درصد";
                }
                break;
        }
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
