package com.rahbarbazaar.checkpanel.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.controllers.adapters.BarcodeListDetailAdapter;
import com.rahbarbazaar.checkpanel.controllers.adapters.ProfileMemberDetailAdapter;
import com.rahbarbazaar.checkpanel.controllers.adapters.SearchAdapter;
import com.rahbarbazaar.checkpanel.models.barcodlist.BarcodeData;
import com.rahbarbazaar.checkpanel.models.barcodlist.BarcodeDetail;
import com.rahbarbazaar.checkpanel.models.edit_products.EditProducts;
import com.rahbarbazaar.checkpanel.models.history.History;
import com.rahbarbazaar.checkpanel.models.profile.MemberDetail;
import com.rahbarbazaar.checkpanel.models.profile.MemberDetailObj;
import com.rahbarbazaar.checkpanel.models.searchable.SearchModel;
import com.rahbarbazaar.checkpanel.ui.activities.MainActivity;
import com.rahbarbazaar.checkpanel.ui.activities.NewRegisterActivity;
import com.rahbarbazaar.checkpanel.ui.activities.ScanActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DialogFactory {

    private Context context;


    public interface DialogFactoryInteraction {

        void onAcceptButtonClicked(String... strings);

        void onDeniedButtonClicked(boolean cancel_dialog);
    }

    public DialogFactory(Context ctx) {
        this.context = ctx;
    }

    public void createNoInternetDialog(DialogFactoryInteraction listener, View root) {

//        View customLayout = LayoutInflater.from(context).inflate(R.layout.no_internet_dialog, (ViewGroup) root, false);
        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog, (ViewGroup) root, false);

        Button btn_wifi_dialog = customLayout.findViewById(R.id.btn1);
        Button btn_data_dialog = customLayout.findViewById(R.id.btn2);
        TextView txt_description = customLayout.findViewById(R.id.txt_description);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        ImageView img_close = customLayout.findViewById(R.id.img_close);

        btn_wifi_dialog.setText(context.getResources().getString(R.string.internet_setting));
        btn_data_dialog.setText(context.getResources().getString(R.string.data_setting));
        txt_header.setText(context.getResources().getString(R.string.warning));
        txt_description.setText(context.getResources().getString(R.string.no_internet_connection));


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //set click listener for views inside of dialog
        btn_wifi_dialog.setOnClickListener(view -> listener.onAcceptButtonClicked(""));
        btn_data_dialog.setOnClickListener(view -> listener.onDeniedButtonClicked(false));
        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        //if dialog dismissed, this action will be called
        dialog.setOnDismissListener(dialogInterface -> listener.onDeniedButtonClicked(true));

        dialog.show();
    }


    public void createConfirmExitDialog(DialogFactoryInteraction listener, View view) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.confirm_exit_dialog2, (ViewGroup) view, false);
        //define views inside of dialog
        TextView btn_exit_dialog = customLayout.findViewById(R.id.btn_exit_dialog);
        TextView btn_cancel_dialog = customLayout.findViewById(R.id.btn_cancel_dialog);
        TextView text_body = customLayout.findViewById(R.id.text_body);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_cancel_dialog.setOnClickListener(v -> {
            dialog.dismiss();
            listener.onDeniedButtonClicked(false);
        });

        btn_exit_dialog.setOnClickListener(v -> listener.onAcceptButtonClicked("")
        );

        dialog.show();
    }


    public void createPrizeDetailDialog(DialogFactoryInteraction listener, String title, String id, View view) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.prize_detail_dialog, (ViewGroup) view, false);
        //define views inside of dialog
        Button btn_register = customLayout.findViewById(R.id.btn_register_dialog);
        Button btn_cancel = customLayout.findViewById(R.id.btn_exit_dialog);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        EditText edt_description = customLayout.findViewById(R.id.edt_description);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_register.setOnClickListener(v -> {

            String desc = edt_description.getText().toString();
            if (desc.trim().length() > 0) {
                listener.onAcceptButtonClicked(desc, title, id);
                dialog.dismiss();

            } else {
                Toast.makeText(context, "" + context.getResources().getString(R.string.add_description), Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        img_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void createOutOfAreaDialog(DialogFactoryInteraction listener, View view) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.out_area_dialog, (ViewGroup) view, false);
        //define views inside of dialog
        Button btn_exit = customLayout.findViewById(R.id.btn_exit_dialog);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_exit.setOnClickListener(v -> {
            listener.onAcceptButtonClicked();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void createChooseScannerDialog(DialogFactoryInteraction listener, View view) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog, (ViewGroup) view, false);
        //define views inside of dialog
        Button btn_scanner = customLayout.findViewById(R.id.btn1);
        Button btn_search = customLayout.findViewById(R.id.btn2);
        TextView txt_description = customLayout.findViewById(R.id.txt_description);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        ImageView img_close = customLayout.findViewById(R.id.img_close);

        btn_scanner.setText(context.getResources().getString(R.string.scanner));
        btn_search.setText(context.getResources().getString(R.string.search_product));
        txt_header.setText("ثبت بارکد");
        txt_description.setText("یکی از روشهای زیر را انتخاب کنید");

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_scanner.setOnClickListener(v -> {
            listener.onAcceptButtonClicked();
        });

        btn_search.setOnClickListener(v -> {
            listener.onDeniedButtonClicked(false);
        });

        img_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void createbarcodeListDetailDialog(DialogFactoryInteraction listener, View view, BarcodeData model) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.barcodelist_detaildialog, (ViewGroup) view, false);


        ImageView img_close = customLayout.findViewById(R.id.img_close);
        RecyclerView recyclerView = customLayout.findViewById(R.id.rv_barcodeListDetail);
        Button btn_close = customLayout.findViewById(R.id.btn_close_barcodeListDialog);


        //set recyclerview
        BarcodeListDetailAdapter adapter;
        List<BarcodeDetail> barcodeDetail = new ArrayList<>();
        barcodeDetail.addAll(model.getBarcodeDetail());

        adapter = new BarcodeListDetailAdapter(barcodeDetail, view.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

//        Glide.with(Objects.requireNonNull(context)).load(model.getImage()).centerCrop().into(imageview);
        img_close.setOnClickListener(v -> dialog.dismiss());
        btn_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    public void createRescanDialog(DialogFactoryInteraction listener, View view) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.rescan_dialog, (ViewGroup) view, false);

        //define views inside of dialog
        Button btn_home = customLayout.findViewById(R.id.btn_home);
        Button btn_search = customLayout.findViewById(R.id.btn_search);
        Button btn_scan = customLayout.findViewById(R.id.btn_scan);
        ImageView img_close = customLayout.findViewById(R.id.img_close);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_home.setOnClickListener(v -> context.startActivity(new Intent(context, MainActivity.class)));
        btn_scan.setOnClickListener(v -> context.startActivity(new Intent(context, ScanActivity.class)));
        btn_search.setOnClickListener(v -> {
        });

        img_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    public void createDeactiveActionDialog(DialogFactoryInteraction listener, View view) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog2, (ViewGroup) view, false);

        //define views inside of dialog
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        TextView txt_description = customLayout.findViewById(R.id.txt_description);
        Button btn_close = customLayout.findViewById(R.id.btn);
        ImageView img_close = customLayout.findViewById(R.id.img_close);

        btn_close.setText(context.getResources().getString(R.string.close));

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        txt_header.setText(context.getResources().getString(R.string.system_message));
        txt_description.setText(context.getResources().getString(R.string.unallowable_registeration));

        btn_close.setOnClickListener(v -> dialog.dismiss());
        img_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    public void createError406Dialog(DialogFactoryInteraction listener, View view, String message) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog2, (ViewGroup) view, false);


        //define views inside of dialog
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        TextView txt_description = customLayout.findViewById(R.id.txt_description);
        Button btn_close = customLayout.findViewById(R.id.btn);
        ImageView img_close = customLayout.findViewById(R.id.img_close);

        btn_close.setText(context.getResources().getString(R.string.close));

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        txt_header.setText(context.getResources().getString(R.string.system_error));
        txt_description.setText(message);

//        btn_close.setOnClickListener(v ->
//                listener.onAcceptButtonClicked()
//                dialog.dismiss());

        btn_close.setOnClickListener(v -> {
            listener.onAcceptButtonClicked();
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }


    public void createReportIssueDialog(DialogFactoryInteraction listener, View root) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.report_issue_dialog, (ViewGroup) root, false);


        //define views inside of dialog
        EditText edt_description = customLayout.findViewById(R.id.edt_description);
        Button btn_send = customLayout.findViewById(R.id.btn_send_dialog);
        Button btn_cancel_dialog = customLayout.findViewById(R.id.btn_cancel_dialog);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        ImageView img_close = customLayout.findViewById(R.id.img_close);

        txt_header.setText(context.getResources().getString(R.string.report_issue_title));

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //set click listener for views inside of dialog

        btn_send.setOnClickListener(view -> {

            listener.onAcceptButtonClicked(edt_description.getText().toString());

            if (edt_description.getText().toString().equals("")) {

            } else {
                dialog.dismiss();
            }

        });


        btn_cancel_dialog.setOnClickListener(view -> {
            listener.onDeniedButtonClicked(false);
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    public void createHistoryDetailDialog(DialogFactoryInteraction listener, View root, History model) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.history_detaildialog, (ViewGroup) root, false);


        //define views inside of dialog

//        Button btn_cancel_dialog = customLayout.findViewById(R.id.btn_cancel_dialog);
        TextView txt_header = customLayout.findViewById(R.id.txt_header_historydetail);
        TextView txt_shop = customLayout.findViewById(R.id.txt_shop);
        TextView txt_purchased_date = customLayout.findViewById(R.id.txt_purchased_date);
        TextView txt_register_date = customLayout.findViewById(R.id.txt_register_date);
        TextView txt_total_purchased_amount = customLayout.findViewById(R.id.txt_total_purchased_amount);
        TextView txt_paid = customLayout.findViewById(R.id.txt_paid);
        TextView txt_discount_amount = customLayout.findViewById(R.id.txt_discount_amount);
        TextView txt_discount_type = customLayout.findViewById(R.id.txt_discount_type);
        TextView txt_members = customLayout.findViewById(R.id.txt_members);
        TextView txt_gifts = customLayout.findViewById(R.id.txt_gifts);
        CardView cardview1 = customLayout.findViewById(R.id.cardview1);
        CardView cardview2 = customLayout.findViewById(R.id.cardview2);
        CardView cardview3 = customLayout.findViewById(R.id.cardview3);
        CardView cardview4 = customLayout.findViewById(R.id.cardview4);
        ImageView image1 = customLayout.findViewById(R.id.image1);
        ImageView image2 = customLayout.findViewById(R.id.image2);
        ImageView image3 = customLayout.findViewById(R.id.image3);
        ImageView image4 = customLayout.findViewById(R.id.image4);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        LinearLayout ll_discount_amount = customLayout.findViewById(R.id.ll_discount_amount);

        if (model.getDiscountType().equals("تخفیف ندارد")) {
            ll_discount_amount.setVisibility(View.GONE);
        }


        txt_header.setText(context.getResources().getString(R.string.explanation2));
        txt_shop.setText(model.getTitle());
        txt_purchased_date.setText(model.getDate());
        txt_register_date.setText(model.getCreatedAt());
        txt_total_purchased_amount.setText(model.getCost());
        txt_paid.setText(model.getPaid());

        txt_discount_type.setText(model.getDiscountType());
        txt_members.setText(model.getShoppingMember());
        txt_gifts.setText(model.getShoppingPrize());


        if (!model.getShoppingImage1().equals("")) {
            cardview1.setVisibility(View.VISIBLE);
            image1.setVisibility(View.VISIBLE);
            Glide.with(Objects.requireNonNull(context)).load(model.getShoppingImage1()).centerCrop().into(image1);
        } else {
            cardview1.setVisibility(View.GONE);
            image1.setVisibility(View.GONE);
        }

        if (!model.getShoppingImage2().equals("")) {
            cardview2.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            Glide.with(Objects.requireNonNull(context)).load(model.getShoppingImage2()).centerCrop().into(image2);
        } else {
            cardview2.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
        }

        if (!model.getShoppingImage3().equals("")) {
            cardview3.setVisibility(View.VISIBLE);
            image3.setVisibility(View.VISIBLE);
            Glide.with(Objects.requireNonNull(context)).load(model.getShoppingImage3()).centerCrop().into(image3);
        } else {
            cardview3.setVisibility(View.GONE);
            image3.setVisibility(View.GONE);
        }

        if (!model.getShoppingImage4().equals("")) {
            cardview4.setVisibility(View.VISIBLE);
            image4.setVisibility(View.VISIBLE);
            Glide.with(Objects.requireNonNull(context)).load(model.getShoppingImage4()).centerCrop().into(image4);
        } else {
            cardview4.setVisibility(View.GONE);
            image4.setVisibility(View.GONE);
        }


        txt_discount_amount.setText(model.getDiscount());


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

//        //set click listener for views inside of dialog
//        btn_cancel_dialog.setOnClickListener(view -> {
//            listener.onDeniedButtonClicked(false);
//            dialog.dismiss();
//        });

        img_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void createCalendarDialog(DialogFactoryInteraction listener, View view) {
        View customLayout = LayoutInflater.from(context).inflate(R.layout.calendar_dialog, (ViewGroup) view, false);

        //define views inside of dialog
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        Button btn_register = customLayout.findViewById(R.id.btn1);
        Button btn_todayDate = customLayout.findViewById(R.id.btn2);
        NumberPicker np_year = customLayout.findViewById(R.id.np_year);
        NumberPicker np_month = customLayout.findViewById(R.id.np_month);
        NumberPicker np_day = customLayout.findViewById(R.id.np_day);

        SolarCalendar calendar = new SolarCalendar();

        int lastYear_ = (Integer.parseInt(calendar.getCurrentShamsiYear())) - 1;
        int currentYear_ = Integer.parseInt(calendar.getCurrentShamsiYear());
        int nextYear_ = (Integer.parseInt(calendar.getCurrentShamsiYear())) + 1;

        String lastYear = String.valueOf(lastYear_);
        String currentYear = String.valueOf(currentYear_);
        String nextYear = String.valueOf(nextYear_);

        np_year.setDisplayedValues(new String[]{ConvertEnDigitToFa.convert(lastYear)
                , ConvertEnDigitToFa.convert(currentYear), ConvertEnDigitToFa.convert(nextYear)});

        np_month.setDisplayedValues(new String[]{"۰۱", "۰۲", "۰۳", "۰۴", "۰۵", "۰۶", "۰۷", "۰۸", "۰۹", "۱۰", "۱۱", "۱۲"});

        np_day.setDisplayedValues(new String[]{"۰۱", "۰۲", "۰۳", "۰۴", "۰۵", "۰۶", "۰۷", "۰۸", "۰۹", "۱۰", "۱۱", "۱۲",
                "۱۳", "۱۴", "۱۵", "۱۶", "۱۷", "۱۸", "۱۹", "۲۰", "۲۱", "۲۲", "۲۳", "۲۴",
                "۲۵", "۲۶", "۲۷", "۲۸", "۲۹", "۳۰", "۳۱"});

        int year = Integer.parseInt(calendar.getCurrentShamsiYear());
        int month = Integer.parseInt(calendar.getCurrentShamsiMonth());
        int day = Integer.valueOf(calendar.getCurrentShamsiDay());


        np_year.setMinValue(currentYear_ - 1);
        np_year.setMaxValue(currentYear_ + 1);

        np_month.setMinValue(1);
        np_month.setMaxValue(12);

        np_day.setMinValue(1);
        np_day.setMaxValue(31);

//        //init set
        np_year.setValue(year);
        np_month.setValue(month);
        np_day.setValue(day);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_todayDate.setOnClickListener(v -> {
            //init set
            np_year.setValue(year);
            np_month.setValue(month);
            np_day.setValue(day);
        });

        btn_register.setOnClickListener(v -> {

            String year1 = ConvertEnDigitToFa.convert(String.valueOf(np_year.getValue()));
            String month1 = ConvertEnDigitToFa.convert(String.valueOf(np_month.getValue()));
            String month2 = (String.format("%s", month1.length() < 2 ? "۰" + month1 : month1));
            String day1 = ConvertEnDigitToFa.convert(String.valueOf(np_day.getValue()));
            String day2 = (String.format("%s", day1.length() < 2 ? "۰" + day1 : day1));

            String date = year1 + "/" + month2 + "/" + day2;

//            String date = ConvertEnDigitToFa.convert(year+"/"+month+"/"+day) ;
            listener.onAcceptButtonClicked(date);
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }


    public void createProfileMemberDetailDialog(@NotNull DialogFactoryInteraction listener,
                                                @Nullable View view, @NotNull MemberDetail member_detail) {


        View customLayout = LayoutInflater.from(context).inflate(R.layout.profile_member_detail_dialog, (ViewGroup) view, false);

        //define views inside of dialog
        Button btn_close = customLayout.findViewById(R.id.btn_close);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        RecyclerView recyclerView = customLayout.findViewById(R.id.rv_profile_member_detail);


        btn_close.setText(context.getResources().getString(R.string.close));

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        //set recyclerview
        ProfileMemberDetailAdapter adapter;
        List<MemberDetailObj> memberDetailObj = new ArrayList<>();
        memberDetailObj.addAll(member_detail.getData());


        adapter = new ProfileMemberDetailAdapter(memberDetailObj, view.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        btn_close.setOnClickListener(v -> dialog.dismiss());
        img_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    public void createChangeProfileDialog(@NotNull DialogFactoryInteraction listener, @Nullable View view) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.report_issue_dialog, (ViewGroup) view, false);

        //define views inside of dialog
        Button btn_close = customLayout.findViewById(R.id.btn_cancel_dialog);
        Button btn_send = customLayout.findViewById(R.id.btn_send_dialog);
        EditText edt_body = customLayout.findViewById(R.id.edt_description);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);

        RecyclerView recyclerView = customLayout.findViewById(R.id.rv_profile_member_detail);
        txt_header.setText(context.getResources().getString(R.string.request_profile_change));
        btn_close.setText(context.getResources().getString(R.string.close));
        btn_send.setText(context.getResources().getString(R.string.send));

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        btn_send.setOnClickListener(v -> {
            String body = edt_body.getText().toString();
            if (body.length() == 0) {
                Toast.makeText(context, context.getResources().getString(R.string.fillup_profile_changes), Toast.LENGTH_SHORT).show();
            } else {
                listener.onAcceptButtonClicked(body);
                dialog.dismiss();
            }

        });


        btn_close.setOnClickListener(v -> dialog.dismiss());
        img_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }


    public void createUpdateDialog(@NotNull DialogFactoryInteraction listener, @Nullable View view, String update_type) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog2, (ViewGroup) view, false);

        //define views inside of dialog

        Button btn_send = customLayout.findViewById(R.id.btn);
        TextView txt_body = customLayout.findViewById(R.id.txt_description);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);


        txt_body.setText(context.getResources().getString(R.string.update_body_dialog));
        btn_send.setText(context.getResources().getString(R.string.update_app));

        if (update_type.equals("force_update")) {
            img_close.setVisibility(View.GONE);
            txt_header.setText(context.getResources().getString(R.string.force_update));
        } else if (update_type.equals("optional_update")) {
            img_close.setVisibility(View.VISIBLE);
            txt_header.setText(context.getResources().getString(R.string.optional_update));
        }


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);
        if (update_type.equals("force_update")) {
            builder.setCancelable(false);
        } else if (update_type.equals("optional_update")) {
            builder.setCancelable(true);
        }


        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        btn_send.setOnClickListener(v -> {
            listener.onAcceptButtonClicked();
            dialog.dismiss();

        });


        img_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }


    public void createInfoMemberPrizeDialog(@NotNull DialogFactoryInteraction listener, @Nullable View view, String info_type) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog2, (ViewGroup) view, false);

        //define views inside of dialog
        Button btn_cancel = customLayout.findViewById(R.id.btn);
        TextView txt_body = customLayout.findViewById(R.id.txt_description);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);

        btn_cancel.setText(context.getResources().getString(R.string.close));


        switch (info_type) {
            case "member_info_new_register":
                txt_body.setText("افرادی از خانواده که جهت خرید با هم به فروشگاه مراجعه کرده اند.");
                txt_header.setText(context.getResources().getString(R.string.familymember));
                break;
            case "prize_info_new_register":
                txt_body.setText("جوایزی می باشد که فروشگاه برای خرید(و نه کالا) در نظر گرفته است.");
                txt_header.setText(context.getResources().getString(R.string.prize_selection));
                break;
            case "member_info_edit_product_detail":
                txt_body.setText("افرادی از خانواده که جهت خرید با هم به فروشگاه مراجعه کرده اند.");
                txt_header.setText(context.getResources().getString(R.string.familymember));
                break;
            case "prize_info_edit_product_detail":
                txt_body.setText("جوایزی می باشد که فروشگاه برای خرید(و نه کالا) در نظر گرفته است.");
                txt_header.setText(context.getResources().getString(R.string.prize_selection));
                break;
            case "member_info_purchased_item":
                txt_body.setText("کسانی که در خانواده از این کالا استفاده می کنند.");
                txt_header.setText(context.getResources().getString(R.string.consumer));

                break;
            case "prize_info_purchased_item":
                txt_body.setText("جوایزی که برای این کالا در نظر گرفته شده است");
                txt_header.setText(context.getResources().getString(R.string.prize_selection));
                break;
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    @SuppressLint("ClickableViewAccessibility")
    public void  createSearchableDialog(@NotNull DialogFactoryInteraction listener, @Nullable View view,
                                        List<SearchModel> searchList, NewRegisterActivity newRegisterActivity) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.searchable_dialog, (ViewGroup) view, false);

        //define views inside of dialog
        RecyclerView recyclerView = customLayout.findViewById(R.id.rv_searchable);
        SearchView searchView = customLayout.findViewById(R.id.searchView);




        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);


        searchEditText.setHintTextColor(context.getResources().getColor(R.color.colorText));
        ImageView imvClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        imvClose.setImageResource(R.drawable.ic_close);

        // delete icon search
        searchView.setIconifiedByDefault(false);





        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }





        //set recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        SearchAdapter adapter =new SearchAdapter(searchList,view.getContext(),dialog);
        adapter.setListener(newRegisterActivity);
        recyclerView.setAdapter(adapter);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                adapter.getFilter().filter(query);
                return false;
            }
        });



        dialog.show();
    }



    public void  createShoppingProductDetailDialog(DialogFactoryInteraction param, RelativeLayout view, EditProducts model) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.searchable_dialog, (ViewGroup) view, false);



        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }




        dialog.show();
    }



}
