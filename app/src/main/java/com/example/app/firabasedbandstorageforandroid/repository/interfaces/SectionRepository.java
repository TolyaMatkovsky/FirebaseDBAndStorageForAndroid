package com.example.app.firabasedbandstorageforandroid.repository.interfaces;

import com.example.app.firabasedbandstorageforandroid.model.Image;
import com.example.app.firabasedbandstorageforandroid.model.Section;

import java.util.List;

/**
 * Created by Tolik on 31.12.2016.
 */

public interface SectionRepository {
    interface CallBack{
        void onSuccess(List<Section> sections);
        void onError();
    }
    interface SimpleCallBack{
        void onSuccess();
        void onError();
    }

    interface ImageCallBack{
        void onSuccess(List<Image> images);
        void onError();
    }

    interface GetResultCallBack {
        void onSuccess(String result);
        void onError();
    }

    void getCountOfImages(String sectioID, GetResultCallBack callBack);

    void getAllImagesInSection(String sectionID, ImageCallBack callBack);

    void saveImageInSection(String sectionID, int count, String lastUpdate, String imageName, GetResultCallBack callBack);

    void deleteImageFromSection(String sectionID, int count, String lastUpdate, String imageID, SimpleCallBack callBack);

    void getAllSectionFromDB(CallBack callBack);

    void editSectionInDB(String sectionID, String sectionName, String sectionDescription, String lastUpdate, SimpleCallBack callBack);

    void saveSectionInDB(String sectionName, String sectionDescription, String dateOfCreation, String lastUpdate, SimpleCallBack callBack);
}
