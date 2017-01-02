package com.example.app.firabasedbandstorageforandroid.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.app.firabasedbandstorageforandroid.R;
import com.example.app.firabasedbandstorageforandroid.manager.AlertManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Tolik on 02.01.2017.
 */

public class BaseFragment extends Fragment{

    protected String getResString(int strRes){
        return getContext().getResources().getString(strRes);
    }

    protected void showError(SweetAlertDialog progressDialog){
        progressDialog.dismiss();
        AlertManager.showToast(getContext(), getResString(R.string.something_wrong));
    }

    protected void showProblemWithInternet(){
        AlertManager.showSweetWarning(getContext(), getResString(R.string.internet_problem), getResString(R.string.please_connect));
    }
}
