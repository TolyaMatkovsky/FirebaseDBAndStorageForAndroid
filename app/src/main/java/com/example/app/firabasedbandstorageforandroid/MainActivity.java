package com.example.app.firabasedbandstorageforandroid;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.firabasedbandstorageforandroid.fragments.AddSectionFragment;
import com.example.app.firabasedbandstorageforandroid.fragments.DetailedSectionFragment;
import com.example.app.firabasedbandstorageforandroid.fragments.EditSectionFragment;
import com.example.app.firabasedbandstorageforandroid.fragments.HomeFragment;
import com.example.app.firabasedbandstorageforandroid.model.Section;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tolik on 30.12.2016.
 */

public class MainActivity extends AppCompatActivity implements AppInterface {

    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.toolbar_left_button)
    ImageView toolBarLeftButton;
    @BindView(R.id.toolbar_right_button)
    TextView toolBarRightButton;
    @BindView(R.id.save_button)
    TextView saveButton;

    private FragmentManager fragmentManager;
    private Fragment homeFragment, addSectionFragment, editSectionFragment, detailedSectionFragment;
    private Stack<Fragment> fragmentStack;


    private FirebaseDatabase dataBase;
    private FirebaseStorage storage;
    private String imageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();
        fragmentStack = new Stack<>();

        dataBase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        homeFragment = new HomeFragment();
        addSectionFragment = new AddSectionFragment();
        editSectionFragment = new EditSectionFragment();
        detailedSectionFragment = new DetailedSectionFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root, homeFragment);
        fragmentStack.push(homeFragment);
        fragmentTransaction.commit();


    }

    @Override
    public void setToolBarTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void setToolBarRightButtonListener(View.OnClickListener listener) {
        toolBarRightButton.setOnClickListener(listener);
        saveButton.setOnClickListener(listener);
    }

    @Override
    public void setToolBarLeftButtonListener(View.OnClickListener listener) {
        toolBarLeftButton.setOnClickListener(listener);
    }

    @Override
    public void setToolBarLeftButtonStyle(boolean hiddenFlag) {
        if (hiddenFlag) {
            toolBarLeftButton.setVisibility(View.GONE);
        } else {
            toolBarLeftButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setToolBarRightButtonStyle(int styleRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolBarRightButton.setTextAppearance(styleRes);
        } else {
            toolBarRightButton.setTextAppearance(this, styleRes);
        }
    }

    @Override
    public void hideSaveButton(boolean flag) {
        if(flag){
            saveButton.setVisibility(View.GONE);
            toolBarRightButton.setVisibility(View.VISIBLE);
        }else {
            saveButton.setVisibility(View.VISIBLE);
            toolBarRightButton.setVisibility(View.GONE);
        }
    }

    @Override
    public FirebaseDatabase getFireBaseDB() {
        return dataBase;
    }

    @Override
    public FirebaseStorage getFireBaseStorage() {
        return storage;
    }

    @Override
    public void moveToAddNewSection() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root, addSectionFragment);
        fragmentStack.push(addSectionFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void moveToEditSection(Section section) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle arg = new Bundle();
        arg.putString("id", section.getId());
        arg.putString("name", section.getName());
        arg.putString("description", section.getDescription());
        editSectionFragment.setArguments(arg);

        fragmentTransaction.replace(R.id.root, editSectionFragment);
        fragmentStack.push(editSectionFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void moveToAddNewImagesToSection(Section section) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle arg = new Bundle();
        arg.putString("id", section.getId());
        arg.putString("name", section.getName());
        arg.putString("count", String.valueOf(section.getCountOfImages()));
        arg.putString("descr", section.getDescription());
        arg.putString("last_update", section.getLastUpdate());
        arg.putString("create_date", section.getDateOfCreation());
        detailedSectionFragment.setArguments(arg);

        fragmentTransaction.replace(R.id.root, detailedSectionFragment);
        fragmentStack.push(detailedSectionFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentStack.size() > 1) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentStack.pop();
            fragmentTransaction.replace(R.id.root, fragmentStack.lastElement());
            fragmentTransaction.commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBase = null;
    }
}
