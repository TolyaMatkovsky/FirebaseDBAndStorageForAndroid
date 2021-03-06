package com.example.app.firabasedbandstorageforandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.app.firabasedbandstorageforandroid.AppInterface;
import com.example.app.firabasedbandstorageforandroid.R;
import com.example.app.firabasedbandstorageforandroid.model.Section;
import com.example.app.firabasedbandstorageforandroid.manager.AlertManager;
import com.example.app.firabasedbandstorageforandroid.manager.ToolsManager;
import com.example.app.firabasedbandstorageforandroid.repository.SectionRepositoryImpl;
import com.example.app.firabasedbandstorageforandroid.repository.interfaces.SectionRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Tolik on 31.12.2016.
 */

public class AddSectionFragment extends BaseFragment {

    @BindView(R.id.et_section_name)
    EditText sectionName;
    @BindView(R.id.et_message_box)
    EditText messageBox;

    private AppInterface mainActivity;
    private Unbinder unbinder;
    private SectionRepository sectionRepository;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (AppInterface) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_section_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        this.sectionRepository = new SectionRepositoryImpl(mainActivity.getFireBaseDB());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sectionName.setText("");
        messageBox.setText("");

        mainActivity.hideSaveButton(false);
        mainActivity.setToolBarTitle(getResString(R.string.add));
        mainActivity.setToolBarRightButtonStyle(R.style.root_wrap_AddSectionStyle);
        mainActivity.setToolBarRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolsManager.checkWifiOrMobileInternet((Context) mainActivity)) {
                    if (validateData()) {
                        checkIfExistSection(sectionName.getText().toString().trim());
                    } else {
                        AlertManager.showToast((Context) mainActivity, getResString(R.string.fields));
                    }
                } else {
                    showProblemWithInternet();
                }
            }
        });

    }

    private void saveSectionInDB() {
        final SweetAlertDialog progressDialog = AlertManager.showSweetProgressBar((Context) mainActivity, getResString(R.string.saving));
        progressDialog.show();

        final SweetAlertDialog successDialog = AlertManager.showSuccessResult((Context) mainActivity, getResString(R.string.add_section))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        mainActivity.onBackPressed();
                    }
                });

        sectionRepository.saveSectionInDB(sectionName.getText().toString().trim(), messageBox.getText().toString().trim(),
                ToolsManager.getCurrentDate(), "",
                new SectionRepository.SimpleCallBack() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                        successDialog.show();
                    }

                    @Override
                    public void onError() {
                        showError(progressDialog);
                    }
                });

    }

    private void checkIfExistSection(final String sectionName) {
        final SweetAlertDialog progressDialog = AlertManager.showSweetProgressBar((Context) mainActivity, getResString(R.string.check));
        progressDialog.show();

        sectionRepository.getAllSectionFromDB(new SectionRepository.CallBack() {
            @Override
            public void onSuccess(List<Section> sections) {
                boolean isExist = false;
                for (Section s : sections) {
                    if (s.getName().equalsIgnoreCase(sectionName)) {
                        isExist = true;
                        break;
                    }
                }
                if (isExist) {
                    progressDialog.dismiss();
                    AlertManager.showSweetWarning((Context) mainActivity, getResString(R.string.exist), getResString(R.string.please_input));
                } else {
                    progressDialog.dismiss();
                    saveSectionInDB();
                }
            }

            @Override
            public void onError() {
                showError(progressDialog);
            }
        });
    }

    private boolean validateData() {
        if (!sectionName.getText().toString().trim().isEmpty() && !messageBox.getText().toString().trim().isEmpty()) {
            return true;
        }
        return false;
    }

    @OnClick(R.id.add_section_view)
    public void hideKeyBoard() {
        ToolsManager.hideKeyboard((Context) mainActivity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder = null;
        }
        mainActivity.hideSaveButton(true);
    }

}
