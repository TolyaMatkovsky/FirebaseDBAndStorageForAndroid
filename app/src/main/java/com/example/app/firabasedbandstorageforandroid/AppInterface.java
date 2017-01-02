package com.example.app.firabasedbandstorageforandroid;

import android.view.View;

import com.example.app.firabasedbandstorageforandroid.model.Section;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by Tolik on 30.12.2016.
 */

public interface AppInterface {
    void setToolBarTitle(String title);
    void setToolBarRightButtonListener(View.OnClickListener listener);
    void setToolBarLeftButtonListener(View.OnClickListener listener);
    void setToolBarLeftButtonStyle(boolean hidden);
    void setToolBarRightButtonStyle(int styleRes);
    void onBackPressed();
    void moveToAddNewSection();
    void moveToEditSection(Section section);
    void moveToAddNewImagesToSection(Section section);
    FirebaseDatabase getFireBaseDB();
    FirebaseStorage getFireBaseStorage();
    void hideSaveButton(boolean flag);
}
