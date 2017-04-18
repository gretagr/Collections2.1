package com.collections2.grigelionyte.greta.collections.model;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

public class ItemsCollection extends AppCompatActivity {

    private String title, subTitle, categories;
    private Uri imageResId;
    private int cardId;
    private boolean favorite = false;

    public ItemsCollection(){

    }
    public ItemsCollection(String title, String subTitle, Uri imageResId, String categories){
        this.title = title;
        this.subTitle = subTitle;
        this.imageResId = imageResId;
        this.categories = categories;

    }
    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
    public String getColTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Uri getColImage() {
        return this.imageResId;
    }

    public void setColImage(Uri imageResId) {
        this.imageResId = imageResId;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setFavorite(boolean favorite){

    }
    public boolean getFavorite(){
        return favorite;
    }
}
