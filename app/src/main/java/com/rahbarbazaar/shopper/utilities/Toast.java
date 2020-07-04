package com.rahbarbazaar.shopper.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rahbarbazaar.shopper.R;

public class Toast {

    Context context;
    String message;
    public Toast(Context context ,String message) {

        this.context = context;
        this.message = message;

        showToast();
    }

    private void showToast() {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, ((Activity) context).findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.txt_toast);
        text.setText(message);
        android.widget.Toast toast = new android.widget.Toast((Activity) context);
//                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


}
