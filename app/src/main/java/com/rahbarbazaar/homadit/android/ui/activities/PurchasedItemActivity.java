package com.rahbarbazaar.homadit.android.ui.activities;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.RegisterMemberDialogAdapter;
import com.rahbarbazaar.homadit.android.controllers.adapters.RegisterMemberEditAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.BarcodeItemInteraction;
import com.rahbarbazaar.homadit.android.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.homadit.android.controllers.interfaces.SearchItemInteraction;
import com.rahbarbazaar.homadit.android.models.api_error.APIError422;
import com.rahbarbazaar.homadit.android.models.api_error.ErrorUtils;
import com.rahbarbazaar.homadit.android.models.barcodlist.Barcode;
import com.rahbarbazaar.homadit.android.models.barcodlist.BarcodeData;
import com.rahbarbazaar.homadit.android.models.purchased_item.PurchaseItemNewProductResult;
import com.rahbarbazaar.homadit.android.models.purchased_item.PurchaseItemResult;
import com.rahbarbazaar.homadit.android.models.purchased_item.SendPurchasedItemData;
import com.rahbarbazaar.homadit.android.models.purchased_item.SendPurchasedItemNewData;
import com.rahbarbazaar.homadit.android.models.purchased_spinners.SpinnersModel;
import com.rahbarbazaar.homadit.android.models.register.Member;
import com.rahbarbazaar.homadit.android.models.register.RegisterMemberEditModel;
import com.rahbarbazaar.homadit.android.models.search_goods.GroupsData;
import com.rahbarbazaar.homadit.android.models.searchable.SearchModel;
import com.rahbarbazaar.homadit.android.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.homadit.android.network.Service;
import com.rahbarbazaar.homadit.android.network.ServiceProvider;
import com.rahbarbazaar.homadit.android.utilities.Cache;
import com.rahbarbazaar.homadit.android.utilities.ConvertEnDigitToFa;
import com.rahbarbazaar.homadit.android.utilities.ConvertorBitmapToString;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.rahbarbazaar.homadit.android.utilities.DialogFactory;
import com.rahbarbazaar.homadit.android.utilities.GeneralTools;
import com.rahbarbazaar.homadit.android.utilities.RxBus;
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

public class PurchasedItemActivity extends CustomBaseActivity implements View.OnClickListener,
        SearchItemInteraction, BarcodeItemInteraction, RegisterItemInteraction, IPickResult, CompoundButton.OnCheckedChangeListener {

    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    RelativeLayout rl_home, rl_readable_barcode, rl_description_purchased, rl_spn_group, rl_info_img_new_register,
            rl_spn_brand, rl_spn_type, rl_spn_amount, rl_root, rl_add_member, rl_photo_purchased, rl_register_barcode,
            rl_photo_purchase_total, rl_info_member_new_register, rl_edt_description_purchased, rl_register_result,root_purchased_item;

    LinearLayout rl_return, ll_texts, ll_spinners, ll_barcode, ll_questions, ll_chkboxes;
    Integer position;
    String state;
    String str_spn_dialog_header;

    EditText edt_barcode, edt_description_purchased;
    MemberPrize initMemberPrizeLists;

    String spn_name, str_spn_group_id = "", str_spn_brand_id = "", str_spn_type_id = "", str_spn_amount_id = "";
    String str_spn_group_title = "", str_spn_brand_title = "", str_spn_type_title = "", str_spn_amount_title = "";
    String product_id, shopping_id, type;
    String sendData_type = "";
    String barcode_type = "";
    String str_one_title = "", str_two_title = "";

    int barcodeListSize;

    GroupsData spinnerList;
    Barcode barcodeList;

    TextView txt_description_purchased, txt_group_purchase, txt_type_purchase, txt_brand_purchase, txt_img_count_purchased, txt_unit,
            txt_amount_purchase, txt_barcode, txt_spn_group_title, txt_title_purchased_item, txt_spn_brand_title, txt_spn_type_title, txt_spn_amount_title;

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
    EditText edt_cost_purchase, edt_amount_purchased;

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
    CheckBox chk_confirmed, chk_no_confirmed;
    Button btn_register_purchased;
    AVLoadingIndicatorView avi_register_purchased;
    StringBuilder builderMember, builderCost, buliderAmount,
            buliderDescription, buliderBarcode,buliderCategory, buliderBrand;


    int max_amount=0,min_price=0,max_price=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_item);

        context = this;

        // to check internet connection
        tools = GeneralTools.getInstance();
        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                tools.doCheckNetwork(PurchasedItemActivity.this, findViewById(R.id.root_purchased_item));
            }
        };


        disposable = RxBus.MemberPrizeLists.subscribeMemberPrizeLists(result -> {
            if (result instanceof MemberPrize) {
                initMemberPrizeLists = (MemberPrize) result;
            }
        });

        initView();
        searchList = new ArrayList<>();

        ll_questions.setVisibility(View.GONE);
        rl_photo_purchase_total.setVisibility(View.GONE);
        rl_edt_description_purchased.setVisibility(View.GONE);

        Barcode barcodeList_unreadable = new Barcode();
        GroupsData spinnerList_unreadable = new GroupsData();
        detectStatus(barcodeList_unreadable, spinnerList_unreadable);


        img_arrow_spinner_brand.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
        img_arrow_spinner_type.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
        img_arrow_spinner_amount.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
    }

    private void detectStatus(Barcode barcodeList_unreadable, GroupsData spinnerList_unreadable) {
        Intent intent = getIntent();
        state = intent.getStringExtra("unreadable_barcode");

        if (barcodeList_unreadable.getData() == null) {
            barcodeList = intent.getParcelableExtra("barcodeList");
            position = intent.getIntExtra("position", 10000);
        } else {
            barcodeList = barcodeList_unreadable;
        }

        if (spinnerList_unreadable.getData() == null) {
            spinnerList = intent.getParcelableExtra("groupsData");
            //text barcode
            barcode_type = "no_match_barcode";
        } else {
            spinnerList = spinnerList_unreadable;
            barcode_type = "new_barcode";
            //edittext barcode
        }


        if (state != null && state.equals("unreadable_barcode")) {
            edt_barcode.setVisibility(View.VISIBLE);
            rl_register_barcode.setVisibility(View.VISIBLE);
            rl_readable_barcode.setVisibility(View.GONE);
            ll_spinners.setVisibility(View.GONE);
            ll_texts.setVisibility(View.GONE);
            rl_description_purchased.setVisibility(View.GONE);
            ll_chkboxes.setVisibility(View.GONE);
            rl_register_result.setVisibility(View.VISIBLE);


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
                txt_unit.setText(this.barcodeList.getData().get(0).getUnit());

                min_price = this.barcodeList.getData().get(0).getMinPrice();
                max_price = this.barcodeList.getData().get(0).getMaxPrice();
                max_amount = this.barcodeList.getData().get(0).getMaxAmount();
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
                txt_unit.setText(this.barcodeList.getData().get(0).getUnit());

                min_price = this.barcodeList.getData().get(0).getMinPrice();
                max_price = this.barcodeList.getData().get(0).getMaxPrice();
                max_amount = this.barcodeList.getData().get(0).getMaxAmount();
            }

            if (barcodeListSize > 1 && this.barcodeList.getData().get(position).getMygroup().equals("wait")) {
                rl_description_purchased.setVisibility(View.VISIBLE);
                ll_spinners.setVisibility(View.GONE);
                ll_texts.setVisibility(View.GONE);

                //add now
                ll_questions.setVisibility(View.VISIBLE);
                ll_chkboxes.setVisibility(View.GONE);




                txt_description_purchased.setText(this.barcodeList.getData().get(position).getDecription());
                txt_barcode.setText(this.barcodeList.getData().get(position).getBarcode());
                txt_unit.setText(this.barcodeList.getData().get(position).getUnit());

                min_price = this.barcodeList.getData().get(position).getMinPrice();
                max_price = this.barcodeList.getData().get(position).getMaxPrice();
                max_amount = this.barcodeList.getData().get(position).getMaxAmount();
            }

            if (barcodeListSize > 1 && this.barcodeList.getData().get(position).getMygroup().equals("product")) {
                rl_description_purchased.setVisibility(View.GONE);
                ll_spinners.setVisibility(View.GONE);
                ll_texts.setVisibility(View.VISIBLE);



                //add now
                ll_questions.setVisibility(View.VISIBLE);
                ll_chkboxes.setVisibility(View.GONE);


                txt_group_purchase.setText(this.barcodeList.getData().get(position).getBarcodeDetail().get(0).getValue());
                txt_type_purchase.setText(this.barcodeList.getData().get(position).getBarcodeDetail().get(1).getValue());
                txt_brand_purchase.setText(this.barcodeList.getData().get(position).getBarcodeDetail().get(2).getValue());
                txt_amount_purchase.setText(this.barcodeList.getData().get(position).getBarcodeDetail().get(3).getValue());
                txt_barcode.setText(this.barcodeList.getData().get(position).getBarcode());
                txt_unit.setText(this.barcodeList.getData().get(position).getUnit());

                min_price = this.barcodeList.getData().get(position).getMinPrice();
                max_price = this.barcodeList.getData().get(position).getMaxPrice();
                max_amount = this.barcodeList.getData().get(position).getMaxAmount();
            }
        }

        if (spinnerList != null) {
            rl_edt_description_purchased.setVisibility(View.VISIBLE);
            rl_photo_purchase_total.setVisibility(View.VISIBLE);
            rl_description_purchased.setVisibility(View.GONE);
            ll_questions.setVisibility(View.VISIBLE);
            ll_chkboxes.setVisibility(View.GONE);
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
            sendData_type = "new";
            String barcode_digit = intent.getStringExtra("barcode_digit");
            if (barcode_digit != null) {
                txt_barcode.setText(barcode_digit);
            }

            txt_title_purchased_item.setText(getResources().getString(R.string.register_new_barcodeList));

        } else {

            if (state != null) {
                txt_title_purchased_item.setText(getResources().getString(R.string.search_goods));
            } else {
                txt_title_purchased_item.setText(getResources().getString(R.string.register_goods));
            }

        }
    }

    private void initView() {

        Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");
        type = intent.getStringExtra("mygroup");
        shopping_id = Cache.getString(PurchasedItemActivity.this, "shopping_id");

        rl_home = findViewById(R.id.rl_home_Purchased_items);
        rl_return = findViewById(R.id.linear_return_qrcode);
        edt_barcode = findViewById(R.id.edt_barcode);
        edt_description_purchased = findViewById(R.id.edt_description_purchased);
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

        rl_register_result = findViewById(R.id.rl_register_result);
        root_purchased_item = findViewById(R.id.root_purchased_item);
        avi_register_barcode = findViewById(R.id.avi_register_barcode);

        txt_spn_group_title = findViewById(R.id.txt_group_spn_title);
        txt_spn_brand_title = findViewById(R.id.txt_brand_spn_title);
        txt_spn_type_title = findViewById(R.id.txt_type_spn_title);
        txt_spn_amount_title = findViewById(R.id.txt_amount_spn_title);
        txt_unit = findViewById(R.id.txt_unit);

        txt_group_purchase = findViewById(R.id.txt_group_purchase);
        txt_type_purchase = findViewById(R.id.txt_type_purchase);
        txt_brand_purchase = findViewById(R.id.txt_brand_purchase);
        txt_amount_purchase = findViewById(R.id.txt_amount_purchase);
        txt_title_purchased_item = findViewById(R.id.txt_title_purchased_item);

        rl_spn_group = findViewById(R.id.rl_spn_group);
        rl_spn_brand = findViewById(R.id.rl_spn_brand);
        rl_spn_type = findViewById(R.id.rl_spn_type);
        rl_spn_amount = findViewById(R.id.rl_spn_amount);
        rl_add_member = findViewById(R.id.rl_add_member);

        rl_register_barcode = findViewById(R.id.rl_register_barcode);
        rl_edt_description_purchased = findViewById(R.id.rl_edt_description_purchased);

        img_arrow_spinner_group = findViewById(R.id.img_arrow_spinner_group);
        img_arrow_spinner_brand = findViewById(R.id.img_arrow_spinner_brand);
        img_arrow_spinner_type = findViewById(R.id.img_arrow_spinner_type);
        img_arrow_spinner_amount = findViewById(R.id.img_arrow_spinner_amount);

        recyclerEditedMember = findViewById(R.id.recycler_edited_members);
        rl_info_img_new_register = findViewById(R.id.rl_info_img_new_register);
        chk_confirmed = findViewById(R.id.chk_confirmed);
        chk_no_confirmed = findViewById(R.id.chk_no_confirmed);
        btn_register_purchased = findViewById(R.id.btn_register_purchased);
        avi_register_purchased = findViewById(R.id.avi_register_purchased);
        edt_amount_purchased = findViewById(R.id.edt_amount_purchased);
        edt_cost_purchase = findViewById(R.id.edt_cost_purchase);
        rl_photo_purchase_total = findViewById(R.id.rl_photo_purchase_total);
        rl_info_member_new_register = findViewById(R.id.rl_info_member_new_register);
        ll_chkboxes = findViewById(R.id.ll_chkboxes);

        rl_home.setOnClickListener(this);
        rl_return.setOnClickListener(this);
        rl_spn_group.setOnClickListener(this);
        rl_spn_brand.setOnClickListener(this);
        rl_spn_type.setOnClickListener(this);
        rl_spn_amount.setOnClickListener(this);
        img_register_barcode.setOnClickListener(this);
        rl_add_member.setOnClickListener(this);
        rl_photo_purchased.setOnClickListener(this);
        chk_confirmed.setOnCheckedChangeListener(this);
        chk_no_confirmed.setOnCheckedChangeListener(this);
        rl_info_img_new_register.setOnClickListener(this);
        btn_register_purchased.setOnClickListener(this);
        rl_info_member_new_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_home_Purchased_items:
                startActivity(new Intent(PurchasedItemActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.linear_return_qrcode:
                startActivity(new Intent(PurchasedItemActivity.this, QRcodeActivity.class));
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

                rl_photo_purchase_total.setVisibility(View.VISIBLE);

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

                str_one_title = spinnersModel.data.oneTitle;
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

                str_two_title = spinnersModel.data.twoTitle;
                str_spn_dialog_header = spinnersModel.data.twoTitle;
                showSpnListDialog(spn_name, searchList, str_spn_dialog_header);

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

            case R.id.rl_info_img_new_register:
                showImageInfoDialog();
                break;

            case R.id.btn_register_purchased:
                if (sendData_type.equals("new")) {
                    send_product_data_new();
                } else {
                    send_product_data();
                }

                break;

            case R.id.rl_info_member_new_register:
                String info_type = "member_info_purchased_item";
                showInfoDialog(info_type);
                break;
        }
    }

    private void showInfoDialog(String info_type) {
        DialogFactory dialogFactory = new DialogFactory(this);
        dialogFactory.createInfoMemberPrizeDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_root, info_type);
    }

    private void send_product_data() {


        SendPurchasedItemData sendData = new SendPurchasedItemData();

        sendData.setMember(editMembers);

        String price = edt_cost_purchase.getText().toString().trim();
//        int price1 = Integer.parseInt(price);
        String amount=edt_amount_purchased.getText().toString().trim();
//        int amount1 = Integer.parseInt(amount);

//        if(price1<min_price){
//            Toast.makeText(context, "مبلغ کمتر از قیمت مجاز است", Toast.LENGTH_LONG).show();
//            return;
//        }else if(price1>max_price){
//            Toast.makeText(context, "مبلغ بیشتر از قیمت مجاز است", Toast.LENGTH_LONG).show();
//            return;
//        }else if(amount1>max_amount){
//            Toast.makeText(context, "تعداد وارد شده بیشتر از تعداد مجاز است", Toast.LENGTH_LONG).show();
//            return;
//        }
//        else {
//            sendData.setCost(price);
//            sendData.setAmount(amount);
//        }

        sendData.setCost(price);
        sendData.setAmount(amount);

        sendData.setProduct_id(product_id);
        sendData.setShopping_id(shopping_id);
        sendData.setType(type);


        avi_register_purchased.setVisibility(View.VISIBLE);
        btn_register_purchased.setVisibility(View.GONE);

        Service service = new ServiceProvider(this).getmService();
        Call<PurchaseItemResult> call = service.getPurchaseItemResult(sendData);
        call.enqueue(new Callback<PurchaseItemResult>() {
            @Override
            public void onResponse(Call<PurchaseItemResult> call, Response<PurchaseItemResult> response) {

                avi_register_purchased.setVisibility(View.GONE);
                btn_register_purchased.setVisibility(View.VISIBLE);

                if (response.code() == 200) {
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.register_product_successfully), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PurchasedItemActivity.this, QRcodeActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (response.code() == 422) {

                    builderMember = null;
                    builderCost = null;
                    buliderAmount = null;
                    APIError422 apiError = ErrorUtils.parseError422(response);

                    if (apiError.errors.member != null) {
                        builderMember = new StringBuilder();
                        for (String a : apiError.errors.member) {
                            builderMember.append("").append(a).append(" ");
                        }
                    }

                    if (apiError.errors.cost != null) {
                        builderCost = new StringBuilder();
                        for (String a : apiError.errors.cost) {
                            builderCost.append("").append(a).append(" ");
                        }
                    }

                    if (apiError.errors.amount != null) {
                        buliderAmount = new StringBuilder();
                        for (String a : apiError.errors.amount) {
                            buliderAmount.append("").append(a).append(" ");
                        }
                    }

                    if (builderMember != null) {
                        Toast.makeText(PurchasedItemActivity.this, "" + builderMember, Toast.LENGTH_SHORT).show();
                    }

                    if (builderCost != null) {
                        String description = String.valueOf(builderCost);
                        createEditAmountDialog(description);
//                        Toast.makeText(PurchasedItemActivity.this, "" + builderCost, Toast.LENGTH_SHORT).show();
                    }

                    if (buliderAmount != null) {
                        String description = String.valueOf(buliderAmount);
                        createEditAmountDialog(description);
//                        Toast.makeText(PurchasedItemActivity.this, "" + buliderAmount, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PurchaseItemResult> call, Throwable t) {
                avi_register_purchased.setVisibility(View.GONE);
                btn_register_purchased.setVisibility(View.VISIBLE);
                Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void send_product_data_new() {
        avi_register_purchased.setVisibility(View.VISIBLE);
        btn_register_purchased.setVisibility(View.GONE);
        SendPurchasedItemNewData sendData = new SendPurchasedItemNewData();

        if (barcode_type.equals("no_match_barcode")) {
            sendData.setBarcode(txt_barcode.getText().toString());
        } else if (barcode_type.equals("new_barcode")) {
            sendData.setBarcode(edt_barcode.getText().toString());
        }
        sendData.setShopping_id(shopping_id);

        if (txt_spn_group_title.getText().toString().equals("گروه")) {
            sendData.setCategory("");
        } else {
            sendData.setCategory(txt_spn_group_title.getText().toString());
        }


        if (txt_spn_brand_title.getText().toString().equals("برند")) {
            sendData.setBrand("");
        } else {
            sendData.setBrand(txt_spn_brand_title.getText().toString());
        }


        sendData.setOne_title(str_one_title);
        sendData.setOne_data(txt_spn_type_title.getText().toString());

        sendData.setTwo_title(str_two_title);
        sendData.setTwo_data(txt_spn_amount_title.getText().toString());


        sendData.setDescription(edt_description_purchased.getText().toString());

        sendData.setCost(edt_cost_purchase.getText().toString().trim());
        sendData.setAmount(edt_amount_purchased.getText().toString().trim());
        sendData.setMember(editMembers);
        sendData.setImage_1(strBm1);
        sendData.setImage_2(strBm3);

        Service service = new ServiceProvider(this).getmService();
        Call<PurchaseItemNewProductResult> call = service.getPurchaseItemNoProductResult(sendData);
        call.enqueue(new Callback<PurchaseItemNewProductResult>() {
            @Override
            public void onResponse(Call<PurchaseItemNewProductResult> call, Response<PurchaseItemNewProductResult> response) {

                if (response.code() == 200) {
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.register_product_successfully), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PurchasedItemActivity.this, QRcodeActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();

                } else if (response.code() == 422) {

                    builderMember = null;
                    builderCost = null;
                    buliderAmount = null;
                    buliderDescription = null;
                    buliderBarcode = null;
                    buliderCategory = null;
                    buliderBrand = null;

                    APIError422 apiError = ErrorUtils.parseError422(response);

                    if (apiError.errors.member != null) {
                        builderMember = new StringBuilder();
                        for (String a : apiError.errors.member) {
                            builderMember.append("").append(a).append(" ");
                        }
                    }

                    if (apiError.errors.cost != null) {
                        builderCost = new StringBuilder();
                        for (String a : apiError.errors.cost) {
                            builderCost.append("").append(a).append(" ");
                        }
                    }

                    if (apiError.errors.amount != null) {
                        buliderAmount = new StringBuilder();
                        for (String a : apiError.errors.amount) {
                            buliderAmount.append("").append(a).append(" ");
                        }
                    }

                    if (apiError.errors.description != null) {
                        buliderDescription = new StringBuilder();
                        for (String a : apiError.errors.description) {
                            buliderDescription.append("").append(a).append(" ");
                        }
                    }

                    if (apiError.errors.barcode != null) {
                        buliderBarcode = new StringBuilder();
                        for (String a : apiError.errors.barcode) {
                            buliderBarcode.append("").append(a).append(" ");
                        }
                    }

                    if (apiError.errors.category!= null) {
                        buliderCategory = new StringBuilder();
                        for (String a : apiError.errors.category) {
                            buliderCategory.append("").append(a).append(" ");
                        }
                    }


                    if (apiError.errors.brand != null) {
                        buliderBrand = new StringBuilder();
                        for (String a : apiError.errors.brand) {
                            buliderBrand.append("").append(a).append(" ");
                        }
                    }


                    if (builderMember != null) {
                        Toast.makeText(PurchasedItemActivity.this, "" + builderMember, Toast.LENGTH_SHORT).show();
                    }

                    if (builderCost != null) {
                        String description = String.valueOf(builderCost);
                        createEditAmountDialog(description);
//                        Toast.makeText(PurchasedItemActivity.this, "" + builderCost, Toast.LENGTH_SHORT).show();
                    }

                    if (buliderAmount != null) {
                        String description = String.valueOf(buliderAmount);
                        createEditAmountDialog(description);
//                        Toast.makeText(PurchasedItemActivity.this, "" + buliderAmount, Toast.LENGTH_SHORT).show();
                    }

                    if (buliderDescription != null) {
                        Toast.makeText(PurchasedItemActivity.this, "" + buliderDescription, Toast.LENGTH_SHORT).show();
                    }

                    if (buliderBarcode != null) {
                        Toast.makeText(PurchasedItemActivity.this, "" + buliderBarcode, Toast.LENGTH_SHORT).show();
                    }

                    if (buliderCategory != null) {
                        Toast.makeText(PurchasedItemActivity.this, "" + buliderCategory, Toast.LENGTH_SHORT).show();
                    }

                    if (buliderBrand != null) {
                        Toast.makeText(PurchasedItemActivity.this, "" + buliderBrand, Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }

                avi_register_purchased.setVisibility(View.GONE);
                btn_register_purchased.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<PurchaseItemNewProductResult> call, Throwable t) {
                avi_register_purchased.setVisibility(View.GONE);
                btn_register_purchased.setVisibility(View.VISIBLE);
                Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createEditAmountDialog(String description) {

        DialogFactory dialogFactory = new DialogFactory(this);
        dialogFactory.createEditAmountDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... strings) {

                startActivity(new Intent(PurchasedItemActivity.this,QRcodeActivity.class));
            }

            @Override
            public void onDeniedButtonClicked(boolean cancel_dialog) {

            }
        },root_purchased_item,description);

    }

    private void showPhotoDialog() {
        final Dialog dialog = new Dialog(PurchasedItemActivity.this);
        dialog.setContentView(R.layout.photo_dialog_purchase);

        RelativeLayout rl_camera1 = dialog.findViewById(R.id.rl_camera1);
        RelativeLayout rl_camera3 = dialog.findViewById(R.id.rl_camera3);
        Button btn_close = dialog.findViewById(R.id.btn_close_photo_dialog);
        TextView txt_phpto = dialog.findViewById(R.id.txt_photo);
        ImageView img_close = dialog.findViewById(R.id.img_close);

        img_delete1 = dialog.findViewById(R.id.img_delete1);
        img_delete3 = dialog.findViewById(R.id.img_delete3);

        img1 = dialog.findViewById(R.id.img1);
        img2 = dialog.findViewById(R.id.img2);
        img3 = dialog.findViewById(R.id.img3);
        img4 = dialog.findViewById(R.id.img4);


        txt_phpto.setText("لطفا با توجه به راهنما از کالا عکس بگیرید.");


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
            strBm1 = "deleted";
            bm1 = null;
            img1.setImageDrawable(null);
            img_delete1.setVisibility(View.GONE);

            image_count1 = 0;
        });

        img_delete3.setOnClickListener(v -> {
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

        return ContextCompat.checkSelfPermission(PurchasedItemActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askCameraPermission(int request_code) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, request_code);
    }


    private void showAddMemberDialog() {

        editMembers = new ArrayList<>();
        final Dialog dialog = new Dialog(PurchasedItemActivity.this);
        dialog.setContentView(R.layout.register_members_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        // to show list of member items
        List<Member> members = new ArrayList<>();

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
        recyclerview_members.setLayoutManager(new LinearLayoutManager(PurchasedItemActivity.this));
        adapter_member = new RegisterMemberDialogAdapter(members, spn_name, dialog, PurchasedItemActivity.this);
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

    // initializing edited member list
    public void updateEditMemberList(ArrayList<RegisterMemberEditModel> editMembers) {
        recyclerEditedMember.setLayoutManager(new GridLayoutManager(PurchasedItemActivity.this, 2));
        adapter_edited = new RegisterMemberEditAdapter(editMembers, PurchasedItemActivity.this);
        recyclerEditedMember.setAdapter(adapter_edited);
    }

    private void getListOfProducts() {
        ll_spinners.setVisibility(View.GONE);
        ll_texts.setVisibility(View.GONE);
        rl_edt_description_purchased.setVisibility(View.GONE);
        rl_description_purchased.setVisibility(View.GONE);
        ll_chkboxes.setVisibility(View.GONE);
        ll_questions.setVisibility(View.GONE);
        chk_no_confirmed.setChecked(false);
        chk_confirmed.setChecked(false);
        img_register_barcode.setVisibility(View.GONE);
        avi_register_barcode.setVisibility(View.VISIBLE);

        txt_title_purchased_item.setText(getResources().getString(R.string.search_goods));


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
                        product_id = barcodeList_unreadable.getData().get(0).getId();
                        type = barcodeList_unreadable.getData().get(0).getMygroup();
                        ll_chkboxes.setVisibility(View.VISIBLE);
                        min_price = barcodeList_unreadable.getData().get(0).getMinPrice();
                        max_price = barcodeList_unreadable.getData().get(0).getMaxPrice();
                        max_amount = barcodeList_unreadable.getData().get(0).getMaxAmount();


                    } else if (barcodeList_unreadable.getData().size() > 1) {
                        Barcode barcode = response.body();
                        showBarcodeListDialog(barcode);
                    }

                    rl_photo_purchase_total.setVisibility(View.GONE);
                    chk_no_confirmed.setChecked(false);
                    chk_confirmed.setChecked(false);
                    sendData_type = "";
                } else if (response.code() == 204) {
                    getSpinneList();

                    ll_questions.setVisibility(View.VISIBLE);
                    ll_chkboxes.setVisibility(View.GONE);
                    rl_edt_description_purchased.setVisibility(View.VISIBLE);
                    rl_description_purchased.setVisibility(View.GONE);
                    sendData_type = "new";
                    barcode_type = "new_barcode";
                    initializeSpinners();
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.no_product), Toast.LENGTH_LONG).show();

                } else if (response.code() == 406) {
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PurchasedItemActivity.this, MainActivity.class));
                    img_register_barcode.setVisibility(View.VISIBLE);
                    avi_register_barcode.setVisibility(View.GONE);
                    rl_photo_purchase_total.setVisibility(View.GONE);
                } else {
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    img_register_barcode.setVisibility(View.VISIBLE);
                    avi_register_barcode.setVisibility(View.GONE);
                    rl_photo_purchase_total.setVisibility(View.GONE);
                    ll_chkboxes.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Barcode> call, Throwable t) {
                Toast.makeText(PurchasedItemActivity.this,
                        "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                img_register_barcode.setVisibility(View.VISIBLE);
                avi_register_barcode.setVisibility(View.GONE);
                rl_photo_purchase_total.setVisibility(View.GONE);
                ll_chkboxes.setVisibility(View.GONE);
            }
        });

    }

    private void initializeSpinners() {

        rl_spn_group.setBackground(getResources().getDrawable(R.drawable.bg_prize_item));
        img_arrow_spinner_group.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
        txt_spn_group_title.setTextColor(getResources().getColor(R.color.blue_dark));
        txt_spn_group_title.setText("گروه");

        rl_spn_brand.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
        img_arrow_spinner_brand.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
        txt_spn_brand_title.setTextColor(getResources().getColor(R.color.grey));
        txt_spn_brand_title.setText("برند");

        rl_spn_type.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
        img_arrow_spinner_type.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
        txt_spn_type_title.setText("");

        rl_spn_amount.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
        img_arrow_spinner_amount.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
        txt_spn_amount_title.setText("");
    }

    private void showBarcodeListDialog(Barcode barcode) {

        DialogFactory dialogFactory = new DialogFactory(PurchasedItemActivity.this);
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

                    Barcode barcodeList_unreadable = new Barcode();
                    detectStatus(barcodeList_unreadable, spinnerList_unreadable);

                } else {
                    img_register_barcode.setVisibility(View.VISIBLE);
                    avi_register_barcode.setVisibility(View.GONE);
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupsData> call, Throwable t) {
                img_register_barcode.setVisibility(View.VISIBLE);
                avi_register_barcode.setVisibility(View.GONE);
                Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showSpnListDialog(String spn_name, List<SearchModel> searchList, String str_spn_dialog_header) {
        DialogFactory dialogFactory = new DialogFactory(PurchasedItemActivity.this);
        dialogFactory.createSpnListDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

                String spn_nam = params[0];
                String dialog_title = params[1];
                showEtcDialog(spn_nam, dialog_title);
            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_root, searchList, this, spn_name, str_spn_dialog_header);
    }

    private void showEtcDialog(String spn_nam, String dialog_title) {

        DialogFactory dialogFactory = new DialogFactory(PurchasedItemActivity.this);
        dialogFactory.createEtcSpnListDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

                String description = params[0];

                if (spn_nam.equals("spn_brand")) {
                    txt_spn_brand_title.setText(description);
                } else if (spn_nam.equals("spn_type")) {
                    txt_spn_type_title.setText(description);
                } else if (spn_nam.equals("spn_amount")) {
                    txt_spn_amount_title.setText(description);
                }
            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_root, this, spn_nam, dialog_title);
    }

    private void showImageInfoDialog() {

        String img1_link = initMemberPrizeLists.data.help_pic_1;
        String img2_link = initMemberPrizeLists.data.help_pic_2;
        DialogFactory dialogFactory = new DialogFactory(PurchasedItemActivity.this);
        dialogFactory.createImageInfoDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_root, img1_link, img2_link);
    }

    @Override
    public void searchListItemOnClick(SearchModel model, AlertDialog dialog, String spn_name) {

        if (spn_name.equals("spn_group")) {

            str_spn_group_title = model.getTitle();
            str_spn_group_id = model.getId();
            txt_spn_group_title.setText(model.getTitle());
            getSpinnerLists(str_spn_group_id);
            dialog.dismiss();

//
//            rl_spn_brand.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
//            img_arrow_spinner_brand.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
//            txt_spn_brand_title.setTextColor(getResources().getColor(R.color.blue_dark));
//            txt_spn_brand_title.setText("برند");

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
                    ll_spinners.setVisibility(View.VISIBLE);
                    ll_texts.setVisibility(View.GONE);

                } else {
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    avi_register_barcode.setVisibility(View.GONE);
                    img_register_barcode.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GroupsData> call, Throwable t) {
                Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
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

                    rl_spn_group.setClickable(true);
                    rl_spn_brand.setClickable(true);
                    rl_spn_type.setClickable(true);
                    rl_spn_amount.setClickable(true);

                    rl_spn_group.setBackground(getResources().getDrawable(R.drawable.bg_prize_item));
                    img_arrow_spinner_group.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
                    txt_spn_group_title.setTextColor(getResources().getColor(R.color.blue_dark));


                    if (spinnersModel.data.brand.size() == 0) {
                        rl_spn_brand.setClickable(false);
                        rl_spn_brand.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
                        img_arrow_spinner_brand.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));

                    } else {

                        rl_spn_brand.setBackground(getResources().getDrawable(R.drawable.bg_prize_item));
                        img_arrow_spinner_brand.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
                        txt_spn_brand_title.setTextColor(getResources().getColor(R.color.blue_dark));
                    }

                    if (spinnersModel.data.oneData.size() == 0) {
                        rl_spn_type.setClickable(false);
                        rl_spn_type.setBackground(getResources().getDrawable(R.drawable.bg_inactive_spn));
                        img_arrow_spinner_type.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_grey));
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

                    } else {
                        rl_spn_amount.setBackground(getResources().getDrawable(R.drawable.bg_prize_item));
                        img_arrow_spinner_amount.setBackground(getResources().getDrawable(R.drawable.arrow_drop_down_blue));
                        txt_spn_amount_title.setTextColor(getResources().getColor(R.color.blue_dark));
                        txt_spn_amount_title.setText(spinnersModel.data.twoTitle);
                    }

                    chk_confirmed.setChecked(false);
                    chk_no_confirmed.setChecked(false);

                } else {
                    Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    avi_register_barcode.setVisibility(View.GONE);
                    img_register_barcode.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<SpinnersModel> call, Throwable t) {
                avi_register_barcode.setVisibility(View.GONE);
                img_register_barcode.setVisibility(View.VISIBLE);
                Toast.makeText(PurchasedItemActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void barcodeListOnClicked(@NotNull BarcodeData model, int position1, @NotNull Barcode barcode, @NotNull AlertDialog dialog) {
        position = position1;
        Barcode barcodeList_unreadable = new Barcode();
        GroupsData spinnerList_unreadable = new GroupsData();
        barcodeList_unreadable = barcode;
        product_id = barcode.getData().get(position1).getId();
        type = barcode.getData().get(position1).getMygroup();
        detectStatus(barcodeList_unreadable, spinnerList_unreadable);

        min_price = barcode.getData().get(position1).getMinPrice();
        max_price = barcode.getData().get(position1).getMaxPrice();
        max_amount = barcode.getData().get(position1).getMaxAmount();

        ll_chkboxes.setVisibility(View.GONE);
        ll_questions.setVisibility(View.VISIBLE);
        txt_title_purchased_item.setText(getResources().getString(R.string.register_goods));

        dialog.dismiss();
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


    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {

        switch (view.getId()) {

            case R.id.chk_confirmed:

                if (chk_confirmed.isChecked()) {
                    ll_questions.setVisibility(View.VISIBLE);
                    chk_confirmed.setChecked(true);
                    chk_no_confirmed.setChecked(false);
                    rl_register_result.setVisibility(View.VISIBLE);

                    txt_title_purchased_item.setText(getResources().getString(R.string.register_goods));


                } else if (!chk_confirmed.isChecked()) {
                    ll_questions.setVisibility(View.GONE);
                    chk_confirmed.setChecked(false);
                    chk_no_confirmed.setChecked(false);
                }

                break;

            case R.id.chk_no_confirmed:

                if (chk_no_confirmed.isChecked()) {
                    ll_questions.setVisibility(View.GONE);
                    chk_confirmed.setChecked(false);
                    chk_no_confirmed.setChecked(true);
                    ll_texts.setVisibility(View.GONE);

                    category_request++;  // to prevent crash
                    ll_spinners.setVisibility(View.VISIBLE);
                    rl_photo_purchase_total.setVisibility(View.VISIBLE);
                    ll_chkboxes.setVisibility(View.GONE);
                    ll_questions.setVisibility(View.VISIBLE);
                    rl_edt_description_purchased.setVisibility(View.VISIBLE);
                    rl_description_purchased.setVisibility(View.GONE);
                    rl_register_result.setVisibility(View.VISIBLE);
                    sendData_type = "new";
                    barcode_type = "no_match_barcode";
                    txt_title_purchased_item.setText(getResources().getString(R.string.register_new_barcodeList));
                    txt_unit.setText("");
                    initializeSpinners();
                } else if (!chk_confirmed.isChecked()) {
                    chk_no_confirmed.setChecked(false);
                    chk_confirmed.setChecked(false);
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
    protected void onStop() {
        super.onStop();
        disposable.dispose();
        unregisterReceiver(connectivityReceiver);
    }
}
