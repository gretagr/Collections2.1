package com.collections2.grigelionyte.greta.collections.model;

public class ItemsCollection {

    private String title, subTitle, categories;
    private byte[] imageResId;
    private int cardId;
    private int favorite;

    public ItemsCollection() {

    }

    public ItemsCollection(String title, String subTitle, byte[] imageResId, String categories, int favorite) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageResId = imageResId;
        this.categories = categories;
        this.favorite = favorite;

    }
    public ItemsCollection(int cardId, String title, String subTitle, byte[] imageResId, String categories, int favorite) {
        this.cardId = cardId;
        this.title = title;
        this.subTitle = subTitle;
        this.imageResId = imageResId;
        this.categories = categories;
        this.favorite = favorite;

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

    public byte[] getColImage() {
        return this.imageResId;
    }

    public void setColImage(byte[] imageResId) {
        this.imageResId = imageResId;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getFavoriteCol() {
        return favorite;
    }

    public void setFavoriteCol(int favorite) {
        this.favorite = favorite;
    }
}
