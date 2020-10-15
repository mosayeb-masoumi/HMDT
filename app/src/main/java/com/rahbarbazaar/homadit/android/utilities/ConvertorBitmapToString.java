package com.rahbarbazaar.homadit.android.utilities;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ConvertorBitmapToString {
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
