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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import com.example.panelist.R;
import com.example.panelist.controllers.adapters.AdapterRegisterMemberDialog;
import com.example.panelist.controllers.adapters.AdapterRegisterMemberEdit;
import com.example.panelist.controllers.viewholders.RegisterItemInteraction;
import com.example.panelist.models.register.Member;
import com.example.panelist.models.register.RegisterMemberEditModel;
import com.example.panelist.models.register.RegisterModel;
import com.example.panelist.models.register_newshop.NewShop;
import com.example.panelist.models.register_newshop.NewShopSendData;
import com.example.panelist.network.Service;
import com.example.panelist.network.ServiceProvider;
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
        implements View.OnClickListener, RegisterItemInteraction {

    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    RegisterModel registerModel;
    Button btn_addMember,btn_register;
    AVLoadingIndicatorView avi;
    AdapterRegisterMemberDialog adapter;
    AdapterRegisterMemberEdit adapter_edited;
    ArrayList<RegisterMemberEditModel> editMembers;
    RecyclerView recyclerEditedMember;
    RelativeLayout rl_spn_shop;
    Spinner spn_shop;
    EditText edtDate;
    private PersianDatePickerDialog picker;
    String date="";

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
        setSpinner();
    }

    private void initView() {
        btn_addMember = findViewById(R.id.add_member);
        recyclerEditedMember = findViewById(R.id.recycler_edited_members);
        rl_spn_shop = findViewById(R.id.rl_spn_shop);
        spn_shop = findViewById(R.id.spn_shop);
        btn_register = findViewById(R.id.btn_register);
        avi=findViewById(R.id.avi_register);
        edtDate=findViewById(R.id.edtDate);
        btn_addMember.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        edtDate.setOnClickListener(this);


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

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.add_member:
                showAddMemberDialog();
                break;

            case R.id.btn_register:
                sendData();
                break;

            case R.id.edtDate:
                showCalander();
                break;
        }
    }

    private void showCalander() {
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtDate, InputMethodManager.SHOW_IMPLICIT);

//        PersianCalendar initDate = new PersianCalendar();
//        initDate.setPersianDate(1370, 3, 13);

        picker = new PersianDatePickerDialog(this)
                .setPositiveButtonString("تایید")
                .setNegativeButton("انصراف")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1397)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)

//                .setInitDate(initDate)
                .setActionTextColor(Color.GRAY)
//                .setTypeFace(typeface)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(ir.hamsaa.persiandatepicker.util.PersianCalendar persianCalendar) {

//                        date = persianCalendar.getPersianYear() + "/"
//                                + persianCalendar.getPersianMonth() + "/"
//                                + persianCalendar.getPersianDay();

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
        adapter = new AdapterRegisterMemberDialog(members, NewRegisterActivity.this);
        adapter.setListener(this);  // important or else the app will crashed
        recyclerview_members.setAdapter(adapter);

        // to select all members
        checkBoxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
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

        recyclerEditedMember.setLayoutManager(new GridLayoutManager(NewRegisterActivity.this,3));
        adapter_edited = new AdapterRegisterMemberEdit(editMembers, NewRegisterActivity.this);
        recyclerEditedMember.setAdapter(adapter_edited);

        if (editMembers.size() > 0) {
            btn_addMember.setText("ویرایش اعضای خانواده");
        } else {
            btn_addMember.setText("افزودن اعضای خانواده");
        }
    }


    private void sendData() {

        NewShopSendData newShopSendData=new NewShopSendData();
        newShopSendData.setMemberEditedList(editMembers);


        Service service = new ServiceProvider(this).getmService();
        Call<NewShop> call = service.registerNewShop(newShopSendData);
        call.enqueue(new Callback<NewShop>() {
            @Override
            public void onResponse(Call<NewShop> call, Response<NewShop> response) {

            }

            @Override
            public void onFailure(Call<NewShop> call, Throwable t) {

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
    }
}
