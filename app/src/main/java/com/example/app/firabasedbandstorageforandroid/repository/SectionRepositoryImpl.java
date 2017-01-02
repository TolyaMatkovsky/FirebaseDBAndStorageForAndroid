package com.example.app.firabasedbandstorageforandroid.repository;

import android.util.Log;

import com.example.app.firabasedbandstorageforandroid.model.Image;
import com.example.app.firabasedbandstorageforandroid.model.Section;
import com.example.app.firabasedbandstorageforandroid.repository.interfaces.SectionRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tolik on 31.12.2016.
 */

public class SectionRepositoryImpl implements SectionRepository {
    private static final String SECTIONS = "sections";
    private static final String IMAGES = "images";

    private FirebaseDatabase dataBase;

    public SectionRepositoryImpl(FirebaseDatabase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void getAllSectionFromDB(final CallBack callBack) {
        DatabaseReference secRef = dataBase.getReference(SECTIONS);
        secRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Section> sections = new ArrayList<Section>();

                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Section section = d.getValue(Section.class);
                    sections.add(section);
                }
                callBack.onSuccess(sections);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError();
            }
        });
    }

    @Override
    public void saveSectionInDB(String sectionName, String sectionDescription, String dateOfCreation, String lastUpdate, final SimpleCallBack callBack) {
        DatabaseReference secRef = dataBase.getReference(SECTIONS);
        String sectionID = secRef.push().getKey();
        Section saveSection = new Section(sectionID, sectionName, lastUpdate, dateOfCreation,
                                            sectionDescription, 0);
        secRef.child(sectionID).setValue(saveSection);

        DatabaseReference sectionRef = secRef.child(sectionID).getRef();

        sectionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Section section = dataSnapshot.getValue(Section.class);
                callBack.onSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError();
            }
        });
    }

    @Override
    public void editSectionInDB(final String sectionID, final String sectionName, final String sectionDescription, final String lastUpdate, final SimpleCallBack callBack) {
        final DatabaseReference secRef = dataBase.getReference(SECTIONS);
        final DatabaseReference sectionRef = secRef.child(sectionID).getRef();

        sectionRef.child("name").setValue(sectionName);
        sectionRef.child("description").setValue(sectionDescription);
        sectionRef.child("lastUpdate").setValue(lastUpdate);

        sectionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Section section = dataSnapshot.getValue(Section.class);
                callBack.onSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError();
            }
        });
    }

    @Override
    public void getCountOfImages(String sectionID, final GetResultCallBack callBack) {
        DatabaseReference ref = dataBase.getReference(SECTIONS).child(sectionID).child("countOfImages");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int c = dataSnapshot.getValue(Integer.class);
                callBack.onSuccess(String.valueOf(c));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError();
            }
        });
    }

    @Override
    public void saveImageInSection(String sectionID, int count, String lastUpdate, String imageName, final GetResultCallBack callBack) {
        final DatabaseReference secRef = dataBase.getReference(SECTIONS);
        secRef.child(sectionID).child("lastUpdate").setValue(lastUpdate);
        secRef.child(sectionID).child("countOfImages").setValue(count);

        DatabaseReference imgRef = dataBase.getReference(SECTIONS).child(sectionID).child(IMAGES);
        String imageID = imgRef.push().getKey();

        Image image = new Image(imageID, imageName, lastUpdate);
        imgRef.child(imageID).setValue(image);

        DatabaseReference savedImageRef = imgRef.child(imageID).getRef();
        savedImageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Image i = dataSnapshot.getValue(Image.class);
                callBack.onSuccess(i.getId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError();
            }
        });
    }

    @Override
    public void deleteImageFromSection(String sectionID, int count, String lastUpdate, String imageID, SimpleCallBack callBack) {
        final DatabaseReference secRef = dataBase.getReference(SECTIONS);
        secRef.child(sectionID).child("lastUpdate").setValue(lastUpdate);
        secRef.child(sectionID).child("countOfImages").setValue(count);

        DatabaseReference imagesRef = dataBase.getReference(SECTIONS).child(sectionID).child(IMAGES);
        DatabaseReference deleteImgRef = imagesRef.child(imageID).getRef();

        deleteImgRef.setValue(null);

        callBack.onSuccess();
    }

    @Override
    public void getAllImagesInSection(String sectionID, final ImageCallBack callBack) {
        DatabaseReference imagesRef = dataBase.getReference(SECTIONS).child(sectionID).child(IMAGES);
        imagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Image> images = new ArrayList<Image>();

                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Image image = d.getValue(Image.class);
                    images.add(image);
                }
                callBack.onSuccess(images);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError();
            }
        });
    }
}
