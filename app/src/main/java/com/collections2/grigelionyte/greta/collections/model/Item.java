package com.collections2.grigelionyte.greta.collections.model;


public class Item {
    private String title, subTitle, categories, itemCatText;
    private byte[] imageResId;
    private int imgEdit;
    private int imgDelete;
    private int colId;
    private int id;
    private int favorite;
    public Item(){

    }
    public Item(int id, String title, String subTitle,byte[] imageResId, String categories, String itemCatText, int colId, int favorite){
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.categories = categories;
        this.itemCatText = itemCatText;
        this.imageResId = imageResId;
        this.colId = colId;
        this.favorite = favorite;
    }
    public Item(String title, String subTitle,byte[] imageResId, String categories, String itemCatText, int colId, int favorite){
        this.title = title;
        this.subTitle = subTitle;
        this.categories = categories;
        this.itemCatText = itemCatText;
        this.imageResId = imageResId;
        this.colId = colId;
        this.favorite = favorite;
    }
    public Item(String title, String subTitle, byte[] imageResId, String categories, String itemCatText, int colId){
        this.title = title;
        this.subTitle = subTitle;
        this.categories = categories;
        this.itemCatText = itemCatText;
        this.imageResId = imageResId;
        this.colId = colId;

    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getColId() {
        return colId;
    }

    public void setColId(int colId) {
        this.colId = colId;
    }

    public String getTitle() {
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

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getItemCat(){
        return itemCatText;
    }
    public void setItemCatText(String itemCatText){
        this.itemCatText = itemCatText;
    }

    public byte[] getImage() {
        return imageResId;
    }

    public void setImage(byte[] imageResId) {
        this.imageResId = imageResId;
    }

    public int getFavorite(){
       return favorite;
    }
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }


}
