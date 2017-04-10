package com.collections2.grigelionyte.greta.collections.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                + COLUMN_URI +" text,"
                + COLUMN_CATEGORIES +" text,"
                + COLUMN_FAVORITE + " integer default 0" + ");");

        db.execSQL("CREATE TABLE " + TABLE_ITEMS + " ( "
                + COLUMN_ITEM_ID + " integer primary key autoincrement,"
                + COLUMN_ITEM_NAME + " text not null,"
                + COLUMN_ITEM_DESCRIPTION +" text not null,"
                + COLUMN_ITEM_URI +" text,"
                + COLUMN_ITEM_CATEGORIES +" text,"
                + COLUMN_ITEM_COLLECTION_ID +" integer,"
                + COLUMN_ITEM_FAVORITE + " integer default 0" + ");");
    }

    // UPDATE DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
        onCreate(db);
    }
    // ADD VALUES TO COLLECTION TABLE
    public void addCollection(Collection collection){
        ContentValues valuesCollection = new ContentValues();
        valuesCollection.put(COLUMN_NAME, collection.getName());
        valuesCollection.put(COLUMN_DESCRIPTION, collection.getDescription());
        valuesCollection.put(COLUMN_URI, String.valueOf(collection.getImage()));
        valuesCollection.put(COLUMN_CATEGORIES, collection.getCategories());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_COLLECTIONS, null, valuesCollection);
        db.close();
    }
    // ADD VALUES TO ITEM TABLE
    public void addItem(Item item){
        ContentValues valuesItem = new ContentValues();
        valuesItem.put(COLUMN_NAME, item.getName());
        valuesItem.put(COLUMN_DESCRIPTION, item.getDescription());
        valuesItem.put(COLUMN_URI, String.valueOf(item.getImage()));
        valuesItem.put(COLUMN_CATEGORIES, item.getCategories());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_COLLECTIONS, null, valuesItem);
        db.close();
    }

//    public void deleteCollection(String name) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM " + " WHERE " + COLUMN_NAME + "=\"" + name + "\";");
//        db.close();
//
//    }
//    public int updateCollection(Collection collection){
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues colValues = new ContentValues();
//        colValues.put(COLUMN_NAME, collection.getName());
//        colValues.put(COLUMN_DESCRIPTION, collection.getDescription());
//        colValues.put(COLUMN_URI, String.valueOf(collection.getImage()));
//        colValues.put(COLUMN_CATEGORIES, collection.getCategories());
//
//        return db.update(TABLE_COLLECTIONS, colValues, COLUMN_ID + "=?", new String[] {String.valueOf(collection.getID)});
//    }

}
