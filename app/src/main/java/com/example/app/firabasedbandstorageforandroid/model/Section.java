package com.example.app.firabasedbandstorageforandroid.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tolik on 30.12.2016.
 */

@IgnoreExtraProperties
public class Section {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String LAST_UPDATE = "lastUpdate";
    public static final String DATE_OF_CREATION = "dateOfCreation";
    public static final String DESCRIPTION = "description";
    public static final String COUNT_IMAGES = "countOfImages";

    public String id;
    public String name;
    public String lastUpdate;
    public String dateOfCreation;
    public String description;
    public int countOfImages;

    public Section() {
    }

    public Section(String id, String name, String lastUpdate, String dateOfCreation, String description, int countOfImages) {
        this.id = id;
        this.name = name;
        this.lastUpdate = lastUpdate;
        this.dateOfCreation = dateOfCreation;
        this.description = description;
        this.countOfImages = countOfImages;
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCountOfImages() {
        return countOfImages;
    }

    public void setCountOfImages(int countOfImages) {
        this.countOfImages = countOfImages;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put(ID, id);
        result.put(NAME, name);
        result.put(lastUpdate, LAST_UPDATE);
        result.put(DATE_OF_CREATION, dateOfCreation);
        result.put(DESCRIPTION, description);
        result.put(COUNT_IMAGES, countOfImages);

        return result;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", dateOfCreation='" + dateOfCreation + '\'' +
                ", description='" + description + '\'' +
                ", countOfImages=" + countOfImages +
                '}';
    }
}
