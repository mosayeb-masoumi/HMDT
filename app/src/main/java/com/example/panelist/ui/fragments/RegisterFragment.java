package com.example.panelist.ui.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.panelist.R;
import com.example.panelist.controllers.adapters.AdapterRegisterMemberDialog;
import com.example.panelist.controllers.adapters.AdapterRegisterMemberEdit;
import com.example.panelist.controllers.viewholders.RegisterItemInteraction;
import com.example.panelist.models.register.RegisterMemberEditModel;
import com.example.panelist.models.register.RegisterMemberModel;
import com.example.panelist.models.register.RegisterModel;
import com.example.panelist.utilities.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener,
        RegisterItemInteraction  {


    Disposable disposable = new CompositeDisposable();
    RegisterModel registerModel;
    Button btn_addMember;
    AdapterRegisterMemberDialog adapter;
    AdapterRegisterMemberEdit adapter_edited;
    ArrayList<RegisterMemberModel> members = new ArrayList<>();

    //    List<String> editMembers;
    ArrayList<RegisterMemberEditModel> editMembers;

    RecyclerView recyclerEditedMember;
    RelativeLayout rl_spn_shop;
    Spinner spn_shop;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable = RxBus.RegisterModel.subscribeRegisterModel(result -> {
            if (result instanceof RegisterModel) {
                registerModel = (RegisterModel) result;

            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initView(view);

        setSpinner();

//        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter = new AdapterRegisterMember(members,getContext());
//
//        adapter.setListener(this);  // important or else the app will crashed
//
//        recyclerview.setAdapter(adapter);
//
        sendData();


        return view;
    }



    private void sendData() {

        members.add(new RegisterMemberModel("reza"));
        members.add(new RegisterMemberModel("ali"));
        members.add(new RegisterMemberModel("milad"));
        members.add(new RegisterMemberModel("df"));
        members.add(new RegisterMemberModel("yu"));
        members.add(new RegisterMemberModel("ol"));
        members.add(new RegisterMemberModel("wrt"));
        members.add(new RegisterMemberModel("o;p"));
        members.add(new RegisterMemberModel("qa"));

    }



    private void initView(View view) {
        btn_addMember = view.findViewById(R.id.add_member);
        recyclerEditedMember = view.findViewById(R.id.recycler_edited_members);
        rl_spn_shop=view.findViewById(R.id.rl_spn_shop);
        spn_shop=view.findViewById(R.id.spn_shop);
//        chipGroup=view.findViewById(R.id.chip_group);
        btn_addMember.setOnClickListener(this);
    }


    private void setSpinner() {

        List<String> shopList = new ArrayList<>();
        for (int i = 0; i < registerModel.data.shops.size(); i++) {
//            shopList.add(App.provinceList.data.get(i).province);
            for (int j = 0; j < registerModel.data.shops.get(i).size(); j++) {
                shopList.add(registerModel.data.shops.get(i).get(j).title);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, shopList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spn_shop.setAdapter(adapter);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.add_member:
                showAddMemberDialog();
                break;
        }
    }

    private void showAddMemberDialog() {

        editMembers = new ArrayList<>();

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.inflate_register_members_dialog);
        dialog.setTitle("Title...");



//        List<String> members1 = new ArrayList<>();
//        for (int i = 0; i < registerModel.data.members.size(); i++) {
//            for (int j = 0; j < registerModel.data.members.get(i).size(); j++) {
//                members1.add(registerModel.data.shops.get(i).get(j).title);
//                int a = 5;
//            }
//        }



        RecyclerView recyclerview_members = dialog.findViewById(R.id.recyclerview_members);
        recyclerview_members.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterRegisterMemberDialog(members, getContext());

        adapter.setListener(this);  // important or else the app will crashed

        recyclerview_members.setAdapter(adapter);


        dialog.show();
    }


    // on click from recycler item view
    @Override
    public void onClicked(String name, Boolean chkbox) {
        if (chkbox) {
            editMembers.add(new RegisterMemberEditModel(name));
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

    public void updateEditMemberList(ArrayList<RegisterMemberEditModel> editMembers) {

        recyclerEditedMember.setLayoutManager(new GridLayoutManager(getContext(), 5));
        adapter_edited = new AdapterRegisterMemberEdit(editMembers, getContext());
        recyclerEditedMember.setAdapter(adapter_edited);


        if (editMembers.size() > 0) {
            btn_addMember.setText("ویرایش اعضای خانواده");
        } else {
            btn_addMember.setText("افزودن اعضای خانواده");
        }
    }



}
