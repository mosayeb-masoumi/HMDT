package com.example.panelist.utilities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.panelist.R;

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

        View customLayout = LayoutInflater.from(context).inflate(R.layout.no_internet_dialog, (ViewGroup) root, false);
//        View customLayout = LayoutInflater.from(context).inflate(R.layout.no_internet_dialog, (ViewGroup) root, false);

        //define views inside of dialog
//        ImageView image_dialog = customLayout.findViewById(R.id.image_dialog);
        TextView text_body = customLayout.findViewById(R.id.text_body);
        TextView btn_wifi_dialog = customLayout.findViewById(R.id.btn_wifi_dialog);
        TextView btn_data_dialog = customLayout.findViewById(R.id.btn_data_dialog);

        //set default values of views
//        text_body.setText(R.string.no_internet_text);
//        btn_wifi_dialog.setText(R.string.internet_setting);
//        btn_data_dialog.setText(R.string.data_setting);
//        image_dialog.setImageResource(R.drawable.no_wifi);


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


        //if dialog dismissed, this action will be called
        dialog.setOnDismissListener(dialogInterface -> listener.onDeniedButtonClicked(true));

        dialog.show();
    }
}
