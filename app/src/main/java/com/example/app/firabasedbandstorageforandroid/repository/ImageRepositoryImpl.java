package com.example.app.firabasedbandstorageforandroid.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.app.firabasedbandstorageforandroid.manager.ImageSaveManager;
import com.example.app.firabasedbandstorageforandroid.repository.interfaces.ImageRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Tolik on 31.12.2016.
 */

public class ImageRepositoryImpl implements ImageRepository {
    private static final String STORAGE_URL = "gs://firabaseclient.appspot.com";

    private FirebaseStorage storage;

    public ImageRepositoryImpl(FirebaseStorage storage) {
        this.storage = storage;
    }

    @Override
    public void saveImageInStorage(String sectionID, String imageName, final SimpleCallBack callBack) {
        StorageReference storageRef = storage.getReferenceFromUrl(STORAGE_URL);
        StorageReference imgRef = storageRef.child("sections").child(sectionID).child(imageName + ".png");

        File savedFile = ImageSaveManager.getFileByImageName(imageName);
        Uri file = Uri.fromFile(savedFile);
        UploadTask uploadTask = imgRef.putFile(file);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callBack.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onError();
            }
        });

    }

    @Override
    public void deleteImageFromStorage(String sectionID, String imageName, final SimpleCallBack callBack) {
        StorageReference storageRef = storage.getReferenceFromUrl(STORAGE_URL);
        StorageReference imgRef = storageRef.child("sections").child(sectionID).child(imageName + ".png");

        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onError();
            }
        });
    }

    @Override
    public void getImageInSection(String sectionID, final String imageName, final Context context, final ImageStorageCallBack callBack) {
        StorageReference storageRef = storage.getReferenceFromUrl(STORAGE_URL);
        StorageReference imgRef = storageRef.child("sections").child(sectionID).child(imageName + ".png");

        final File tempFile = ImageSaveManager.getFileByImageName("tmp"+ new Random().nextInt(300));

        imgRef.getFile(tempFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = ImageSaveManager.readByFile(tempFile);
                tempFile.delete();
                callBack.onSuccess(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onError();
            }
        });
    }
}
