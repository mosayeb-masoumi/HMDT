package com.rahbarbazaar.homadit.android.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.BarcodeListAdapter;
import com.rahbarbazaar.homadit.android.controllers.adapters.ProfileMemberDetailAdapter;
import com.rahbarbazaar.homadit.android.controllers.adapters.SearchAdapter;
import com.rahbarbazaar.homadit.android.controllers.adapters.ShoppingProductsDetailAdapter;
import com.rahbarbazaar.homadit.android.models.api_error.APIError422;
import com.rahbarbazaar.homadit.android.models.api_error.ErrorUtils;
import com.rahbarbazaar.homadit.android.models.barcodlist.Barcode;
import com.rahbarbazaar.homadit.android.models.barcodlist.BarcodeData;
import com.rahbarbazaar.homadit.android.models.dashboard.dashboard_create.DashboardCreateData;
import com.rahbarbazaar.homadit.android.models.history.History;
import com.rahbarbazaar.homadit.android.models.profile.MemberDetail;
import com.rahbarbazaar.homadit.android.models.profile.MemberDetailObj;
import com.rahbarbazaar.homadit.android.models.searchable.SearchModel;
import com.rahbarbazaar.homadit.android.models.shopping_product.ShoppingProductList;
import com.rahbarbazaar.homadit.android.models.transaction.Transaction;
import com.rahbarbazaar.homadit.android.network.Service;
import com.rahbarbazaar.homadit.android.network.ServiceProvider;
import com.rahbarbazaar.homadit.android.ui.activities.NewRegisterActivity;
import com.rahbarbazaar.homadit.android.ui.activities.PurchasedItemActivity;
import com.rahbarbazaar.homadit.android.ui.activities.QRcodeActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            // to show keyboard automatically while editText is in  dialog
            dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    public void createOutOfAreaDialog2(DialogFactoryInteraction listener, DrawerLayout view) {
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

        btn_close.setOnClickListener(v -> {
            listener.onAcceptButtonClicked();
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void createError406Dialog2(DialogFactoryInteraction listener, DrawerLayout view, String message) {
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
            // to show keyboard automatically while editText is in  dialog
            dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        TextView txt_header = customLayout.findViewById(R.id.txt_header_historydetail);
        TextView txt_shop = customLayout.findViewById(R.id.txt_shop);
        TextView txt_purchased_date = customLayout.findViewById(R.id.txt_purchased_date);
        TextView txt_register_date = customLayout.findViewById(R.id.txt_register_date);
        TextView txt_total_purchased_amount = customLayout.findViewById(R.id.txt_total_purchased_amount);

        TextView txt_members = customLayout.findViewById(R.id.txt_members);
        CardView cardview1 = customLayout.findViewById(R.id.cardview1);
        CardView cardview2 = customLayout.findViewById(R.id.cardview2);
        CardView cardview3 = customLayout.findViewById(R.id.cardview3);
        CardView cardview4 = customLayout.findViewById(R.id.cardview4);
        ImageView image1 = customLayout.findViewById(R.id.image1);
        ImageView image2 = customLayout.findViewById(R.id.image2);
        ImageView image3 = customLayout.findViewById(R.id.image3);
        ImageView image4 = customLayout.findViewById(R.id.image4);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        txt_header.setText("جزئیات خرید");
        txt_shop.setText(model.getTitle());
        txt_purchased_date.setText(model.getDate());
        txt_register_date.setText(model.getCreatedAt());
        txt_total_purchased_amount.setText(model.getCost());
        txt_members.setText(model.getShoppingMember());

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


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

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

//        np_month.setDisplayedValues(new String[]{"۰۱", "۰۲", "۰۳", "۰۴", "۰۵", "۰۶", "۰۷", "۰۸", "۰۹", "۱۰", "۱۱", "۱۲"});
        np_month.setDisplayedValues(new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"});

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


            if(np_year.getValue() > year){
                Toast.makeText(context, "تاریخ مربوط به آینده می باشد!", Toast.LENGTH_SHORT).show();
            }else if(np_month.getValue() > month){
                Toast.makeText(context, "تاریخ مربوط به آینده می باشد!", Toast.LENGTH_SHORT).show();
            }else if(np_day.getValue() > day){
                Toast.makeText(context, "تاریخ مربوط به آینده می باشد!", Toast.LENGTH_SHORT).show();
            }else{
                listener.onAcceptButtonClicked(date);
                dialog.dismiss();
            }

//            String date = ConvertEnDigitToFa.convert(year+"/"+month+"/"+day) ;


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
            // to show keyboard automatically while editText is in  dialog
            dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
//                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);



//        edt_body.requestFocus();
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(edt_body, InputMethodManager.SHOW_IMPLICIT);




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

            case "member_info_orderer":
                txt_body.setText("کسی که به صورت آنلاین سفارش خرید داده است.");
                txt_header.setText(context.getResources().getString(R.string.orderer));
                break;


            case "prize_info_new_register":
                txt_body.setText("جوایزی می باشد که فروشگاه برای خرید(و نه کالا) در نظر گرفته است.");
                txt_header.setText(context.getResources().getString(R.string.prize_selection));
                break;
            case "member_info_edit_product_detail":
                txt_body.setText("افرادی از خانواده که جهت خرید با هم به فروشگاه مراجعه کرده اند.");
                txt_header.setText(context.getResources().getString(R.string.consumers));
                break;
            case "prize_info_edit_product_detail":
                txt_body.setText("جوایزی می باشد که فروشگاه برای خرید(و نه کالا) در نظر گرفته است.");
                txt_header.setText(context.getResources().getString(R.string.prize_selection));
                break;
            case "member_info_purchased_item":
                txt_body.setText("کسانی که در خانواده از این کالا استفاده می کنند.");
                txt_header.setText(context.getResources().getString(R.string.consumers));

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
    public void createSearchableDialog(@NotNull DialogFactoryInteraction listener, @Nullable View view,
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

        Button btn_close = customLayout.findViewById(R.id.btn_close_search_dialog);


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
        SearchAdapter adapter = new SearchAdapter(searchList, view.getContext(), dialog, "searchable");
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
//                adapter.getFilter().filter(query);
                return false;
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        btn_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void createShoppingProductDetailDialog(@NotNull DialogFactoryInteraction listener,
                                                  @Nullable RelativeLayout view, @NotNull ShoppingProductList model) {
        View customLayout = LayoutInflater.from(context).inflate(R.layout.shopping_product_detail_dialog, (ViewGroup) view, false);

        //define views inside of dialog
        RecyclerView recyclerView = customLayout.findViewById(R.id.rv_shopping_products_detail);
        ImageView img_close = customLayout.findViewById(R.id.img_close);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        img_close.setOnClickListener(v -> dialog.dismiss());
//        set recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ShoppingProductsDetailAdapter adapter = new ShoppingProductsDetailAdapter(model.getDetail(), view.getContext());
        recyclerView.setAdapter(adapter);
        dialog.show();
    }


    //    public void createQrcodeInfoBtnsDialog(DialogFactoryInteraction dialogFactoryInteraction, LinearLayout view, String description, String name) {
    public void createQrcodeInfoBtnsDialog(DialogFactoryInteraction listener, View view, String description, String name) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog2, (ViewGroup) view, false);
        //define views inside of dialog
        Button btn_close = customLayout.findViewById(R.id.btn);
        TextView txt_body = customLayout.findViewById(R.id.txt_description);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_close.setText(context.getResources().getString(R.string.close));
        txt_body.setText(description);
        txt_header.setText(name);

        btn_close.setOnClickListener(v -> dialog.dismiss());
        img_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    public void createBarcodeResultListDialog(DialogFactoryInteraction listener, LinearLayout view, Barcode barcode,
                                              QRcodeActivity qRcodeActivity) {
        View customLayout = LayoutInflater.from(context).inflate(R.layout.barcode_result_list_dialog, (ViewGroup) view, false);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        RecyclerView recyclerView = customLayout.findViewById(R.id.rv_barcodeListDialog);
        Button btn_close = customLayout.findViewById(R.id.btn_close);
        Button btn_new_registeration = customLayout.findViewById(R.id.btn_new_register_barcode_list_dialog);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        List<BarcodeData> barcodeList = new ArrayList<>();

        for (int i = 0; i < barcode.getData().size(); i++) {

            barcodeList.add(new BarcodeData(barcode.getData().get(i).getId(), barcode.getData().get(i).getMygroup(),
                    barcode.getData().get(i).getMinPrice(), barcode.getData().get(i).getMaxPrice(),
                    barcode.getData().get(i).getMaxAmount(),
                    barcode.getData().get(i).getBarcode(), barcode.getData().get(i).getDecription(),
                    barcode.getData().get(i).getUnit(), barcode.getData().get(i).getBarcodeDetail()));
        }

        //set recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        BarcodeListAdapter adapter = new BarcodeListAdapter(barcodeList, context, dialog, barcode);
        adapter.setListener(qRcodeActivity);
        recyclerView.setAdapter(adapter);

        btn_new_registeration.setOnClickListener(v -> {
            listener.onAcceptButtonClicked();
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
//            listener.onDeniedButtonClicked(false);
        });

        btn_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    public void createBarcodeResultListUnreadableDialog(DialogFactoryInteraction listener, RelativeLayout view, Barcode barcode,
                                                        PurchasedItemActivity purchasedItemActivity) {
        View customLayout = LayoutInflater.from(context).inflate(R.layout.barcode_result_list_dialog, (ViewGroup) view, false);

        ImageView img_close = customLayout.findViewById(R.id.img_close);
        RecyclerView recyclerView = customLayout.findViewById(R.id.rv_barcodeListDialog);

        Button btn_close = customLayout.findViewById(R.id.btn_close);
        Button btn_new_registeration = customLayout.findViewById(R.id.btn_new_register_barcode_list_dialog);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        List<BarcodeData> barcodeList = new ArrayList<>();

        for (int i = 0; i < barcode.getData().size(); i++) {
            barcodeList.add(new BarcodeData(barcode.getData().get(i).getId(), barcode.getData().get(i).getMygroup(),
                    barcode.getData().get(i).getMinPrice(), barcode.getData().get(i).getMaxPrice(),
                    barcode.getData().get(i).getMaxAmount(),
                    barcode.getData().get(i).getBarcode(), barcode.getData().get(i).getDecription(),
                    barcode.getData().get(i).getUnit(), barcode.getData().get(i).getBarcodeDetail()));
        }

        //set recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        BarcodeListAdapter adapter = new BarcodeListAdapter(barcodeList, context, dialog, barcode);
        adapter.setListener(purchasedItemActivity);
        recyclerView.setAdapter(adapter);

        btn_new_registeration.setOnClickListener(v -> {
            listener.onAcceptButtonClicked();
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
            listener.onDeniedButtonClicked(false);
        });

        btn_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    public void createSpnListDialog(DialogFactoryInteraction listner, RelativeLayout view,
                                    List<SearchModel> searchList, PurchasedItemActivity purchasedItemActivity,
                                    String spn_name, String title) {
        View customLayout = LayoutInflater.from(context).inflate(R.layout.spn_search_dialog, (ViewGroup) view, false);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        TextView txt_explanation = customLayout.findViewById(R.id.txt_explanation);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        RecyclerView recyclerView = customLayout.findViewById(R.id.rv_spinner);
        Button btn_close = customLayout.findViewById(R.id.btn_exit_dialog_shop);
        Button btn_scan = customLayout.findViewById(R.id.btn_exit_dialog_shop1);
        Button btn_etc = customLayout.findViewById(R.id.btn_etc);
        LinearLayout ll_explanation = customLayout.findViewById(R.id.ll_explanation);
        LinearLayout ll_etc = customLayout.findViewById(R.id.ll_etc);

        SearchView searchView = customLayout.findViewById(R.id.search_view);
        searchView.setQueryHint("جستجو...");
        EditText searchEditText = searchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(Color.GRAY);

        // change close icon (color shape ...)
        ImageView imvClose = searchView.findViewById(R.id.search_close_btn);
        imvClose.setImageResource(R.drawable.ic_close);

        // delete icon search
        searchView.setIconifiedByDefault(false);

        if(spn_name.equals("spn_brand"))
            searchView.setVisibility(View.VISIBLE);
        else
            searchView.setVisibility(View.GONE);





        if (spn_name.equals("spn_group")) {
            ll_etc.setVisibility(View.GONE);
            ll_explanation.setVisibility(View.VISIBLE);
            txt_explanation.setVisibility(View.VISIBLE);
            btn_scan.setText("ثبت کالای جدید");
        } else {
            ll_etc.setVisibility(View.VISIBLE);
            btn_scan.setVisibility(View.GONE);
            ll_explanation.setVisibility(View.GONE);
            txt_explanation.setVisibility(View.GONE);
        }



        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        txt_header.setText(title);
        txt_explanation.setText("اگر کالای شما در لیست فوق نیست کالای جدید را اسکن نمایید");

        //set recyclerview
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        SearchAdapter adapter = new SearchAdapter(searchList, view.getContext(), dialog, spn_name);
        adapter.setListener(purchasedItemActivity);
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





        btn_etc.setOnClickListener(v -> {
            listner.onAcceptButtonClicked(spn_name, title);
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_close.setOnClickListener(v -> dialog.dismiss());
        btn_scan.setOnClickListener(v -> {
            context.startActivity(new Intent(context, QRcodeActivity.class));
            dialog.dismiss();
        });
        dialog.show();
    }

    public void createOnline_Present_PurchaseListDialog(DialogFactoryInteraction listener, RelativeLayout view,
                                                        List<SearchModel> searchList, NewRegisterActivity newRegisterActivity, String spn_name) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.spn_search_dialog, (ViewGroup) view, false);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        RecyclerView recyclerView = customLayout.findViewById(R.id.rv_spinner);
        Button btn_close = customLayout.findViewById(R.id.btn_exit_dialog_shop1);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);

        SearchView searchView = customLayout.findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);

        if(spn_name.equals("online")){
            txt_header.setText("لیست فروشگاههای آنلاین");
        }else if(spn_name.equals("present")){
            txt_header.setText("لیست فروشگاههای حضوری/تلفنی");
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //set recyclerview
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        SearchAdapter adapter = new SearchAdapter(searchList, view.getContext(), dialog, spn_name);
        adapter.setListener(newRegisterActivity);
        recyclerView.setAdapter(adapter);

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    public void createEtcSpnListDialog(DialogFactoryInteraction listener, RelativeLayout view,
                                       PurchasedItemActivity purchasedItemActivity, String spn_nam, String dialog_title) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.report_issue_dialog, (ViewGroup) view, false);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        EditText edt_description = customLayout.findViewById(R.id.edt_description);
        Button btn_close = customLayout.findViewById(R.id.btn_cancel_dialog);
        Button btn_register = customLayout.findViewById(R.id.btn_send_dialog);

        btn_register.setText("ثبت");

        txt_header.setText(dialog_title);
        edt_description.setHint(("لطفا عنوان ") + (dialog_title)+ (" را بصورت کامل تایپ کنید"));
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // to show keyboard automatically while editText is in  dialog
            dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        img_close.setOnClickListener(v -> dialog.dismiss());
        btn_close.setOnClickListener(v -> dialog.dismiss());

        btn_register.setOnClickListener(v -> {

            dialog.dismiss();
            String description = edt_description.getText().toString();
            listener.onAcceptButtonClicked(description);

        });

        dialog.show();
    }


    public void createImageInfoDialog(DialogFactoryInteraction listener, RelativeLayout view,
                                      String img1_link, String img2_link) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.image_info_dialog, (ViewGroup) view, false);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        Button btn_close = customLayout.findViewById(R.id.btn_close);
        TextView txt_img1_info = customLayout.findViewById(R.id.txt_img1_info);
        TextView txt_img2_info = customLayout.findViewById(R.id.txt_img2_info);
        ImageView img1_info = customLayout.findViewById(R.id.img1_info);
        ImageView img2_info = customLayout.findViewById(R.id.img2_info);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);
        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Glide.with(Objects.requireNonNull(context)).load(img1_link).centerCrop().into(img1_info);
        Glide.with(Objects.requireNonNull(context)).load(img2_link).centerCrop().into(img2_info);

        txt_img1_info.setText("عکس از نمای بالا");
        txt_img2_info.setText("عکس از نمای روبرو");
        txt_header.setText("راهنمای عکس");
        img_close.setOnClickListener(v -> dialog.dismiss());
        btn_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    public void createTransactionItemDialog(DialogFactoryInteraction listener, LinearLayout view,
                                            Transaction model, int position) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.transaction_info_dialog, (ViewGroup) view, false);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        Button btn_close = customLayout.findViewById(R.id.btn_close);

        TextView txt_title = customLayout.findViewById(R.id.txt_title);
        TextView txt_type = customLayout.findViewById(R.id.txt_type);
        TextView txt_date = customLayout.findViewById(R.id.txt_date);
        TextView txt_time = customLayout.findViewById(R.id.txt_time);
        TextView txt_origin = customLayout.findViewById(R.id.txt_origin);
        TextView txt_amount = customLayout.findViewById(R.id.txt_amount);
        TextView txt_extra = customLayout.findViewById(R.id.txt_extra);

        txt_title.setText(model.title);
        txt_type.setText(model.type);
        txt_date.setText(model.date);
        txt_time.setText(model.time);
        txt_origin.setText(model.origin);
        txt_amount.setText(model.amount);
        txt_extra.setText(model.extra);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);
        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        img_close.setOnClickListener(v -> dialog.dismiss());
          btn_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    public void createConvertPapasiTomanDialog(DialogFactoryInteraction listener, View view) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog3, (ViewGroup) view, false);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        EditText edt_description = customLayout.findViewById(R.id.edt_description);
        Button btn_register = customLayout.findViewById(R.id.btn);
        AVLoadingIndicatorView avi_convert_papasi = customLayout.findViewById(R.id.avi_convert_papasi);


        avi_convert_papasi.setVisibility(View.GONE);
        btn_register.setVisibility(View.VISIBLE);

        txt_header.setText(context.getResources().getString(R.string.papasi_to_rial));
//        edt_description.setHint(Html.fromHtml(
//                "<small>"
//                        + context.getResources().getString(R.string.insert_papasi_hint)
//                        + "</small>"));

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "BYekan.ttf");
        edt_description.setTypeface(tf);


        edt_description.setTextSize(17);
        edt_description.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt_description.setHint(context.getResources().getString(R.string.insert_papasi_hint));

        btn_register.setText(context.getResources().getString(R.string.register));

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);
        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        img_close.setOnClickListener(v -> dialog.dismiss());


        // to limit characters
            edt_description.setFilters(new InputFilter[] {
                    new InputFilter.LengthFilter(15)
            });

        btn_register.setOnClickListener(view1 -> {

            avi_convert_papasi.setVisibility(View.VISIBLE);
            btn_register.setVisibility(View.GONE);
            String digits = (edt_description.getText().toString());


            long papasi = Long.parseLong(digits);

            Service service = new ServiceProvider(context).getmService();
            Call<Void> call = service.covertPapasi(papasi);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code()==200){

//                        Toast.makeText(context, ""+context.getResources().getString(R.string.convert_succeed), Toast.LENGTH_SHORT).show();
                        requestDashboardData();

                    }else if(response.code()==422){
                        avi_convert_papasi.setVisibility(View.GONE);
                        btn_register.setVisibility(View.VISIBLE);
                        APIError422 apiError = ErrorUtils.parseError422(response);
                        StringBuilder builderAmount = null;

                        if (apiError.errors.amount != null) {
                            builderAmount = new StringBuilder();
                            for (String b : apiError.errors.amount) {
                                builderAmount.append("").append(b).append(" ");
                            }
                        }

                        String msg = builderAmount.toString();
                        listener.onAcceptButtonClicked("","",msg);

                    }else{
                        avi_convert_papasi.setVisibility(View.GONE);
                        btn_register.setVisibility(View.VISIBLE);
                        Toast.makeText(context, ""+context.getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();

                    }
                }



                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    avi_convert_papasi.setVisibility(View.GONE);
                    btn_register.setVisibility(View.VISIBLE);
                    Toast.makeText(context, ""+context.getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                }


                private void requestDashboardData() {
                    Service service = new ServiceProvider(context).getmService();
                    Call<DashboardCreateData> call = service.getDashboardData();
                    call.enqueue(new Callback<DashboardCreateData>() {
                        @Override
                        public void onResponse(Call<DashboardCreateData> call, Response<DashboardCreateData> response) {
                            if(response.code() == 200){
                                Toast.makeText(context, ""+context.getResources().getString(R.string.convert_succeed), Toast.LENGTH_LONG).show();
                                dialog.dismiss();

                                DashboardCreateData dashboardCreateData;
                                dashboardCreateData = response.body();
                                RxBus.DashboardModel.publishDashboardModel(dashboardCreateData);

                                listener.onAcceptButtonClicked(dashboardCreateData.data.one , dashboardCreateData.data.two,"");
                            }else {
                                Toast.makeText(context, ""+context.getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<DashboardCreateData> call, Throwable t) {
                            Toast.makeText(context, ""+context.getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();

                        }
                    });

                }



            });
        });




        dialog.show();
    }



    public void createEditAmountDialog(DialogFactoryInteraction listener, RelativeLayout view, String description) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog, (ViewGroup) view, false);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        TextView txt_description = customLayout.findViewById(R.id.txt_description);
        Button btn_edit = customLayout.findViewById(R.id.btn2);
        Button btn_scan = customLayout.findViewById(R.id.btn1);


        txt_header.setText(context.getResources().getString(R.string.system_error));
        btn_edit.setText(context.getResources().getString(R.string.update));
        btn_scan.setText("اسکن محصول بعدی");

        txt_description.setText(description);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);
        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        img_close.setOnClickListener(v -> dialog.dismiss());
        btn_edit.setOnClickListener(view1 -> dialog.dismiss());
        btn_scan.setOnClickListener(view12 -> listener.onAcceptButtonClicked());

        dialog.show();
    }



    public void createLottaryDialog(DialogFactoryInteraction listener, RelativeLayout view) {

        View customLayout = LayoutInflater.from(context).inflate(R.layout.sample_dialog3, (ViewGroup) view, false);
        ImageView img_close = customLayout.findViewById(R.id.img_close);
        TextView txt_header = customLayout.findViewById(R.id.txt_header);
        EditText edt_description = customLayout.findViewById(R.id.edt_description);
        Button btn_register = customLayout.findViewById(R.id.btn);


        txt_header.setText("ثبت پاپاسی");
        btn_register.setText("ثبت");
        edt_description.setHint("مقدار پاپاسی مورد نظر را وارد کنید");

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);
        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        btn_register.setOnClickListener(view1 -> {
            String description = edt_description.getText().toString();
            listener.onAcceptButtonClicked(description);
            dialog.dismiss();
        });

        img_close.setOnClickListener(v -> dialog.dismiss());


        dialog.show();
    }




}
