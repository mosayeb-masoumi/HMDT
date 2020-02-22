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
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.adapters.EditPrizeAdapter;
import com.rahbarbazaar.shopper.controllers.adapters.PrizeAdapter;
import com.rahbarbazaar.shopper.controllers.adapters.RegisterMemberDialogAdapter;
import com.rahbarbazaar.shopper.controllers.adapters.RegisterMemberEditAdapter;
import com.rahbarbazaar.shopper.controllers.interfaces.PrizeItemInteraction;
import com.rahbarbazaar.shopper.controllers.interfaces.RegisterItemInteraction;
import com.rahbarbazaar.shopper.controllers.interfaces.SearchItemInteraction;
import com.rahbarbazaar.shopper.models.api_error.APIError422;
import com.rahbarbazaar.shopper.models.api_error.ErrorUtils;
import com.rahbarbazaar.shopper.models.register.GetShopId;
import com.rahbarbazaar.shopper.models.register.Member;
import com.rahbarbazaar.shopper.models.register.RegisterMemberEditModel;
import com.rahbarbazaar.shopper.models.register.RegisterModel;
import com.rahbarbazaar.shopper.models.register.SendPrize;
import com.rahbarbazaar.shopper.models.register.SendRegisterTotalData;
import com.rahbarbazaar.shopper.models.searchable.SearchModel;
import com.rahbarbazaar.shopper.models.shopping_edit.Data;
import com.rahbarbazaar.shopper.models.shopping_edit.SendUpdateTotalData;
import com.rahbarbazaar.shopper.models.shopping_edit.ShoppingEdit;
import com.rahbarbazaar.shopper.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.utilities.Cache;
import com.rahbarbazaar.shopper.utilities.ConvertEnDigitToFa;
import com.rahbarbazaar.shopper.utilities.ConvertorBitmapToString;
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.EditTextWatcher;
import com.rahbarbazaar.shopper.utilities.GeneralTools;
import com.rahbarbazaar.shopper.utilities.RxBus;
import com.rahbarbazaar.shopper.utilities.SolarCalendar;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRegisterActivity extends CustomBaseActivity
        implements View.OnClickListener, RegisterItemInteraction, PrizeItemInteraction, SearchItemInteraction, IPickResult {

    GeneralTools tools;
    BroadcastReceiver connectivityReceiver = null;
    Disposable disposable = new CompositeDisposable();
    DialogFactory dialogFactory;
    RegisterModel registerModel;
    ShoppingEdit shoppingEditModel;
    Button btn_register, btn_update, btn_present_purchase, btn_online_purchase;
    LinearLayout linear_return_new_register;
    AVLoadingIndicatorView avi;
    RegisterMemberDialogAdapter adapter_member;
    RegisterMemberEditAdapter adapter_edited;
    PrizeAdapter adapter_prize;
    EditPrizeAdapter editPrizeAdapter;
    List<SendPrize> sendPrizes;
    ArrayList<RegisterMemberEditModel> editMembers;
    RecyclerView recyclerEditedMember, recycler_prize;
    RelativeLayout rl_spn_shop, rl_addmember, rl_prize, rl_calander, layout_register, rl_member_info,
            rl_prize_info, rl_photo, rl_show_shop_result,rl_info_img_new_register;
    Spinner spn_shop;
    EditText edtDate, edt_discount, edt_total_amount, edt_paid;
    CheckBox checkBox_precentage, checkBox_amount;

    String str_shop_id, info_type, checkbox_text = "";
    Context context;

    TextView txt_header, txt_total_amount_title, txt_paid_title, txt_discount_title,
            txt_spinner_title, txt_checkBox_amount, txt_button_photo, txt_img_count, txt_shop_title;
    // for handling422
    private StringBuilder builderPaid, builderCost, builderDiscountAmount,
            builderShopId, builderMember, builderDate, buliderPrize;

    MemberPrize initMemberPrizeLists;


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

    String spn_name = "";
    ImageView img_delete_shop_item;

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

        //get data from ShoppingEdit fragment
        disposable = RxBus.ShoppingEdit.subscribeShoppingEdit(result -> {
            if (result instanceof ShoppingEdit) {
                shoppingEditModel = (ShoppingEdit) result;
            }
        });


        disposable = RxBus.MemberPrizeLists.subscribeMemberPrizeLists(result -> {
            if (result instanceof MemberPrize) {
                initMemberPrizeLists = (MemberPrize) result;
            }
        });


        context = this;

        initView();

        //initial Dialog factory
        dialogFactory = new DialogFactory(NewRegisterActivity.this);

        if (registerModel.data != null) {

            txt_header.setText(getResources().getString(R.string.register_new));
            SolarCalendar solarCalendar = new SolarCalendar();
            String current_date = solarCalendar.getCurrentShamsiDate();
            edtDate.setText(ConvertEnDigitToFa.convert(current_date));

            btn_register.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.GONE);
            txt_button_photo.setText(getResources().getString(R.string.take_photo));

        } else if (shoppingEditModel.data != null) {
            txt_header.setText("ویرایش خرید");
            edt_total_amount.setText(shoppingEditModel.data.shopping.cost);
//            edt_paid.setText(shoppingEditModel.data.shopping.paid);
//            edt_discount.setText(shoppingEditModel.data.shopping.discount_amount);
            edtDate.setText(shoppingEditModel.data.shopping.date);

//           txt_spinner_title.setText(shoppingEditModel.data.shopping.shop);
            str_shop_id = shoppingEditModel.data.shopping.shop_id; // get shop_id from model(after select spinner shop,shop_id will modified)

            btn_register.setVisibility(View.GONE);
            btn_update.setVisibility(View.VISIBLE);

//            if (shoppingEditModel.data.shopping.discount_type.equals("percent")) {
//                checkBox_precentage.setChecked(true);
//                checkBox_amount.setChecked(false);

//            } else if (shoppingEditModel.data.shopping.discount_type.equals("amount")) {
//                checkBox_precentage.setChecked(false);
//                checkBox_amount.setChecked(true);
//            }

            setEditMemberRecyclere(shoppingEditModel.data);
//            setEditPrizeRecycler(shoppingEditModel.data);

            txt_button_photo.setText("ویرایش عکس");

        }
//        setSpinner();

        txt_total_amount_title.setText(getResources().getString(R.string.tottal_amount));
//        txt_total_amount_title.setText(String.format("%s (ریال)", getResources().getString(R.string.tottal_amount)));
//        txt_paid_title.setText(String.format("%s (ریال)", getResources().getString(R.string.paid_amount)));
//        txt_discount_title.setText(String.format("%s (در صورت تخفیف داشتن خرید از فروشگاه)",
//                getResources().getString(R.string.discount_amount)));
//        txt_checkBox_amount.setText(String.format("%s (ریال)", getResources().getString(R.string.amount)));


        Typeface tf = Typeface.createFromAsset(getAssets(), "BYekan.ttf");
        edt_total_amount.setTypeface(tf);
//        edt_paid.setTypeface(tf);
//        edt_discount.setTypeface(tf);
        edtDate.setTypeface(tf);


        // to count the number of images at first run while in editModel
        if (shoppingEditModel.data != null) {
            if (!shoppingEditModel.data.shopping.image_1.equals("")) {
                image_count1 = 1;
            } else {
                image_count1 = 0;
            }
        }
        if (shoppingEditModel.data != null) {
            if (!shoppingEditModel.data.shopping.image_2.equals("")) {
                image_count2 = 1;
            } else {
                image_count2 = 0;
            }
        }
        if (shoppingEditModel.data != null) {
            if (!shoppingEditModel.data.shopping.image_3.equals("")) {
                image_count3 = 1;
            } else {
                image_count3 = 0;
            }
        }
        if (shoppingEditModel.data != null) {
            if (!shoppingEditModel.data.shopping.image_4.equals("")) {
                image_count4 = 1;
            } else {
                image_count4 = 0;
            }
        }

        int total_img_count1 = image_count1 + image_count2 + image_count3 + image_count4;
        String img_count1 = ConvertEnDigitToFa.convert(String.valueOf(total_img_count1));
        if (total_img_count1 > 0) {
            txt_img_count.setVisibility(View.VISIBLE);
            txt_img_count.setText(String.format("+%s", img_count1));
        } else {
            txt_img_count.setVisibility(View.GONE);
        }


        // to add comma after 3 digits
        edt_total_amount.addTextChangedListener(new EditTextWatcher(edt_total_amount));
//        edt_paid.addTextChangedListener(new EditTextWatcher(edt_paid));
//        edt_discount.addTextChangedListener(new EditTextWatcher(edt_discount));


    }

    private void setEditMemberRecyclere(Data shoppingEditModel) {

        editMembers = new ArrayList<>();
        for (int i = 0; i < shoppingEditModel.shoppingMember.shoppingMemberData.size(); i++) {
            editMembers.add(new RegisterMemberEditModel(shoppingEditModel.shoppingMember.shoppingMemberData.get(i).name,
                    shoppingEditModel.shoppingMember.shoppingMemberData.get(i).id));
        }
        updateEditMemberList(editMembers);
    }

    private void setEditPrizeRecycler(Data shoppingEditModel) {

//        sendPrizes = new ArrayList<>();
//        for (int i = 0; i < shoppingEditModel.shoppingPrize.shoppingPrizeData.size(); i++) {
//            sendPrizes.add(new SendPrize(shoppingEditModel.shoppingPrize.shoppingPrizeData.get(i).prize,
//                    shoppingEditModel.shoppingPrize.shoppingPrizeData.get(i).prizeTypeId));
//        }
//        updateEditPrizeList(sendPrizes);
    }

    private void initView() {
        rl_addmember = findViewById(R.id.rl_addmember);
        recyclerEditedMember = findViewById(R.id.recycler_edited_members);
//        recycler_prize = findViewById(R.id.recycler_prize);
//        rl_spn_shop = findViewById(R.id.rl_spn_shop);
//        spn_shop = findViewById(R.id.spn_shop);
        btn_register = findViewById(R.id.btn_register);
        btn_update = findViewById(R.id.btn_update);
        btn_present_purchase = findViewById(R.id.btn_present_purchase);
        btn_online_purchase = findViewById(R.id.btn_online_purchase);
//        rl_prize = findViewById(R.id.rl_prize);
        rl_calander = findViewById(R.id.rl_calander);
        rl_member_info = findViewById(R.id.rl_info_member_new_register);
//        rl_prize_info = findViewById(R.id.rl_info_prize_new_register);
        rl_photo = findViewById(R.id.rl_photo);
        avi = findViewById(R.id.avi_register);
        edtDate = findViewById(R.id.edtDate);
//        edt_discount = findViewById(R.id.edt_discount);
        edt_total_amount = findViewById(R.id.edt_total_amount);
//        edt_paid = findViewById(R.id.edt_paid);
//        checkBox_precentage = findViewById(R.id.checkBox_precentage);
//        checkBox_amount = findViewById(R.id.checkBox_amount);
        layout_register = findViewById(R.id.layout_new_register);
        txt_header = findViewById(R.id.header_new_register);
        linear_return_new_register = findViewById(R.id.linear_return_new_register);
        txt_total_amount_title = findViewById(R.id.txt_total_amount_title);
        txt_shop_title = findViewById(R.id.txt_shop_title);
//        txt_spinner_title = findViewById(R.id.txt_spinner_title_new_register);
//        txt_paid_title = findViewById(R.id.txt_paid_title);
//        txt_discount_title = findViewById(R.id.txt_discount_title);
//        txt_checkBox_amount = findViewById(R.id.checkBox_amount_txt_new_register);
        txt_button_photo = findViewById(R.id.txt_button_photo);
        txt_img_count = findViewById(R.id.txt_img_count);
        rl_show_shop_result = findViewById(R.id.rl_show_shop_result);
        img_delete_shop_item = findViewById(R.id.img_delete_shop_item);
        rl_info_img_new_register = findViewById(R.id.rl_info_img_new_register);


        rl_addmember.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        edtDate.setOnClickListener(this);
//        rl_prize.setOnClickListener(this);
        rl_calander.setOnClickListener(this);
        btn_present_purchase.setOnClickListener(this);
        btn_online_purchase.setOnClickListener(this);
        img_delete_shop_item.setOnClickListener(this);

        rl_info_img_new_register.setOnClickListener(this);


//        checkBox_precentage.setOnCheckedChangeListener(this);
//        checkBox_amount.setOnCheckedChangeListener(this);
        linear_return_new_register.setOnClickListener(this);
        rl_member_info.setOnClickListener(this);
//        rl_prize_info.setOnClickListener(this);
//        rl_spn_shop.setOnClickListener(this);
        rl_photo.setOnClickListener(this);
//        edt_discount.setEnabled(false);
//        edt_discount.setText("");
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_addmember:
                showAddMemberDialog();
                break;

            case R.id.btn_register:
                if (cameraPermissionGranted()) {
                    sendRegisterData();
                } else {
                    int request_code = 21;
                    askCameraPermission(request_code);
                }

                break;

            case R.id.btn_update:

                sendUpdateData();
                break;

            case R.id.rl_calander:
//                showCalander();
                showCalendarDialog();
                break;
            case R.id.edtDate:
//                showCalander();
                showCalendarDialog();
                break;

//            case R.id.rl_prize:
//                showPrizeDialog();
//                break;

            case R.id.rl_photo:

                if (cameraPermissionGranted()) {
                    showPhotoDialog();
                } else {
                    int request_code = 20;
                    askCameraPermission(request_code);
                }

                break;


            case R.id.linear_return_new_register:
                finish();
                break;

            case R.id.rl_info_member_new_register:
                info_type = "member_info_new_register";
                showInfoDialog(info_type);
                break;


            case R.id.btn_present_purchase:
                spn_name = "present";
                showShopListDialog(spn_name);
                break;

            case R.id.btn_online_purchase:
                spn_name = "online";
                showShopListDialog(spn_name);
                break;

            case R.id.img_delete_shop_item:
                str_shop_id = "";
                txt_shop_title.setText("");
                rl_show_shop_result.setVisibility(View.GONE);
                btn_present_purchase.setBackground(getResources().getDrawable(R.drawable.bg_present_purchase_active));
                btn_online_purchase.setBackground(getResources().getDrawable(R.drawable.bg_online_purchase_active));

                btn_online_purchase.setClickable(true);
                btn_present_purchase.setClickable(true);

                break;


            case R.id.rl_info_img_new_register:
                showImageInfoDialog();
                break;



//            case R.id.rl_info_prize_new_register:
//                info_type = "prize_info_new_register";
//                showInfoDialog(info_type);
//                break;

//            case R.id.rl_spn_shop:
//                showSearchableDialog();
//                break;
        }
    }

    private void showImageInfoDialog() {

        String img1_link = initMemberPrizeLists.data.help_pic_1;
        String img2_link = initMemberPrizeLists.data.help_pic_2;

        DialogFactory dialogFactory = new DialogFactory(NewRegisterActivity.this);
        dialogFactory.createImageInfoDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {


            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, layout_register, img1_link, img2_link);

    }

    private void showShopListDialog(String spn_name) {

//        List<SearchModel> searchList = new ArrayList<>();
//        for (int i = 0; i <spinnerList.getData().size() ; i++) {
//            searchList.add(new SearchModel(spinnerList.getData().get(i).getTitle(),spinnerList.getData().get(i).getId()));
//        }


        List<SearchModel> searchList = null;
        if (spn_name.equals("online")) {
            searchList = new ArrayList<>();
            if (registerModel.data != null) {
                for (int i = 0; i < registerModel.data.shop.size(); i++) {
                    for (int j = 0; j < registerModel.data.shop.get(i).size(); j++) {
                        if (registerModel.data.shop.get(i).get(j).online.equals("yes")) {
                            searchList.add(new SearchModel(registerModel.data.shop.get(i).get(j).title,
                                    registerModel.data.shop.get(i).get(j).id));
                        }

                    }
                }
            } else if (shoppingEditModel.data != null) {
                for (int i = 0; i < shoppingEditModel.data.shop.size(); i++) {
                    for (int j = 0; j < shoppingEditModel.data.shop.get(i).size(); j++) {
                        if (shoppingEditModel.data.shop.get(i).get(j).online.equals("yes")) {
                            searchList.add(new SearchModel(shoppingEditModel.data.shop.get(i).get(j).title,
                                    shoppingEditModel.data.shop.get(i).get(j).id));
                        }

                    }
                }
            }

        } else if (spn_name.equals("present")) {

            searchList = new ArrayList<>();
            if (registerModel.data != null) {
                for (int i = 0; i < registerModel.data.shop.size(); i++) {
                    for (int j = 0; j < registerModel.data.shop.get(i).size(); j++) {
                        if (registerModel.data.shop.get(i).get(j).online.equals("no")) {
                            searchList.add(new SearchModel(registerModel.data.shop.get(i).get(j).title,
                                    registerModel.data.shop.get(i).get(j).id));
                        }

                    }
                }
            } else if (shoppingEditModel.data != null) {
                for (int i = 0; i < shoppingEditModel.data.shop.size(); i++) {
                    for (int j = 0; j < shoppingEditModel.data.shop.get(i).size(); j++) {
                        if (shoppingEditModel.data.shop.get(i).get(j).online.equals("no")) {
                            searchList.add(new SearchModel(shoppingEditModel.data.shop.get(i).get(j).title,
                                    shoppingEditModel.data.shop.get(i).get(j).id));
                        }
                    }
                }
            }
        }


        DialogFactory dialogFactory = new DialogFactory(NewRegisterActivity.this);

        dialogFactory.createOnline_Present_PurchaseListDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {


            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, layout_register, searchList, this, spn_name);
    }


    private boolean cameraPermissionGranted() {

        return ContextCompat.checkSelfPermission(NewRegisterActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askCameraPermission(int request_code) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, request_code);
    }


    private void showPhotoDialog() {

        final Dialog dialog = new Dialog(NewRegisterActivity.this);
        dialog.setContentView(R.layout.photo_dialog);

        RelativeLayout rl_camera1 = dialog.findViewById(R.id.rl_camera1);
        RelativeLayout rl_camera2 = dialog.findViewById(R.id.rl_camera2);
        RelativeLayout rl_camera3 = dialog.findViewById(R.id.rl_camera3);
        RelativeLayout rl_camera4 = dialog.findViewById(R.id.rl_camera4);
        Button btn_close = dialog.findViewById(R.id.btn_close_photo_dialog);

        ImageView img_close = dialog.findViewById(R.id.img_close);

        img_delete1 = dialog.findViewById(R.id.img_delete1);
        img_delete2 = dialog.findViewById(R.id.img_delete2);
        img_delete3 = dialog.findViewById(R.id.img_delete3);
        img_delete4 = dialog.findViewById(R.id.img_delete4);

        img1 = dialog.findViewById(R.id.img1);
        img2 = dialog.findViewById(R.id.img2);
        img3 = dialog.findViewById(R.id.img3);
        img4 = dialog.findViewById(R.id.img4);


        if (bm1 != null && shoppingEditModel.data == null) {
            img1.setImageBitmap(bm1);
            img_delete1.setVisibility(View.VISIBLE);
            image_count1 = 1;
        } else if (bm1 == null && shoppingEditModel.data != null) {
            if (!shoppingEditModel.data.shopping.image_1.equals("")) {
                Glide.with(Objects.requireNonNull(context)).load(shoppingEditModel.data.shopping.image_1).centerCrop().into(img1);
                strBm1 = null;
                img_delete1.setVisibility(View.VISIBLE);

                image_count1 = 1;
            }
        } else if (bm1 != null && shoppingEditModel.data != null) {
            img1.setImageBitmap(bm1);
            img_delete1.setVisibility(View.VISIBLE);
            image_count1 = 1;
        }

        //        if (bm2 != null) {
//            img2.setImageBitmap(bm2);
//            img_delete2.setVisibility(View.VISIBLE);
//        }

        if (bm2 != null && shoppingEditModel.data == null) {
            img2.setImageBitmap(bm2);
            img_delete2.setVisibility(View.VISIBLE);
            image_count2 = 1;
        } else if (bm2 == null && shoppingEditModel.data != null) {
            if (!shoppingEditModel.data.shopping.image_2.equals("")) {
                Glide.with(Objects.requireNonNull(context)).load(shoppingEditModel.data.shopping.image_2).centerCrop().into(img2);
                strBm2 = null;
                img_delete2.setVisibility(View.VISIBLE);
                image_count2 = 1;
            }
        } else if (bm2 != null && shoppingEditModel.data != null) {
            img2.setImageBitmap(bm2);
            img_delete2.setVisibility(View.VISIBLE);
            image_count2 = 1;
        }


        if (bm3 != null && shoppingEditModel.data == null) {
            img3.setImageBitmap(bm3);
            img_delete3.setVisibility(View.VISIBLE);
            image_count3 = 1;
        } else if (bm3 == null && shoppingEditModel.data != null) {
            if (!shoppingEditModel.data.shopping.image_3.equals("")) {
                Glide.with(Objects.requireNonNull(context)).load(shoppingEditModel.data.shopping.image_3).centerCrop().into(img3);
                strBm3 = null;
                img_delete3.setVisibility(View.VISIBLE);
                image_count3 = 1;
            }
        } else if (bm3 != null && shoppingEditModel.data != null) {
            img3.setImageBitmap(bm3);
            img_delete3.setVisibility(View.VISIBLE);
            image_count3 = 1;
        }


        if (bm4 != null && shoppingEditModel.data == null) {
            img4.setImageBitmap(bm4);
            img_delete4.setVisibility(View.VISIBLE);
            image_count4 = 1;
        } else if (bm4 == null && shoppingEditModel.data != null) {
            if (!shoppingEditModel.data.shopping.image_4.equals("")) {
                Glide.with(Objects.requireNonNull(context)).load(shoppingEditModel.data.shopping.image_4).centerCrop().into(img4);
                strBm4 = null;
                img_delete4.setVisibility(View.VISIBLE);
                image_count4 = 1;
            }
        } else if (bm4 != null && shoppingEditModel.data != null) {
            img4.setImageBitmap(bm4);
            img_delete4.setVisibility(View.VISIBLE);
            image_count4 = 1;
        }


//        if(bm1==null && !shoppingEditModel.data.shopping.image1.equals("")){
//            Glide.with(Objects.requireNonNull(context)).load(shoppingEditModel.data.shopping.image1).centerCrop().into(img1);
//        }


        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        rl_camera1.setOnClickListener(v -> {
            status = 1;
            choose_pic();
        });
        rl_camera2.setOnClickListener(v -> {
            status = 2;
            choose_pic();
        });
        rl_camera3.setOnClickListener(v -> {
            status = 3;
            choose_pic();
        });
        rl_camera4.setOnClickListener(v -> {
            status = 4;
            choose_pic();
        });

        img_delete1.setOnClickListener(v -> {
//            strBm1 = "";
            strBm1 = "deleted";
            bm1 = null;
            img1.setImageDrawable(null);
            img_delete1.setVisibility(View.GONE);

            image_count1 = 0;

            if (shoppingEditModel.data != null) {
                shoppingEditModel.data.shopping.image_1 = ""; // to dosent download url again
            }

        });

        img_delete2.setOnClickListener(v -> {
//            strBm2 = "";
            strBm2 = "deleted";
            bm2 = null;
            img2.setImageDrawable(null);
            img_delete2.setVisibility(View.GONE);
            image_count2 = 0;

            if (shoppingEditModel.data != null) {
                shoppingEditModel.data.shopping.image_2 = ""; // to dosent download url again
            }

        });
        img_delete3.setOnClickListener(v -> {
//            strBm3 = "";
            strBm3 = "deleted";
            bm3 = null;
            img3.setImageDrawable(null);
            img_delete3.setVisibility(View.GONE);
            image_count3 = 0;

            if (shoppingEditModel.data != null) {
                shoppingEditModel.data.shopping.image_3 = ""; // to dosent download url again
            }

        });
        img_delete4.setOnClickListener(v -> {
//            strBm4 = "";
            bm4 = null;
            strBm4 = "deleted";
            img4.setImageDrawable(null);
            img_delete4.setVisibility(View.GONE);
            image_count4 = 0;

            if (shoppingEditModel.data != null) {
                shoppingEditModel.data.shopping.image_4 = ""; // to dosent download url again
            }
        });


        img_close.setOnClickListener(v -> {
            dialog.dismiss();

            int total_img_count = image_count1 + image_count2 + image_count3 + image_count4;

            String img_count = ConvertEnDigitToFa.convert(String.valueOf(total_img_count));

            if (total_img_count > 0) {
                txt_img_count.setVisibility(View.VISIBLE);
                txt_img_count.setText(String.format("+%s", img_count));
            } else {
                txt_img_count.setVisibility(View.GONE);
            }

        });


        btn_close.setOnClickListener(v -> {
            dialog.dismiss();
            int total_img_count = image_count1 + image_count2 + image_count3 + image_count4;

            String img_count = ConvertEnDigitToFa.convert(String.valueOf(total_img_count));

            if (total_img_count > 0) {
                txt_img_count.setVisibility(View.VISIBLE);
                txt_img_count.setText(String.format("+%s", img_count));
            } else {
                txt_img_count.setVisibility(View.GONE);
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
            } else if (status == 2) {
                img2.setImageBitmap(r.getBitmap());
                bm2 = r.getBitmap();
                strBm2 = ConvertorBitmapToString.bitmapToString(bm2);
                img_delete2.setVisibility(View.VISIBLE);
                image_count2 = 1;
            } else if (status == 3) {
                img3.setImageBitmap(r.getBitmap());
                bm3 = r.getBitmap();
                strBm3 = ConvertorBitmapToString.bitmapToString(bm3);
                img_delete3.setVisibility(View.VISIBLE);
                image_count3 = 1;
            } else if (status == 4) {
                img4.setImageBitmap(r.getBitmap());
                bm4 = r.getBitmap();
                strBm4 = ConvertorBitmapToString.bitmapToString(bm4);
                img_delete4.setVisibility(View.VISIBLE);
                image_count4 = 1;
            }

        }
    }


//    private void showSearchableDialog() {
//
//        List<SearchModel> searchList = new ArrayList<>();
//        if (registerModel.data != null) {
//            for (int i = 0; i < registerModel.data.shop.size(); i++) {
//                for (int j = 0; j < registerModel.data.shop.get(i).size(); j++) {
//                    searchList.add(new SearchModel(registerModel.data.shop.get(i).get(j).title,
//                            registerModel.data.shop.get(i).get(j).id));
//                }
//            }
//        } else if (shoppingEditModel.data != null) {
//            for (int i = 0; i < shoppingEditModel.data.shop.size(); i++) {
//                for (int j = 0; j < shoppingEditModel.data.shop.get(i).size(); j++) {
//                    searchList.add(new SearchModel(shoppingEditModel.data.shop.get(i).get(j).title,
//                            shoppingEditModel.data.shop.get(i).get(j).id));
//                }
//            }
//        }
//
//        dialogFactory.createSearchableDialog(new DialogFactory.DialogFactoryInteraction() {
//            @Override
//            public void onAcceptButtonClicked(String... params) {
//
//
//            }
//
//            @Override
//            public void onDeniedButtonClicked(boolean bool) {
//
//            }
//        }, layout_register, searchList, this);
//    }


    private void showInfoDialog(String info_type) {
        dialogFactory.createInfoMemberPrizeDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {


            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, layout_register, info_type);
    }

    private void showCalendarDialog() {

        dialogFactory.createCalendarDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

                String date = params[0];
                edtDate.setText(date);

            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, layout_register);

    }


    private void showAddMemberDialog() {

        editMembers = new ArrayList<>();
        final Dialog dialog = new Dialog(NewRegisterActivity.this);
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
        recyclerview_members.setLayoutManager(new LinearLayoutManager(NewRegisterActivity.this));
        adapter_member = new RegisterMemberDialogAdapter(members, NewRegisterActivity.this);
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

//    private void showPrizeDialog() {
//        sendPrizes = new ArrayList<>();
//        final Dialog dialog = new Dialog(NewRegisterActivity.this);
//        dialog.setContentView(R.layout.prize_dialog);
//
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//
//        // to show list of member items
//        List<Prize> prizes = new ArrayList<>();
//
//        for (int i = 0; i < initMemberPrizeLists.data.prize.size(); i++) {
//            for (int j = 0; j < initMemberPrizeLists.data.prize.get(i).size(); j++) {
//                prizes.add(new Prize(initMemberPrizeLists.data.prize.get(i).get(j).title
//                        , initMemberPrizeLists.data.prize.get(i).get(j).id));
//            }
//        }
//
//
//        RecyclerView recycler_prize = dialog.findViewById(R.id.recycler_prize);
//        Button btn_exit_dialog = dialog.findViewById(R.id.btn_exit_dialog);
//        ImageView img_close = dialog.findViewById(R.id.img_close);
//        recycler_prize.setLayoutManager(new LinearLayoutManager(NewRegisterActivity.this));
//        adapter_prize = new PrizeAdapter(prizes, NewRegisterActivity.this);
//        adapter_prize.setListener(this);  // important or else the app will crashed
////        adapter_prize.setListener(this);  // important or else the app will crashed
//        recycler_prize.setAdapter(adapter_prize);
//
//        img_close.setOnClickListener(v -> dialog.dismiss());
//        btn_exit_dialog.setOnClickListener(v -> dialog.dismiss());
//
//        dialog.show();
//    }

    // to setCheck single checkbox and show in list
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

    // initializing edited member list
    public void updateEditMemberList(ArrayList<RegisterMemberEditModel> editMembers) {

        recyclerEditedMember.setLayoutManager(new GridLayoutManager(NewRegisterActivity.this, 2));
        adapter_edited = new RegisterMemberEditAdapter(editMembers, NewRegisterActivity.this);
        recyclerEditedMember.setAdapter(adapter_edited);
    }

    private void sendRegisterData() {

        btn_register.setVisibility(View.GONE);
        btn_update.setVisibility(View.GONE);
        avi.setVisibility(View.VISIBLE);

        String total_amount = edt_total_amount.getText().toString();
//        String total_paid = edt_paid.getText().toString();
//        String discount_amount = edt_discount.getText().toString();
        String date = edtDate.getText().toString();

        SendRegisterTotalData sendData = new SendRegisterTotalData();
        sendData.setMember(editMembers);
        sendData.setPrize(sendPrizes);
        sendData.setShop_id(str_shop_id);
        sendData.setCost(total_amount);
//        sendData.setPaid(total_paid);
        sendData.setLat(Cache.getString(NewRegisterActivity.this, "lat"));
        sendData.setLng(Cache.getString(NewRegisterActivity.this, "lng"));

        sendData.setImage_1(strBm1);
        sendData.setImage_2(strBm2);
        sendData.setImage_3(strBm3);
        sendData.setImage_4(strBm4);

//        if (Cache.getString("validate_area").equals("true")) {
        if (Cache.getString(NewRegisterActivity.this, "validate_area").equals("true")) {
            sendData.setValidate_area("yes");
        } else {
            sendData.setValidate_area("no");
        }


//        if (checkBox_precentage.isChecked()) {
//            sendData.setDiscount_type("percent");
//            sendData.setDiscount_amount(discount_amount);
//        } else if (checkBox_amount.isChecked()) {
//            sendData.setDiscount_type("amount");
//            sendData.setDiscount_amount(discount_amount);
//        }

        sendData.setDate(date);

        Service service = new ServiceProvider(this).getmService();
        Call<GetShopId> call = service.registerNewShop(sendData);
        call.enqueue(new Callback<GetShopId>() {
            @Override
            public void onResponse(Call<GetShopId> call, Response<GetShopId> response) {
                if (response.code() == 200) {

                    String shopping_id = response.body().data;
//                    Cache.setString("shopping_id",shopping_id);
                    Cache.setString(NewRegisterActivity.this, "shopping_id", shopping_id);

                    Intent intent = new Intent(NewRegisterActivity.this, QRcodeActivity1.class);
                    intent.putExtra("static_barcode", "static_barcode");
                    startActivity(intent);
                    NewRegisterActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    Toast.makeText(NewRegisterActivity.this, "مشخصات خرید ثبت شد,لطفا اقلام خرید را وارد نمایید.", Toast.LENGTH_SHORT).show();
                    finish();
                    btn_register.setVisibility(View.VISIBLE);
                    avi.setVisibility(View.GONE);

                } else if (response.code() == 422) {

                    btn_register.setVisibility(View.VISIBLE);
                    avi.setVisibility(View.GONE);

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
                    if (buliderPrize != null) {
                        Toast.makeText(NewRegisterActivity.this, "" + buliderPrize, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(NewRegisterActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    btn_register.setVisibility(View.VISIBLE);
                    avi.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetShopId> call, Throwable t) {
                Toast.makeText(NewRegisterActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                btn_register.setVisibility(View.VISIBLE);
                avi.setVisibility(View.GONE);
            }
        });
    }

    private void sendUpdateData() {
        btn_update.setVisibility(View.GONE);
        btn_register.setVisibility(View.GONE);
        avi.setVisibility(View.VISIBLE);

        String total_amount = edt_total_amount.getText().toString();
//        String total_paid = edt_paid.getText().toString();
//        String discount_amount = edt_discount.getText().toString();
        String date = edtDate.getText().toString();

        SendUpdateTotalData sendData = new SendUpdateTotalData();
        sendData.setMember(editMembers);
        sendData.setPrize(sendPrizes);
        sendData.setShop_id(str_shop_id);
        sendData.setCost(total_amount);
//        sendData.setPaid(total_paid);


        if (strBm1 != null) {
            sendData.setImage_1(strBm1);
        }

        if (strBm2 != null) {
            sendData.setImage_2(strBm2);
        }

        if (strBm3 != null) {
            sendData.setImage_3(strBm3);
        }

        if (strBm4 != null) {
            sendData.setImage_4(strBm4);
        }


//        sendData.setShopping_id(Cache.getString("shopping_id"));
        sendData.setShopping_id(Cache.getString(NewRegisterActivity.this, "shopping_id"));

//        String chechBox_type = checkbox_text;
//        if (!checkBox_amount.isChecked() && !checkBox_precentage.isChecked()) {
//            chechBox_type = "";
//        }


//        if (chechBox_type.equals("مبلغ") || chechBox_type.equals("amount")) {
//            sendData.setDiscount_type("amount");
//            sendData.setDiscount_amount(discount_amount);
//        } else if (chechBox_type.equals("درصد") || chechBox_type.equals("percent")) {
//            sendData.setDiscount_type("percent");
//            sendData.setDiscount_amount(discount_amount);
//        } else {
//            sendData.setDiscount_type("not_set");
//        }

        sendData.setDate(date);

        Service service = new ServiceProvider(this).getmService();
        Call<GetShopId> call = service.update(sendData);
        call.enqueue(new Callback<GetShopId>() {
            @Override
            public void onResponse(Call<GetShopId> call, Response<GetShopId> response) {
                if (response.code() == 200) {

                    btn_update.setVisibility(View.VISIBLE);
                    btn_register.setVisibility(View.GONE);
                    avi.setVisibility(View.GONE);
//                    String id = response.body().data;
                    Toast.makeText(NewRegisterActivity.this, "" + getResources().getString(R.string.update_done), Toast.LENGTH_SHORT).show();
                    finish();

                } else if (response.code() == 422) {

                    btn_update.setVisibility(View.VISIBLE);
                    btn_register.setVisibility(View.GONE);
                    avi.setVisibility(View.GONE);
                    builderMember = null;
                    builderShopId = null;
                    builderCost = null;
                    builderPaid = null;
                    builderDiscountAmount = null;
                    builderDate = null;
                    buliderPrize = null;

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

                    if (apiError.errors.prize != null) {
                        buliderPrize = new StringBuilder();
                        for (String b : apiError.errors.prize) {
                            buliderPrize.append("").append(b).append(" ");
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
                    if (buliderPrize != null) {
                        Toast.makeText(NewRegisterActivity.this, "" + buliderPrize, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(NewRegisterActivity.this, "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    btn_update.setVisibility(View.VISIBLE);
                    btn_register.setVisibility(View.GONE);
                    avi.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetShopId> call, Throwable t) {
                Toast.makeText(NewRegisterActivity.this, "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                btn_update.setVisibility(View.VISIBLE);
                btn_register.setVisibility(View.GONE);
                avi.setVisibility(View.GONE);
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
        }, title, id, layout_register);
    }

    private void updateEditPrizeList(List<SendPrize> sendPrizes) {

        recycler_prize.setLayoutManager(new GridLayoutManager(NewRegisterActivity.this, 2));
        editPrizeAdapter = new EditPrizeAdapter(sendPrizes, NewRegisterActivity.this);
        recycler_prize.setAdapter(editPrizeAdapter);
    }

//    @Override
//    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
//
//        if (!checkBox_precentage.isChecked() && !checkBox_amount.isChecked()) {
//            edt_discount.setHint(getResources().getString(R.string.percent_amount));
//            edt_discount.setEnabled(false);
//            edt_discount.setText("");
//        }
//
//        switch (view.getId()) {
//            case R.id.checkBox_amount:
//                if (isChecked) {
//                    checkBox_precentage.setChecked(false);
//                    edt_discount.setHint(getResources().getString(R.string.amount2_));
//                    checkbox_text = getResources().getString(R.string.amount2_);
//                    edt_discount.setEnabled(true);
//                }
//                break;
//
//            case R.id.checkBox_precentage:
//                if (isChecked) {
//                    checkBox_amount.setChecked(false);
//                    edt_discount.setHint(getResources().getString(R.string.percent));
//                    checkbox_text = getResources().getString(R.string.percent);
//                    edt_discount.setEnabled(true);
//                }
//                break;
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 20) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPhotoDialog();
            } else {
                Toast.makeText(this, "نیاز به اجازه ی دسترسی دوربین", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 21) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendRegisterData();
            } else {
                Toast.makeText(this, "نیاز به اجازه ی دسترسی دوربین", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        rl_show_shop_result.setVisibility(View.VISIBLE);
        txt_shop_title.setText(model.getTitle());
        str_shop_id = model.getId();
        dialog.dismiss();

        if(spn_name.equals("online")){
            btn_online_purchase.setBackground(getResources().getDrawable(R.drawable.bg_online_purchase_active));
            btn_present_purchase.setBackground(getResources().getDrawable(R.drawable.bg_present_purchase_deactive));
            btn_online_purchase.setClickable(true);
            btn_present_purchase.setClickable(false);

        }

        if(spn_name.equals("present")){
            btn_present_purchase.setBackground(getResources().getDrawable(R.drawable.bg_present_purchase_active));
            btn_online_purchase.setBackground(getResources().getDrawable(R.drawable.bg_online_purchase_deactive));
            btn_present_purchase.setClickable(true);
            btn_online_purchase.setClickable(false);
        }


    }
}

