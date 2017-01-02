package com.example.app.firabasedbandstorageforandroid.manager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.app.firabasedbandstorageforandroid.R;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Tolik on 31.12.2016.
 */

public class AlertManager {
    public static SweetAlertDialog showSweetProgressBar(Context context, String message){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        pDialog.setTitleText(message+"...");
        pDialog.setCancelable(false);
        return pDialog;
    }

    public static void showSweetWarning(Context context, String title, String message){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(context.getResources().getString(R.string.ok))
                .show();
    }

    public static void showSweetSuccessDialog(Context context, String title, String message){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(context.getResources().getString(R.string.ok))
                .show();
    }

    public static MaterialStyledDialog showInputImageNameDialog(Context context, View customView, MaterialDialog.SingleButtonCallback callback){
        return new MaterialStyledDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.image_name))
                .setDescription(context.getResources().getString(R.string.input_image))
                .setCustomView(customView, 30, 10, 30, 10)
                .setPositiveText(context.getResources().getString(R.string.ok))
                .onPositive(callback)
                .setCancelable(false)
                .build();
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static SweetAlertDialog showSuccessResult(Context context, String message){
        return  new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(context.getResources().getString(R.string.success))
                .setContentText(message)
                .setConfirmText(context.getResources().getString(R.string.ok));

    }
}
