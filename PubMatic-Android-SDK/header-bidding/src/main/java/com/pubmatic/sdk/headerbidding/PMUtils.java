package com.pubmatic.sdk.headerbidding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import java.security.MessageDigest;

/**
 * Created by Sagar on 3/23/2017.
 */

public class PMUtils {

    public static String getNetworkType(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= cm.getActiveNetworkInfo();
        if(networkInfo!=null) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    return "cellular";
                case ConnectivityManager.TYPE_WIFI:
                    return "wifi";
                default:
                    return null;
            }
        }
        return null;
    }

    public static String getUdidFromContext(Context context) {
        String deviceId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId = (deviceId == null) ? "" : sha1(deviceId);
        return deviceId;

    }

    public static String md5(String string) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = string.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            for (final byte b : bytes) {
                stringBuilder.append(String.format("%02X", b));
            }

            return stringBuilder.toString().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    @SuppressLint("DefaultLocale")
    public static String sha1(String string) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = string.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            for (final byte b : bytes) {
                stringBuilder.append(String.format("%02X", b));
            }

            return stringBuilder.toString().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

}
