package com.collections2.grigelionyte.greta.collections.model;

import android.net.Uri;

class Collection {

    private String name, description, categories;
    private Uri image;
    private boolean favourite = false;

    public Collection(){

    }
    public Collection(String name, String description, Uri image, String categories){
        this.name = name;
        this.description = description;
        this.image = image;
        this.categories = categories;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    protected String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setFavourite(boolean favourite){

    }
    public boolean getFavourite(){
        return favourite;
    }
}
