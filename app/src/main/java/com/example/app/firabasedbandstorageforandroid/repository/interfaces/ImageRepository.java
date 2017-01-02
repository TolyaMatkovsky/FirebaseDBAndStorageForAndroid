package com.example.app.firabasedbandstorageforandroid.repository.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Tolik on 31.12.2016.
 */

public interface ImageRepository {
    interface SimpleCallBack{
        void onSuccess();
        void onError();
    }

    interface ImageStorageCallBack{
        void onSuccess(Bitmap bitmap);
        void onError();
    }

    void saveImageInStorage(String sectionID, String imageName, SimpleCallBack callBack);

    void deleteImageFromStorage(String sectionID, String imageName, SimpleCallBack callBack);

    void getImageInSection(String sectionID, String imageName, Context context, ImageStorageCallBack callBack);
}
