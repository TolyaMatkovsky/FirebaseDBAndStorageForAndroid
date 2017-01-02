package com.example.app.firabasedbandstorageforandroid.model;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tolik on 30.12.2016.
 */
@IgnoreExtraProperties
public class Image {
    public static final String ID = "image_id";
    public static final String NAME = "name";
    public static final String DATE_OF_CREATION = "date_of_creation";

    public String id;
    public String name;
    public String dateOfCreation;

    @Exclude
    public Bitmap imageBitmap;

    public Image() {
    }

    public Image(String id, String name, String dateOfCreation) {
        this.id = id;
        this.name = name;
        this.dateOfCreation = dateOfCreation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put(ID, id);
        result.put(NAME, name);
        result.put(DATE_OF_CREATION, dateOfCreation);

        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateOfCreation='" + dateOfCreation + '\'' +
                ", imageBitmap=" + imageBitmap +
                '}';
    }
}
