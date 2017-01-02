package com.example.app.firabasedbandstorageforandroid.manager;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tolik on 31.12.2016.
 */

public class ToolsManager {
    private static final String REUME_FLAG = "resume_flag";
    private static final String COUNT_IMAGES = "count_images";

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(((AppCompatActivity)context).getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ignored) {
        }
    }

    public static boolean checkWifiOrMobileInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }

    public static String getCurrentDate(){
        long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        return dateFormat.format(currentDate);
    }

    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setResumeFlag(boolean flag, Context context){
        getSharedPreference(context).edit().putBoolean(REUME_FLAG, flag).apply();
    }

    public static boolean getResumeFlag(Context context){
        return getSharedPreference(context).getBoolean(REUME_FLAG,false);
    }

    public static void setCountOfImages(int count, Context context){
        getSharedPreference(context).edit().putInt(COUNT_IMAGES, count).apply();
    }

    public static int getCountOfImages(Context context){
        return getSharedPreference(context).getInt(COUNT_IMAGES, 0);
    }
}
