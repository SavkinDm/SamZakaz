package com.development.sdm.SamZakaz.SupportClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static android.graphics.Bitmap.CompressFormat.PNG;

public class ImageUtil {
    public static Bitmap convert(String base64Str) throws IllegalArgumentException {

        byte[] decodedBytes = Base64.decode( base64Str.substring(base64Str.indexOf(",") + 1), Base64.DEFAULT );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //bitmap.compress(PNG, 10, outputStream);
        bitmap.compress(JPEG, 10, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
