package com.rahbarbazaar.checkpanel.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rahbarbazaar.checkpanel.R;
import com.rahbarbazaar.checkpanel.models.barcodlist.BarcodeData;
import com.rahbarbazaar.checkpanel.models.history.History;
import com.rahbarbazaar.checkpanel.ui.activities.MainActivity;
import com.rahbarbazaar.checkpanel.ui.activities.ScanActivity;

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

        //define views inside of dialog
        TextView txt_main = customLayout.findViewById(R.id.txt_main);
        TextView txt_category = customLayout.findViewById(R.id.txt_category);
        TextView txt_subCategory = customLayout.findViewById(R.id.txt_subCategory);
        TextView txt_brand = customLayout.findViewById(R.id.txt_brand);
        TextView txt_subBrand = customLayout.findViewById(R.id.txt_subBrand);
        TextView txt_owner = customLayout.findViewById(R.id.txt_owner);
        TextView txt_company = customLayout.findViewById(R.id.txt_company);
        TextView txt_country = customLayout.findViewById(R.id.txt_country);
        TextView txt_packaging = customLayout.findViewById(R.id.txt_packaging);
        TextView txt_unit = customLayout.findViewById(R.id.txt_unit);
        TextView txt_price = customLayout.findViewById(R.id.txt_price);
        TextView txt_type = customLayout.findViewById(R.id.txt_type);
        TextView txt_amount = customLayout.findViewById(R.id.txt_amount);
        TextView txt_description = customLayout.findViewById(R.id.txt_description);
        ImageView imageview = customLayout.findViewById(R.id.imageview);
        ImageView img_close = customLayout.findViewById(R.id.img_close);

        //set textes
        txt_main.setText(model.getMain());
        txt_category.setText(model.getCategory());
        txt_subCategory.setText(model.getSubCategory());
        txt_brand.setText(model.getBrand());
        txt_subBrand.setText(model.getSubBrand());
        txt_owner.setText(model.getOwner());
        txt_company.setText(model.getCompany());
        txt_country.setText(model.getCountry());
        txt_packaging.setText(model.getPackaging());
        txt_unit.setText(model.getUnit());
        txt_price.setText(model.getPrice());
        txt_type.setText(model.getType());
        txt_amount.setText(model.getAmount());
        txt_description.setText(model.getDecription());

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(customLayout);

        //create dialog and set background transparent
        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Glide.with(Objects.requireNonNull(context)).load(model.getImage()).centerCrop().into(imageview);
        img_close.setOnClickListener(v -> dialog.dismiss());

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

}
