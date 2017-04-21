package com.collections2.grigelionyte.greta.collections.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper{

    // DB INFO
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "collections.db";

    // COLLECTIONS TABLE AND COLUMNS

    private static final String TABLE_COLLECTIONS = "collections",
                        COLUMN_ID = "_id",
                        COLUMN_NAME = "name",
                        COLUMN_DESCRIPTION = "descrtiption",
                        COLUMN_URI = "image_Uri",
                        COLUMN_CATEGORIES = "categories",
                        COLUMN_FAVORITE = "favorite";

    // ITEM TABLE AND COLUMNS
    private static final String TABLE_ITEMS = "items",
                        COLUMN_ITEM_ID = "_id",
                        COLUMN_ITEM_NAME = "name",
                        COLUMN_ITEM_DESCRIPTION = "descrtiption",
                        COLUMN_ITEM_URI = "image_Uri",
                        COLUMN_ITEM_CATEGORIES = "categories",
                        COLUMN_ITEM_CATEGORIES_TEXT = "categories_text",
                        COLUMN_ITEM_FAVORITE = "favorite",
                        COLUMN_ITEM_COLLECTION_ID = "collection_id";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();

    }
    // CREATE TABLES
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_COLLECTIONS + " ( "
                + COLUMN_ID + " integer primary key autoincrement,"
                + COLUMN_NAME + " text not null,"
                + COLUMN_DESCRIPTION +" text not null,"
                + COLUMN_URI +" blob,"
                + COLUMN_CATEGORIES +" text,"
                + COLUMN_FAVORITE + " integer default 0" + ");");

        db.execSQL("CREATE TABLE " + TABLE_ITEMS + " ( "
                + COLUMN_ITEM_ID + " integer primary key autoincrement,"
                + COLUMN_ITEM_NAME + " text not null,"
                + COLUMN_ITEM_DESCRIPTION +" text not null,"
                + COLUMN_ITEM_URI +" text,"
                + COLUMN_ITEM_CATEGORIES +" text,"
                + COLUMN_ITEM_CATEGORIES_TEXT + " text,"
                + COLUMN_ITEM_COLLECTION_ID +" integer,"
                + COLUMN_ITEM_FAVORITE + " integer default 0" + ");");
    }

    // UPDATE DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }
    // ADD VALUES TO COLLECTION TABLE
    public void addCollection(ItemsCollection collection){
        ContentValues valuesCollection = new ContentValues();
        valuesCollection.put(COLUMN_NAME, collection.getColTitle());
        valuesCollection.put(COLUMN_DESCRIPTION, collection.getSubTitle());
        valuesCollection.put(COLUMN_URI, collection.getColImage());
        valuesCollection.put(COLUMN_CATEGORIES, collection.getCategories());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_COLLECTIONS, null, valuesCollection);
        db.close();
    }
    // ADD VALUES TO ITEM TABLE
    public void addItem(Item item){
        ContentValues valuesItem = new ContentValues();
        valuesItem.put(COLUMN_ITEM_NAME, item.getTitle());
        valuesItem.put(COLUMN_ITEM_DESCRIPTION, item.getSubTitle());
        valuesItem.put(COLUMN_ITEM_URI, item.getImage());
        valuesItem.put(COLUMN_ITEM_CATEGORIES, item.getCategories());
        valuesItem.put(COLUMN_ITEM_CATEGORIES_TEXT, item.getItemCat());
        valuesItem.put(COLUMN_ITEM_COLLECTION_ID, item.getColId());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ITEMS, null, valuesItem);
        db.close();
    }
    // delete values
    public int deleteCollection(ItemsCollection collection) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_COLLECTIONS, COLUMN_NAME + "=?", new String[]{String.valueOf(collection.getColTitle())});


    }
    public int deleteItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        return  db.delete(TABLE_ITEMS, COLUMN_ITEM_NAME  + "=?", new String[]{String.valueOf(item.getTitle())} );

    }
    public  List<ItemsCollection> getAllCollections(){
      List<ItemsCollection> collections = new ArrayList<ItemsCollection>();
        String getAll = "SELECT * FROM " + TABLE_COLLECTIONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(getAll, null);

        if (cursor.moveToFirst()) {
            do {
                ItemsCollection collection = new ItemsCollection( cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)));
                collections.add(collection);
            } while (cursor.moveToNext());
        }

        return collections;
    }
    public  List<Item> getAllItems(){
        List<Item> items = new ArrayList<Item>();
        String getAll = "SELECT * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(getAll, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)) );
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }
    public Cursor getAllValues()
    {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns={COLUMN_ID, COLUMN_NAME,COLUMN_DESCRIPTION,COLUMN_CATEGORIES, COLUMN_FAVORITE};
        return  db.query(TABLE_COLLECTIONS, columns, null, null, null, null, null);
    }
    public int getColIdInItems(String name) {
        SQLiteDatabase db = getWritableDatabase();
        int id = 0;
        String[] columns = {COLUMN_ITEM_COLLECTION_ID, COLUMN_NAME};
        Cursor cursor = db.query(TABLE_ITEMS, columns, COLUMN_NAME + " = '" + name + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_ITEM_COLLECTION_ID);

            id = cursor.getInt(index);
        }
        return id;
    }

    public int getId(String name){
        SQLiteDatabase db = getWritableDatabase();
        int id = 0;
        String[] columns = {COLUMN_ID, COLUMN_NAME};
        Cursor cursor = db.query(TABLE_COLLECTIONS, columns, COLUMN_NAME + " = '"+ name + "'", null, null, null, null );
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_ID);

            id = cursor.getInt(index);
        }
        return id;
    }
    public String getCategories(String name){
        SQLiteDatabase db = getWritableDatabase();
        String string = "";
        String[] columns = {COLUMN_NAME, COLUMN_CATEGORIES};
        Cursor cursor = db.query(TABLE_COLLECTIONS, columns, COLUMN_NAME + " = '"+ name + "'", null, null, null, null );
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_CATEGORIES);
            string = cursor.getString(index);
        }
        return string;
    }
    public String getColName(int id){
        SQLiteDatabase db = getWritableDatabase();
        String nameOfCol = "";
        String[] columns = {COLUMN_NAME, COLUMN_ID};
        Cursor cursor = db.query(TABLE_COLLECTIONS, columns, COLUMN_NAME + " = '"+ id + "'", null, null, null, null );
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_ID);
            nameOfCol = cursor.getString(index);
        }
        return nameOfCol;
    }

    public  List<Item> getAllItemsFromCollection(int id){
        List<Item> items = new ArrayList<Item>();
        SQLiteDatabase db = this.getWritableDatabase();
        //String[]columns = {COLUMN_ITEM_NAME, COLUMN_ITEM_DESCRIPTION, COLUMN_ITEM_URI, COLUMN_ITEM_CATEGORIES, COLUMN_ITEM_CATEGORIES_TEXT, COLUMN_ITEM_COLLECTION_ID, COLUMN_FAVORITE};
        //Cursor cursor = db.query(TABLE_ITEMS, columns, COLUMN_ITEM_COLLECTION_ID + " = '" + id + "'", null, null, null, null);
        String getAll = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_COLLECTION_ID + " = " + id;
        Cursor cursor =  db.rawQuery(getAll, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)) );
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }
    public int setFavoriteItem(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_FAVORITE, 1);
        return db.update(TABLE_ITEMS, contentValues, COLUMN_ITEM_NAME + " =? ", new String[] {name});
    }
    public int setNotFavorite(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_FAVORITE, 0);
        return db.update(TABLE_ITEMS, contentValues, COLUMN_ITEM_NAME + " =? ", new String[] {name});
    }
    public int getFavorite(String name){
        SQLiteDatabase db = getWritableDatabase();
        int res = 0;
        String[] columns = {COLUMN_ITEM_NAME, COLUMN_ITEM_FAVORITE};
        Cursor cursor = db.query(TABLE_ITEMS, columns, COLUMN_ITEM_NAME + " = '"+ name + "'", null, null, null, null );
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_ITEM_FAVORITE);
            res = cursor.getInt(index);
        }
        return res;
    }
    //-------------------------------------------------------------------------------------------------------------
    public int setFavoriteCol(ItemsCollection itemsCollection){
        SQLiteDatabase db = this.getWritableDatabase();
        String name = itemsCollection.getColTitle();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FAVORITE, 1);
        return db.update(TABLE_COLLECTIONS, contentValues, COLUMN_NAME + " =? ", new String[] {name});
    }
    public int setNotFavoriteCol(ItemsCollection itemsCollection){
        SQLiteDatabase db = this.getWritableDatabase();
        String name = itemsCollection.getColTitle();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FAVORITE, 0);
        return db.update(TABLE_COLLECTIONS, contentValues, COLUMN_NAME + " =? ", new String[] {name});
    }
    public int getFavoriteCol(ItemsCollection itemsCollection){
        SQLiteDatabase db = getWritableDatabase();
        String name = itemsCollection.getColTitle();
        int res = 0;
        String[] columns = {COLUMN_NAME, COLUMN_FAVORITE};
        Cursor cursor = db.query(TABLE_COLLECTIONS, columns, COLUMN_NAME + " = '"+ name + "'", null, null, null, null );
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_FAVORITE);
            res = cursor.getInt(index);
        }
        return res;
    }
    public int updateItem(Item item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
         contentValues.put(COLUMN_ITEM_NAME, item.getTitle());
        contentValues.put(COLUMN_DESCRIPTION, item.getSubTitle());
        contentValues.put(COLUMN_ITEM_URI, String.valueOf(item.getImage()));
        contentValues.put(COLUMN_ITEM_CATEGORIES_TEXT, item.getItemCat());
        contentValues.put(COLUMN_ITEM_COLLECTION_ID, item.getColId());
        return db.update(TABLE_ITEMS, contentValues, COLUMN_ITEM_NAME + "=?", new String[] {item.getTitle()});
    }
}
