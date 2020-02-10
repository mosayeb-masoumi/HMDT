package com.rahbarbazaar.shopper.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.adapters.RegisterMemberDialogAdapter;
import com.rahbarbazaar.shopper.controllers.adapters.RegisterMemberEditAdapter;
import com.rahbarbazaar.shopper.controllers.interfaces.BarcodeItemInteraction;
import com.rahbarbazaar.shopper.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.shopper.controllers.interfaces.SearchItemInteraction;
import com.rahbarbazaar.shopper.models.barcodlist.Barcode;
import com.rahbarbazaar.shopper.models.barcodlist.BarcodeData;
import com.rahbarbazaar.shopper.models.purchased_spinners.SpinnersModel;
import com.rahbarbazaar.shopper.models.register.Member;
import com.rahbarbazaar.shopper.models.register.RegisterMemberEditModel;
import com.rahbarbazaar.shopper.models.search_goods.GroupsData;
import com.rahbarbazaar.shopper.models.searchable.SearchModel;
import com.rahbarbazaar.shopper.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.utilities.ConvertEnDigitToFa;
import com.rahbarbazaar.shopper.utilities.ConvertorBitmapToString;
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.GeneralTools;
import com.rahbarbazaar.shopper.utilities.RxBus;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchasedItemActivityNew extends CustomBaseActivity implements View.OnClickListener,
        SearchItemInteraction, BarcodeItemInteraction, RegisterItemInteraction, IPickResult {


    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    RelativeLayout rl_home, rl_readable_barcode, rl_description_purchased, rl_spn_group,
            rl_spn_brand, rl_spn_type, rl_spn_amount, rl_root, rl_add_member, rl_photo_purchased, rl_register_barcode;


    LinearLayout rl_return, ll_texts, ll_spinners, ll_barcode, ll_questions;
    Integer position;
    String state;
    String str_spn_dialog_header;

    EditText edt_barcode;
    MemberPrize initMemberPrizeLists;

    String spn_name, str_spn_group_id, str_spn_brand_id, str_spn_type_id, str_spn_amount_id;
    String str_spn_group_title, str_spn_brand_title, str_spn_type_title, str_spn_amount_title;


    int barcodeListSize;

    GroupsData spinnerList;
    Barcode barcodeList;

    TextView txt_description_purchased, txt_group_purchase, txt_type_purchase, txt_brand_purchase, txt_img_count_purchased,
            txt_amount_purchase, txt_barcode, txt_spn_group_title, txt_spn_brand_title, txt_spn_type_title, txt_spn_amount_title;

    Button btn_confirmed, btn_no_confirmed;
    ImageView img_register_barcode;
    AVLoadingIndicatorView avi_register_barcode;

    ImageView img_arrow_spinner_group, img_arrow_spinner_brand, img_arrow_spinner_type, img_arrow_spinner_amount;

    List<SearchModel> searchList;

    int category_request = 0;
    RegisterMemberEditAdapter adapter_edited;
    RegisterMemberDialogAdapter adapter_member;
    ArrayList<RegisterMemberEditModel> editMembers;
    RecyclerView recyclerEditedMember;

    Context context;


    ImageView img1, img2, img3, img4, img_delete1, img_delete2, img_delete3, img_delete4;
    Bitmap bm1, bm2, bm3, bm4;
    String strBm1 = "";
    String strBm2 = "";
    String strBm3 = "";
    String strBm4 = "";
    int status = 0;

    int image_count1 = 0;
    int image_count2 = 0;
    int image_count3 = 0;
    int image_count4 = 0;


    SpinnersModel spinnersModel = new SpinnersModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_item_new);

        context = this;

        // to check internet connection
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(PurchasedItemActivityNew.this, findViewById(R.id.root_purchased_item));
            }
        };

        //get data from register fragment
//        barcode = new Barcode();
//        disposable = RxBus.BarcodeList.subscribeBarcodeList(result -> {
//            if (result instanceof Barcode) {
//                barcode = (Barcode) result;
//            }
//        });

        disposable = RxBus.MemberPrizeLists.subscribeMemberPrizeLists(result -> {
            if (result instanceof MemberPrize) {
                initMemberPrizeLists = (MemberPrize) result;
            }
        });

        initView();
        searchList = new ArrayList<>();

//        Intent intent = getIntent();
//        position = intent.getIntExtra("position", 0);
//        state = intent.getStringExtra("unreadable_barcode");
//
//
//        spinnerList = new GroupsData();
//        spinnerList = intent.getParcelableExtra("groupsData");
//        barcodeList = intent.getParcelableExtra("barcodeList");
//
//
//        if (state != null && state.equals("unreadable_barcode")) {
//            edt_barcode.setVisibility(View.VISIBLE);
//            rl_readable_barcode.setVisibility(View.GONE);
//
//            ll_spinners.setVisibility(View.GONE);
//            ll_texts.setVisibility(View.GONE);
//            rl_description_purchased.setVisibility(View.GONE);
//
//
//        } else {
//            edt_barcode.setVisibility(View.GONE);
//            rl_readable_barcode.setVisibility(View.VISIBLE);
//        }
//
//
//        // if wait , just show description
//        if (barcodeList != null) {
//            barcodeListSize = barcodeList.getData().size();
//            if (barcodeListSize == 1 && barcodeList.getData().get(0).getMygroup().equals("wait")) {
//                rl_description_purchased.setVisibility(View.VISIBLE);
//                ll_spinners.setVisibility(View.GONE);
//                ll_texts.setVisibility(View.GONE);
//                txt_description_purchased.setText(barcodeList.getData().get(0).getDecription());
//                txt_barcode.setText(barcodeList.getData().get(0).getBarcode());
//
//            }
//
//            if (barcodeListSize == 1 && barcodeList.getData().get(0).getMygroup().equals("product")) {
//                rl_description_purchased.setVisibility(View.GONE);
//                ll_spinners.setVisibility(View.GONE);
//                ll_texts.setVisibility(View.VISIBLE);
//
//                txt_group_purchase.setText(barcodeList.getData().get(0).getBarcodeDetail().get(0).getValue());
//                txt_type_purchase.setText(barcodeList.getData().get(0).getBarcodeDetail().get(1).getValue());
//                txt_brand_purchase.setText(barcodeList.getData().get(0).getBarcodeDetail().get(2).getValue());
//                txt_amount_purchase.setText(barcodeList.getData().get(0).getBarcodeDetail().get(3).getValue());
//                txt_barcode.setText(barcodeList.getData().get(0).getBarcode());
//
//            }
//
//
//            if (barcodeListSize > 1 && barcodeList.getData().get(position).getMygroup().equals("wait")) {
//                rl_description_purchased.setVisibility(View.VISIBLE);
//                ll_spinners.setVisibility(View.GONE);
//                ll_texts.setVisibility(View.GONE);
//                txt_description_purchased.setText(barcodeList.getData().get(position).getDecription());
//                txt_barcode.setText(barcodeList.getData().get(position).getBarcode());
//
//            }
//
//            if (barcodeListSize > 1 && barcodeList.getData().get(position).getMygroup().equals("product")) {
//                rl_description_purchased.setVisibility(View.GONE);
//                ll_spinners.setVisibility(View.GONE);
//                ll_texts.setVisibility(View.VISIBLE);
//
//                txt_group_purchase.setText(barcodeList.getData().get(position).getBarcodeDetail().get(0).getValue());
//                txt_type_purchase.setText(barcodeList.getData().get(position).getBarcodeDetail().get(1).getValue());
//                txt_brand_purchase.setText(barcodeList.getData().get(position).getBarcodeDetail().get(2).getValue());
//                txt_amount_purchase.setText(barcodeList.getData().get(position).getBarcodeDetail().get(3).getValue());
//                txt_barcode.setText(barcodeList.getData().get(position).getBarcode());
//
//            }
//        }
//
//
//        if (spinnerList != null) {
//            rl_description_purchased.setVisibility(View.GONE);
//            ll_texts.setVisibility(View.GONE);
//            ll_spinners.setVisibility(View.VISIBLE);
//
//        }


        Barcode barcodeList_unreadable = new Barcode();
        GroupsData spinnerList_unreadable = new GroupsData();
        detectStatus(barcodeList_unreadable, spinnerList_unreadable);

        ll_questions.setVisibility(View.GONE);

        img_arrow_spinner_brand.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
        img_arrow_spinner_type.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
        img_arrow_spinner_amount.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));


    }


    private void detectStatus(Barcode barcodeList_unreadable, GroupsData spinnerList_unreadable) {
        Intent intent = getIntent();

        state = intent.getStringExtra("unreadable_barcode");


//        spinnerList = new GroupsData();
//        barcodeList = new Barcode();

        if (barcodeList_unreadable.getData() == null) {
            barcodeList = intent.getParcelableExtra("barcodeList");
            position = intent.getIntExtra("position", 10000);
        } else {
            barcodeList = barcodeList_unreadable;
        }

        if (spinnerList_unreadable.getData() == null) {
            spinnerList = intent.getParcelableExtra("groupsData");
        } else {
            spinnerList = spinnerList_unreadable;
        }


        if (state != null && state.equals("unreadable_barcode")) {
            edt_barcode.setVisibility(View.VISIBLE);
            rl_register_barcode.setVisibility(View.VISIBLE);
            rl_readable_barcode.setVisibility(View.GONE);

            ll_spinners.setVisibility(View.GONE);
            ll_texts.setVisibility(View.GONE);
            rl_description_purchased.setVisibility(View.GONE);


        } else {
            edt_barcode.setVisibility(View.GONE);
            rl_readable_barcode.setVisibility(View.VISIBLE);
            rl_register_barcode.setVisibility(View.GONE);
        }


        // if wait , just show description
        if (this.barcodeList != null) {
            barcodeListSize = this.barcodeList.getData().size();
            if (barcodeListSize == 1 && this.barcodeList.getData().get(0).getMygroup().equals("wait")) {
                rl_description_purchased.setVisibility(View.VISIBLE);
                ll_spinners.setVisibility(View.GONE);
                ll_texts.setVisibility(View.GONE);
                txt_description_purchased.setText(this.barcodeList.getData().get(0).getDecription());
                txt_barcode.setText(this.barcodeList.getData().get(0).getBarcode());

            }

            if (barcodeListSize == 1 && this.barcodeList.getData().get(0).getMygroup().equals("product")) {
                rl_description_purchased.setVisibility(View.GONE);
                ll_spinners.setVisibility(View.GONE);
                ll_texts.setVisibility(View.VISIBLE);

                txt_group_purchase.setText(this.barcodeList.getData().get(0).getBarcodeDetail().get(0).getValue());
                txt_type_purchase.setText(this.barcodeList.getData().get(0).getBarcodeDetail().get(1).getValue());
                txt_brand_purchase.setText(this.barcodeList.getData().get(0).getBarcodeDetail().get(2).getValue());
                txt_amount_purchase.setText(this.barcodeList.getData().get(0).getBarcodeDetail().get(3).getValue());
                txt_barcode.setText(this.barcodeList.getData().get(0).getBarcode());

            }


            if (barcodeListSize > 1 && this.barcodeList.getData().get(position).getMygroup().equals("wait")) {
                rl_description_purchased.setVisibility(View.VISIBLE);
                ll_spinners.setVisibility(View.GONE);
                ll_texts.setVisibility(View.GONE);
                txt_description_purchased.setText(this.barcodeList.getData().get(position).getDecription());
                txt_barcode.setText(this.barcodeList.getData().get(position).getBarcode());

            }

            if (barcodeListSize > 1 && this.barcodeList.getData().get(position).getMygroup().equals("product")) {
                rl_description_purchased.setVisibility(View.GONE);
                ll_spinners.setVisibility(View.GONE);
                ll_texts.setVisibility(View.VISIBLE);

                txt_group_purchase.setText(this.barcodeList.getData().get(position).getBarcodeDetail().get(0).getValue());
                txt_type_purchase.setText(this.barcodeList.getData().get(position).getBarcodeDetail().get(1).getValue());
                txt_brand_purchase.setText(this.barcodeList.getData().get(position).getBarcodeDetail().get(2).getValue());
                txt_amount_purchase.setText(this.barcodeList.getData().get(position).getBarcodeDetail().get(3).getValue());
                txt_barcode.setText(this.barcodeList.getData().get(position).getBarcode());

            }
        }


        if (spinnerList != null) {
            rl_description_purchased.setVisibility(View.GONE);
            ll_texts.setVisibility(View.GONE);
            ll_spinners.setVisibility(View.VISIBLE);
            rl_spn_brand.setClickable(false);
            rl_spn_type.setClickable(false);
            rl_spn_amount.setClickable(false);
            rl_spn_group.setClickable(true);
            rl_spn_group.setBackground(getResources().getDrawable(R.drawable.bg_prize_item));
            img_arrow_spinner_group.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
            txt_spn_group_title.setTextColor(getResources().getColor(R.color.blue_dark));
            txt_spn_group_title.setText("گروه");

        }
    }


    private void initView() {

        rl_home = findViewById(R.id.rl_home_Purchased_items);
        rl_return = findViewById(R.id.linear_return_qrcode);
        edt_barcode = findViewById(R.id.edt_barcode);
        rl_readable_barcode = findViewById(R.id.rl_readable_barcode);
        ll_texts = findViewById(R.id.ll_texts);
        ll_spinners = findViewById(R.id.ll_spinners);
        rl_description_purchased = findViewById(R.id.rl_description_purchased);
        txt_description_purchased = findViewById(R.id.txt_description_purchased);
        txt_barcode = findViewById(R.id.txt_barcode);
        ll_barcode = findViewById(R.id.ll_barcode);
        rl_root = findViewById(R.id.root_purchased_item);
        rl_photo_purchased = findViewById(R.id.rl_photo_purchased);
        txt_img_count_purchased = findViewById(R.id.txt_img_count_purchased);

        ll_questions = findViewById(R.id.ll_questions);
        img_register_barcode = findViewById(R.id.img_register_barcode);


        btn_confirmed = findViewById(R.id.btn_confirmed);
        btn_no_confirmed = findViewById(R.id.btn_no_confirmed);
        avi_register_barcode = findViewById(R.id.avi_register_barcode);


        txt_spn_group_title = findViewById(R.id.txt_group_spn_title);
        txt_spn_brand_title = findViewById(R.id.txt_brand_spn_title);
        txt_spn_type_title = findViewById(R.id.txt_type_spn_title);
        txt_spn_amount_title = findViewById(R.id.txt_amount_spn_title);


        txt_group_purchase = findViewById(R.id.txt_group_purchase);
        txt_type_purchase = findViewById(R.id.txt_type_purchase);
        txt_brand_purchase = findViewById(R.id.txt_brand_purchase);
        txt_amount_purchase = findViewById(R.id.txt_amount_purchase);

        rl_spn_group = findViewById(R.id.rl_spn_group);
        rl_spn_brand = findViewById(R.id.rl_spn_brand);
        rl_spn_type = findViewById(R.id.rl_spn_type);
        rl_spn_amount = findViewById(R.id.rl_spn_amount);
        rl_add_member = findViewById(R.id.rl_add_member);

        rl_register_barcode = findViewById(R.id.rl_register_barcode);


        img_arrow_spinner_group = findViewById(R.id.img_arrow_spinner_group);
        img_arrow_spinner_brand = findViewById(R.id.img_arrow_spinner_brand);
        img_arrow_spinner_type = findViewById(R.id.img_arrow_spinner_type);
        img_arrow_spinner_amount = findViewById(R.id.img_arrow_spinner_amount);

        recyclerEditedMember = findViewById(R.id.recycler_edited_members);

        rl_home.setOnClickListener(this);
        rl_return.setOnClickListener(this);
        rl_spn_group.setOnClickListener(this);
        rl_spn_brand.setOnClickListener(this);
        rl_spn_type.setOnClickListener(this);
        rl_spn_amount.setOnClickListener(this);
        btn_confirmed.setOnClickListener(this);
        btn_no_confirmed.setOnClickListener(this);
        img_register_barcode.setOnClickListener(this);
        rl_add_member.setOnClickListener(this);
        rl_photo_purchased.setOnClickListener(this);


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

                str_spn_dialog_header = "گروه";

                if (category_request == 0) {
                    for (int i = 0; i < spinnerList.getData().size(); i++) {
                        searchList.add(new SearchModel(spinnerList.getData().get(i).getTitle(), spinnerList.getData().get(i).getId()));
                    }

                    showSpnListDialog(spn_name, searchList, str_spn_dialog_header);
                    category_request++;
                } else if (category_request > 0) {

                    getCategoryList();
                }


                rl_spn_brand.setClickable(false);
                rl_spn_type.setClickable(false);
                rl_spn_amount.setClickable(false);

                rl_spn_brand.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
                rl_spn_type.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
                rl_spn_amount.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));

                img_arrow_spinner_brand.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
                img_arrow_spinner_type.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
                img_arrow_spinner_amount.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));

                txt_spn_brand_title.setText("برند");
                txt_spn_type_title.setText("");
                txt_spn_amount_title.setText("");

                txt_spn_brand_title.setTextColor(getResources().getColor(R.color.grey));
                txt_spn_type_title.setTextColor(getResources().getColor(R.color.grey));
                txt_spn_amount_title.setTextColor(getResources().getColor(R.color.grey));


                break;

            case R.id.rl_spn_brand:
                spn_name = "spn_brand";

                str_spn_dialog_header = "برند";
                searchList = new ArrayList<>();

                for (int i = 0; i < spinnersModel.data.brand.size(); i++) {
                    searchList.add(new SearchModel(spinnersModel.data.brand.get(i).title,
                            spinnersModel.data.brand.get(i).id));
                }

                showSpnListDialog(spn_name, searchList, str_spn_dialog_header);


                break;
            case R.id.rl_spn_type:
                spn_name = "spn_type";
                searchList = new ArrayList<>();

                for (int i = 0; i < spinnersModel.data.oneData.size(); i++) {
                    searchList.add(new SearchModel(spinnersModel.data.oneData.get(i).title,
                            spinnersModel.data.oneData.get(i).id));
                }

                str_spn_dialog_header = spinnersModel.data.oneTitle;
                showSpnListDialog(spn_name, searchList, str_spn_dialog_header);

                break;
            case R.id.rl_spn_amount:
                spn_name = "spn_amount";
                searchList = new ArrayList<>();

                for (int i = 0; i < spinnersModel.data.twoData.size(); i++) {
                    searchList.add(new SearchModel(spinnersModel.data.twoData.get(i).title,
                            spinnersModel.data.twoData.get(i).id));
                }

                str_spn_dialog_header = spinnersModel.data.twoTitle;
                showSpnListDialog(spn_name, searchList, str_spn_dialog_header);

                break;


            case R.id.btn_confirmed:
                ll_questions.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_no_confirmed:
                ll_questions.setVisibility(View.GONE);
                break;

            case R.id.img_register_barcode:
                getListOfProducts();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                break;

            case R.id.rl_add_member:
                showAddMemberDialog();
                break;

            case R.id.rl_photo_purchased:
                if (cameraPermissionGranted()) {
                    showPhotoDialog();
                } else {
                    int request_code = 7;
                    askCameraPermission(request_code);
                }
                break;


        }
    }

    private void showPhotoDialog() {

        final Dialog dialog = new Dialog(PurchasedItemActivityNew.this);
        dialog.setContentView(R.layout.photo_dialog_purchase);

        RelativeLayout rl_camera1 = dialog.findViewById(R.id.rl_camera1);
        RelativeLayout rl_camera3 = dialog.findViewById(R.id.rl_camera3);

        Button btn_close = dialog.findViewById(R.id.btn_close_photo_dialog);

        ImageView img_close = dialog.findViewById(R.id.img_close);

        img_delete1 = dialog.findViewById(R.id.img_delete1);

        img_delete3 = dialog.findViewById(R.id.img_delete3);


        img1 = dialog.findViewById(R.id.img1);
        img2 = dialog.findViewById(R.id.img2);
        img3 = dialog.findViewById(R.id.img3);
        img4 = dialog.findViewById(R.id.img4);


        if (bm1 != null) {
            img1.setImageBitmap(bm1);
            img_delete1.setVisibility(View.VISIBLE);
            image_count1 = 1;

        }


        if (bm3 != null) {
            img3.setImageBitmap(bm3);
            img_delete3.setVisibility(View.VISIBLE);
            image_count3 = 1;
        }


        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        rl_camera1.setOnClickListener(v -> {
            status = 1;
            choose_pic();
        });

        rl_camera3.setOnClickListener(v -> {
            status = 3;
            choose_pic();
        });


        img_delete1.setOnClickListener(v -> {
//            strBm1 = "";
            strBm1 = "deleted";
            bm1 = null;
            img1.setImageDrawable(null);
            img_delete1.setVisibility(View.GONE);

            image_count1 = 0;


        });


        img_delete3.setOnClickListener(v -> {
//            strBm3 = "";
            strBm3 = "deleted";
            bm3 = null;
            img3.setImageDrawable(null);
            img_delete3.setVisibility(View.GONE);
            image_count3 = 0;


        });


        img_close.setOnClickListener(v -> {
            dialog.dismiss();

            int total_img_count = image_count1 + image_count2 + image_count3 + image_count4;

            String img_count = ConvertEnDigitToFa.convert(String.valueOf(total_img_count));

            if (total_img_count > 0) {
                txt_img_count_purchased.setVisibility(View.VISIBLE);
                txt_img_count_purchased.setText(String.format("+%s", img_count));
            } else {
                txt_img_count_purchased.setVisibility(View.GONE);
            }

        });


        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
            int total_img_count = image_count1 + image_count2 + image_count3 + image_count4;

            String img_count = ConvertEnDigitToFa.convert(String.valueOf(total_img_count));

            if (total_img_count > 0) {
                txt_img_count_purchased.setVisibility(View.VISIBLE);
                txt_img_count_purchased.setText(String.format("+%s", img_count));
            } else {
                txt_img_count_purchased.setVisibility(View.GONE);
            }

        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    private void choose_pic() {

        PickSetup setup = new PickSetup()
                .setTitle("settitle")
                .setProgressText("progress text")
                .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)
                .setSystemDialog(true);
        PickImageDialog.build(setup).show(this);
    }

    @Override
    public void onPickResult(PickResult r) {

        if (r.getError() == null) {
            if (status == 1) {
                img1.setImageBitmap(r.getBitmap());
                bm1 = r.getBitmap();
                strBm1 = ConvertorBitmapToString.bitmapToString(bm1);
                img_delete1.setVisibility(View.VISIBLE);
                image_count1 = 1;
            } else if (status == 3) {
                img3.setImageBitmap(r.getBitmap());
                bm3 = r.getBitmap();
                strBm3 = ConvertorBitmapToString.bitmapToString(bm3);
                img_delete3.setVisibility(View.VISIBLE);
                image_count3 = 1;
            }

        }

    }


    private boolean cameraPermissionGranted() {

        return ContextCompat.checkSelfPermission(PurchasedItemActivityNew.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askCameraPermission(int request_code) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, request_code);
    }


    private void showAddMemberDialog() {

        editMembers = new ArrayList<>();
        final Dialog dialog = new Dialog(PurchasedItemActivityNew.this);
        dialog.setContentView(R.layout.register_members_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // to show list of member items
        List<Member> members = new ArrayList<>();

//        if (registerModel.data != null) {
        for (int i = 0; i < initMemberPrizeLists.data.member.size(); i++) {
            for (int j = 0; j < initMemberPrizeLists.data.member.get(i).size(); j++) {
                members.add(new Member(initMemberPrizeLists.data.member.get(i).get(j).name
                        , initMemberPrizeLists.data.member.get(i).get(j).id));
            }
//            }
//        } else if (shoppingEditModel.data != null) {
//            for (int i = 0; i < shoppingEditModel.data.member.size(); i++) {
//                for (int j = 0; j < shoppingEditModel.data.member.get(i).size(); j++) {
//                    members.add(new Member(shoppingEditModel.data.member.get(i).get(j).name
//                            , shoppingEditModel.data.member.get(i).get(j).id));
//                }
//            }
        }

        CheckBox checkBoxAll = dialog.findViewById(R.id.checkbox_all);
        RecyclerView recyclerview_members = dialog.findViewById(R.id.recyclerview_members);
        Button btn_exit_dialog = dialog.findViewById(R.id.btn_exit_dialog);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        recyclerview_members.setLayoutManager(new LinearLayoutManager(PurchasedItemActivityNew.this));
        adapter_member = new RegisterMemberDialogAdapter(members, PurchasedItemActivityNew.this);
        adapter_member.setListener(this);  // important or else the app will crashed
        recyclerview_members.setAdapter(adapter_member);

        // to select all members
        checkBoxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editMembers = new ArrayList<>();

//                if (registerModel.data != null) {
//                    for (int i = 0; i < registerModel.data.member.size(); i++) {
//                        for (int j = 0; j < registerModel.data.member.get(i).size(); j++) {
//                            editMembers.add(new RegisterMemberEditModel(registerModel.data.member.get(i).get(j).name,
//                                    registerModel.data.member.get(i).get(j).id));
//                        }
//                    }
//                } else if (shoppingEditModel.data != null) {
//                    for (int i = 0; i < shoppingEditModel.data.member.size(); i++) {
//                        for (int j = 0; j < shoppingEditModel.data.member.get(i).size(); j++) {
//                            editMembers.add(new RegisterMemberEditModel(shoppingEditModel.data.member.get(i).get(j).name,
//                                    shoppingEditModel.data.member.get(i).get(j).id));
//                        }
//                    }
//                }

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

    // initializing edited member list
    public void updateEditMemberList(ArrayList<RegisterMemberEditModel> editMembers) {

        recyclerEditedMember.setLayoutManager(new GridLayoutManager(PurchasedItemActivityNew.this, 2));
        adapter_edited = new RegisterMemberEditAdapter(editMembers, PurchasedItemActivityNew.this);
        recyclerEditedMember.setAdapter(adapter_edited);
    }


    private void getListOfProducts() {

        img_register_barcode.setVisibility(View.GONE);
        avi_register_barcode.setVisibility(View.VISIBLE);

        String barcode = edt_barcode.getText().toString();
        Service service = new ServiceProvider(this).getmService();
        Call<Barcode> call = service.getBarcodeList(barcode);
        call.enqueue(new Callback<Barcode>() {
            @Override
            public void onResponse(Call<Barcode> call, Response<Barcode> response) {

                if (response.code() == 200) {
                    img_register_barcode.setVisibility(View.VISIBLE);
                    avi_register_barcode.setVisibility(View.GONE);

                    Barcode barcodeList_unreadable = new Barcode();
                    barcodeList_unreadable = response.body();

                    if (barcodeList_unreadable.getData().size() == 1) {


                        GroupsData spinnerList_unreadable = new GroupsData();
                        detectStatus(barcodeList_unreadable, spinnerList_unreadable);


                    } else if (barcodeList_unreadable.getData().size() > 1) {

                        Barcode barcode = response.body();
                        showBarcodeListDialog(barcode);
                    }


                } else if (response.code() == 204) {

                    getSpinneList();

                } else if (response.code() == 406) {
                    Toast.makeText(PurchasedItemActivityNew.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PurchasedItemActivityNew.this, MainActivity.class));
                    img_register_barcode.setVisibility(View.VISIBLE);
                    avi_register_barcode.setVisibility(View.GONE);


                } else {

                    Toast.makeText(PurchasedItemActivityNew.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    img_register_barcode.setVisibility(View.VISIBLE);
                    avi_register_barcode.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<Barcode> call, Throwable t) {

                Toast.makeText(PurchasedItemActivityNew.this,
                        "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                img_register_barcode.setVisibility(View.VISIBLE);
                avi_register_barcode.setVisibility(View.GONE);
            }
        });

    }

    private void showBarcodeListDialog(Barcode barcode) {

        DialogFactory dialogFactory = new DialogFactory(PurchasedItemActivityNew.this);
        dialogFactory.createBarcodeResultListUnreadableDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {
                onResume();
            }
        }, rl_root, barcode, this);
    }


    private void getSpinneList() {
        Service service = new ServiceProvider(this).getmService();
        Call<GroupsData> call = service.getCategorySpnData();
        call.enqueue(new Callback<GroupsData>() {
            @Override
            public void onResponse(Call<GroupsData> call, Response<GroupsData> response) {
                if (response.code() == 200) {
                    img_register_barcode.setVisibility(View.VISIBLE);
                    avi_register_barcode.setVisibility(View.GONE);

                    GroupsData spinnerList_unreadable = new GroupsData();
                    spinnerList_unreadable = response.body();

//                    ll_spinners.setVisibility(View.VISIBLE);
//                    spinnerList = groupsData;
//                    barcodeList=null;
                    Barcode barcodeList_unreadable = new Barcode();

                    detectStatus(barcodeList_unreadable, spinnerList_unreadable);


                } else {
                    img_register_barcode.setVisibility(View.VISIBLE);
                    avi_register_barcode.setVisibility(View.GONE);
                    Toast.makeText(PurchasedItemActivityNew.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupsData> call, Throwable t) {
                img_register_barcode.setVisibility(View.VISIBLE);
                avi_register_barcode.setVisibility(View.GONE);
                Toast.makeText(PurchasedItemActivityNew.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showSpnListDialog(String spn_name, List<SearchModel> searchList, String str_spn_dialog_header) {
//        List<SearchModel> searchList = new ArrayList<>();
//
//        if(spn_name.equals("spn_group")){
//            for (int i = 0; i < spinnerList.getData().size(); i++) {
//                searchList.add(new SearchModel(spinnerList.getData().get(i).getTitle(), spinnerList.getData().get(i).getId()));
//            }
//        }


        DialogFactory dialogFactory = new DialogFactory(PurchasedItemActivityNew.this);
        dialogFactory.createSpnListDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

                String spn_nam = params[0];
                String dialog_title = params[1];

                showEtcDialog(spn_nam,dialog_title);

            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_root, searchList, this, spn_name, str_spn_dialog_header);

    }

    private void showEtcDialog(String spn_nam, String dialog_title) {



        DialogFactory dialogFactory = new DialogFactory(PurchasedItemActivityNew.this);
        dialogFactory.createEtcSpnListDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

                String description = params[0];

                if(spn_nam.equals("spn_brand")){
                    txt_spn_brand_title.setText(description);
                }else if(spn_nam.equals("spn_type")){
                    txt_spn_type_title.setText(description);
                }else if(spn_nam.equals("spn_group")){
                    txt_spn_amount_title.setText(description);
                }


            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_root, this,spn_nam,dialog_title);
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
//        disposable.dispose(); //very important  to avoid memory leak

    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose();
    }

    @Override
    public void searchListItemOnClick(SearchModel model, AlertDialog dialog, String spn_name) {

        if (spn_name.equals("spn_group")) {

            str_spn_group_title = model.getTitle();
            str_spn_group_id = model.getId();
            txt_spn_group_title.setText(model.getTitle());

            getSpinnerLists(str_spn_group_id);
            dialog.dismiss();

        } else if (spn_name.equals("spn_brand")) {

            str_spn_brand_title = model.getTitle();
            str_spn_brand_id = model.getId();
            txt_spn_brand_title.setText(model.getTitle());

            dialog.dismiss();

        } else if (spn_name.equals("spn_type")) {
            str_spn_type_title = model.getTitle();
            str_spn_type_id = model.getId();
            txt_spn_type_title.setText(model.getTitle());

            dialog.dismiss();

        } else if (spn_name.equals("spn_amount")) {
            str_spn_amount_title = model.getTitle();
            str_spn_amount_id = model.getId();
            txt_spn_amount_title.setText(model.getTitle());

            dialog.dismiss();
        }

    }

    private void getCategoryList() {

        avi_register_barcode.setVisibility(View.VISIBLE);
        img_register_barcode.setVisibility(View.GONE);

        Service service = new ServiceProvider(this).getmService();
        Call<GroupsData> call = service.getCategorySpnData();
        call.enqueue(new Callback<GroupsData>() {
            @Override
            public void onResponse(Call<GroupsData> call, Response<GroupsData> response) {
                if (response.code() == 200) {

                    searchList = new ArrayList<>();

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        searchList.add(new SearchModel(response.body().getData().get(i).getTitle(),
                                response.body().getData().get(i).getId()));
                    }

                    str_spn_dialog_header = "گروه";
                    showSpnListDialog("spn_group", searchList, str_spn_dialog_header);

                    rl_spn_brand.setClickable(false);
                    rl_spn_type.setClickable(false);
                    rl_spn_amount.setClickable(false);

                    avi_register_barcode.setVisibility(View.GONE);
                    img_register_barcode.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(PurchasedItemActivityNew.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    avi_register_barcode.setVisibility(View.GONE);
                    img_register_barcode.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GroupsData> call, Throwable t) {
                Toast.makeText(PurchasedItemActivityNew.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                avi_register_barcode.setVisibility(View.GONE);
                img_register_barcode.setVisibility(View.VISIBLE);
            }
        });

    }

    private void getSpinnerLists(String str_spn_group_id) {

        avi_register_barcode.setVisibility(View.VISIBLE);
        img_register_barcode.setVisibility(View.GONE);


        Service service = new ServiceProvider(this).getmService();
        Call<SpinnersModel> call = service.getSpinnersData(str_spn_group_id);
        call.enqueue(new Callback<SpinnersModel>() {
            @Override
            public void onResponse(Call<SpinnersModel> call, Response<SpinnersModel> response) {

                if (response.code() == 200) {

                    avi_register_barcode.setVisibility(View.GONE);
                    img_register_barcode.setVisibility(View.VISIBLE);


                    spinnersModel = response.body();

//                    searchList = new ArrayList<>();
////
////                    for (int i = 0; i < response.body().getData().size(); i++) {
////                        searchList.add(new SearchModel(response.body().getData().get(i).getTitle(),
////                                response.body().getData().get(i).getId()));
////                    }

                    rl_spn_group.setClickable(true);
                    rl_spn_brand.setClickable(true);
                    rl_spn_type.setClickable(true);
                    rl_spn_amount.setClickable(true);

                    rl_spn_group.setBackground(getResources().getDrawable(R.drawable.bg_prize_item));
                    img_arrow_spinner_group.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
                    txt_spn_group_title.setTextColor(getResources().getColor(R.color.blue_dark));


                    rl_spn_brand.setBackground(getResources().getDrawable(R.drawable.bg_prize_item));
                    img_arrow_spinner_brand.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
                    txt_spn_brand_title.setTextColor(getResources().getColor(R.color.blue_dark));


                    if (spinnersModel.data.oneData.size() == 0) {
                        rl_spn_type.setClickable(false);
                        rl_spn_type.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
                        img_arrow_spinner_type.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
//                        txt_spn_type_title.setTextColor(getResources().getColor(R.color.blue_dark));
//                        txt_spn_type_title.setText(spinnersModel.data.oneTitle);
                    } else {
                        rl_spn_type.setBackground(getResources().getDrawable(R.drawable.bg_prize_item));
                        img_arrow_spinner_type.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
                        txt_spn_type_title.setTextColor(getResources().getColor(R.color.blue_dark));
                        txt_spn_type_title.setText(spinnersModel.data.oneTitle);
                    }


                    if (spinnersModel.data.twoData.size() == 0) {
                        rl_spn_amount.setClickable(false);
                        rl_spn_amount.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
                        img_arrow_spinner_amount.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
//                        txt_spn_amount_title.setTextColor(getResources().getColor(R.color.blue_dark));
//                        txt_spn_amount_title.setText(spinnersModel.data.twoTitle);

                    } else {
                        rl_spn_amount.setBackground(getResources().getDrawable(R.drawable.bg_prize_item));
                        img_arrow_spinner_amount.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
                        txt_spn_amount_title.setTextColor(getResources().getColor(R.color.blue_dark));
                        txt_spn_amount_title.setText(spinnersModel.data.twoTitle);
                    }


                } else {
                    Toast.makeText(PurchasedItemActivityNew.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    avi_register_barcode.setVisibility(View.GONE);
                    img_register_barcode.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<SpinnersModel> call, Throwable t) {
                avi_register_barcode.setVisibility(View.GONE);
                img_register_barcode.setVisibility(View.VISIBLE);
                Toast.makeText(PurchasedItemActivityNew.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void barcodeListOnClicked(@NotNull BarcodeData model, int position1, @NotNull Barcode barcode, @NotNull AlertDialog dialog) {


        position = position1;

        Barcode barcodeList_unreadable = new Barcode();
        GroupsData spinnerList_unreadable = new GroupsData();
        barcodeList_unreadable = barcode;
        detectStatus(barcodeList_unreadable, spinnerList_unreadable);

        dialog.dismiss();


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


}
