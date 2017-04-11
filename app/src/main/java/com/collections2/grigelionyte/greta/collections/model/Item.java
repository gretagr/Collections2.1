package com.collections2.grigelionyte.greta.collections.model;


import android.net.Uri;

public class Item {
    private String name, description, categories;
    private Uri image;

    public Item(){

    }
    public Item(String name, String description, String categories, Uri image){
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
