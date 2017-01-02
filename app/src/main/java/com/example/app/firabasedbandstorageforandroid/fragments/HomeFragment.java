package com.example.app.firabasedbandstorageforandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.firabasedbandstorageforandroid.AppInterface;
import com.example.app.firabasedbandstorageforandroid.R;
import com.example.app.firabasedbandstorageforandroid.model.Section;
import com.example.app.firabasedbandstorageforandroid.adapters.SectionRecycleViewAdapter;
import com.example.app.firabasedbandstorageforandroid.adapters.decoration.VerticalSpaceItemDecoration;
import com.example.app.firabasedbandstorageforandroid.manager.AlertManager;
import com.example.app.firabasedbandstorageforandroid.manager.ToolsManager;
import com.example.app.firabasedbandstorageforandroid.repository.SectionRepositoryImpl;
import com.example.app.firabasedbandstorageforandroid.repository.interfaces.SectionRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Tolik on 30.12.2016.
 */

public class HomeFragment extends BaseFragment {
    private static final int VERTICAL_ITEM_SPACE = 70;
    private static final int VERTICAL_SPACE_FROM_TOP = 70;

    @BindView(R.id.recycle_sections)
    RecyclerView recycle_sections;

    private AppInterface mainActivity;
    private Unbinder unbinder;
    private SectionRepository sectionRepository;
    private SectionRecycleViewAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (AppInterface) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        this.sectionRepository = new SectionRepositoryImpl(mainActivity.getFireBaseDB());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) mainActivity);
        recycle_sections.setLayoutManager(linearLayoutManager);
        recycle_sections.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE, VERTICAL_SPACE_FROM_TOP));

        adapter = new SectionRecycleViewAdapter((Context) mainActivity);
        adapter.setOnItemClickListener(new SectionRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onSectionItemClicked(Section section) {
                mainActivity.moveToAddNewImagesToSection(section);
            }

            @Override
            public void onSectionEditButtonClicked(Section section) {
                mainActivity.moveToEditSection(section);
            }
        });
        recycle_sections.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setToolBarTitle(getResString(R.string.sections));
        mainActivity.setToolBarLeftButtonStyle(true);
        mainActivity.setToolBarRightButtonStyle(R.style.root_wrap_MainStyle);
        mainActivity.setToolBarRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.moveToAddNewSection();
            }
        });
        if(ToolsManager.checkWifiOrMobileInternet((Context) mainActivity)){
            getAllSectionsFromDB();
        }else {
            showProblemWithInternet();
        }
    }

    private void getAllSectionsFromDB(){
        final SweetAlertDialog progressDialog = AlertManager.showSweetProgressBar((Context) mainActivity, getResString(R.string.loading));
        progressDialog.show();

        sectionRepository.getAllSectionFromDB(new SectionRepository.CallBack() {
            @Override
            public void onSuccess(List<Section> sections) {
                adapter.setSectionsCollection(sections);
                progressDialog.dismiss();
            }

            @Override
            public void onError() {
                showError(progressDialog);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null){
            unbinder = null;
        }
    }


}
