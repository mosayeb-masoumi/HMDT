package com.example.panelist.utilities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.panelist.R;
import com.example.panelist.models.barcodlist.BarcodeData;

public class DialogFactory {

    private Context context;




    public interface DialogFactoryInteraction{

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
        ImageView img_close  =customLayout.findViewById(R.id.img_close);

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
        img_close.setOnClickListener(v -> { dialog.dismiss();});


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
            if(desc.trim().length()>0){
                listener.onAcceptButtonClicked(desc,title,id);
                dialog.dismiss();

            }else{
                Toast.makeText(context, ""+context.getResources().getString(R.string.add_description), Toast.LENGTH_SHORT).show();
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
        ImageView img_close  =customLayout.findViewById(R.id.img_close);

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

    public void createbarcodeListDetailDialog(DialogFactoryInteraction listener, View view , BarcodeData model ) {

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
//        txt_subCategory.setText(model.getSubCategory());
        txt_brand.setText(model.getBrand());
//        txt_subBrand.setText(model.getSubBrand());
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





//        btn_scanner.setOnClickListener(v -> {
//            listener.onAcceptButtonClicked();
//        });
//
//        btn_search.setOnClickListener(v -> {
//            listener.onDeniedButtonClicked(false);
//        });
//
        img_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}
