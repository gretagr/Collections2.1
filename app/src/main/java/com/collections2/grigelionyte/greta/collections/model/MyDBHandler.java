package com.collections2.grigelionyte.greta.collections.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper{

    //--------------------------------------------------------------------------------------------------------------------------- VARIABLES
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "collections.db";




    //----------------------------------------------------------------------------------------------------------------- for collections table

    private static final String TABLE_COLLECTIONS = "collections",
                        COLUMN_ID = "_id",
                        COLUMN_NAME = "name",
                        COLUMN_DESCRIPTION = "descrtiption",
                        COLUMN_URI = "image_Uri",
                        COLUMN_CATEGORIES = "categories",
                        COLUMN_FAVORITE = "favorite";

    //----------------------------------------------------------------------------------------------------------------------- for items table
    private static final String TABLE_ITEMS = "items",
                        COLUMN_ITEM_ID = "_id",
                        COLUMN_ITEM_NAME = "name",
                        COLUMN_ITEM_DESCRIPTION = "descrtiption",
                        COLUMN_ITEM_URI = "image_Uri",
                        COLUMN_ITEM_CATEGORIES = "categories",
                        COLUMN_ITEM_CATEGORIES_TEXT = "categories_text",
                        COLUMN_ITEM_FAVORITE = "favorite",
                        COLUMN_ITEM_COLLECTION_ID = "collection_id";
    //----------------------------------------------------------------------------------------------------------------------------- db handler
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();

    }


    //--------------------------------------------------------------------------------------------------------------------------- create tables
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


    //--------------------------------------------------------------------------------------------------------------------------------- upgrade db
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }


    //--------------------------------------------------------------------------------------------------------- add collection to table collections
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
    //--------------------------------------------------------------------------------------------------------------------- add Item to table items
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
    //------------------------------------------------------------------------------------------------------------------ delete collection / item
    public int deleteCollection(ItemsCollection collection) {
        SQLiteDatabase db = getWritableDatabase();
        int dbDeleted = db.delete(TABLE_COLLECTIONS, COLUMN_ID + "=?", new String[]{String.valueOf(collection.getCardId())});
        db.delete(TABLE_ITEMS, COLUMN_ITEM_COLLECTION_ID + "=?", new String[]{String.valueOf(collection.getCardId())});
        return dbDeleted;
    }
    public int deleteItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        return  db.delete(TABLE_ITEMS, COLUMN_ITEM_ID  + "=?", new String[]{String.valueOf(item.getId())} );

    }

    //--------------------------------------------------------------------------------------------------------------------------- get list values

    public  List<Item> getAllItems(){
        List<Item> items = new ArrayList<Item>();
        String getAll = "SELECT * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(getAll, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getBlob(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        Integer.parseInt(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7)));
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public  List<ItemsCollection> getAllCollections(){
        List<ItemsCollection> collections = new ArrayList<ItemsCollection>();
        String getAll = "SELECT * FROM " + TABLE_COLLECTIONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(getAll, null);

        if (cursor.moveToFirst()) {
            do {
                ItemsCollection collection = new ItemsCollection(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getBlob(3),
                        cursor.getString(4),
                        Integer.parseInt(cursor.getString(5)));

                collections.add(collection);

            } while (cursor.moveToNext());
        }

        return collections;
    }
    //-------------------------------------------------------------------------------------------------------------------------- get cursor values

    public Cursor getAllValues() {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns={
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_DESCRIPTION,
                COLUMN_CATEGORIES,
                COLUMN_FAVORITE};
        return  db.query(TABLE_COLLECTIONS, columns, null, null, null, null, null);
    }
    public Cursor getAllItemValues() {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns={
                COLUMN_ITEM_ID,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_DESCRIPTION,
                COLUMN_ITEM_CATEGORIES,
                COLUMN_ITEM_FAVORITE};
        return  db.query(TABLE_ITEMS, columns, null, null, null, null, null);
    }
    //--------------------------------------------------------------------------------------------- get collection desc by name (for info button)
    public String getCollectionDescByName(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String desc = "";
        String[] columns = {COLUMN_DESCRIPTION, COLUMN_NAME};

        Cursor cursor = db.query(TABLE_COLLECTIONS, columns, COLUMN_NAME + " = '" + name + "'", null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_DESCRIPTION);

            desc = cursor.getString(index);
        }
        return desc;
    }
   //------------------------------------------------------------------------------------------------------------------ get collection id by name
    public int getId(String name) {
        SQLiteDatabase db = getWritableDatabase();
        int id = 0;
        String[] columns = {COLUMN_ID, COLUMN_NAME};
        Cursor cursor = db.query(TABLE_COLLECTIONS, columns, COLUMN_NAME + " = '" + name + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_ID);
            id = cursor.getInt(index);
        }
        return id;
    }
    //----------------------------------------------------------------------------------------------------------------------- get item id by name
    public int getItemIdByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        int id = 0;
        String[] columns = {COLUMN_ITEM_ID, COLUMN_ITEM_NAME};
        Cursor cursor = db.query(TABLE_ITEMS, columns, COLUMN_ITEM_NAME + " = '"+ name + "'", null, null, null, null );
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_ITEM_ID);

            id = cursor.getInt(index);
        }
        return id;
    }
    //--------------------------------------------------------------------------------------------------------------------- get categories by name
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
    //----------------------------------------------------------------------------------------------------- all items of specific collection by id
    public  List<Item> getAllItemsFromCollection(int id){
        List<Item> items = new ArrayList<Item>();
        SQLiteDatabase db = this.getWritableDatabase();
        String getAll = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_COLLECTION_ID + " = " + id;
        Cursor cursor =  db.rawQuery(getAll, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                        cursor.getString(2),
                        cursor.getBlob(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        Integer.parseInt(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7)));
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }
    //-------------------------------------------------------------------------------------------------------------------- setting item favorites
    public int setFavoriteItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_FAVORITE, 1);
        return db.update(TABLE_ITEMS, contentValues, COLUMN_ITEM_ID + " =? ", new String[] {String.valueOf(item.getId())});
    }

    public int setNotFavorite(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_FAVORITE, 0);
        return db.update(TABLE_ITEMS, contentValues, COLUMN_ITEM_ID + " =? ", new String[] {String.valueOf(item.getId())});
    }

    public int getFavorite(Item item){
        SQLiteDatabase db = getWritableDatabase();
        int res = 0;
        String[] columns = {COLUMN_ITEM_ID, COLUMN_ITEM_FAVORITE};
        Cursor cursor = db.query(TABLE_ITEMS, columns, COLUMN_ITEM_ID + " = '"+ item.getId() + "'", null, null, null, null );
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_ITEM_FAVORITE);
            res = cursor.getInt(index);
        }
        return res;
    }
    //------------------------------------------------------------------------------------------------------------- setting collection favorites
    public int setFavoriteCol(ItemsCollection itemsCollection){
        SQLiteDatabase db = this.getWritableDatabase();
        String name = itemsCollection.getColTitle();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FAVORITE, 1);
        return db.update(TABLE_COLLECTIONS, contentValues, COLUMN_ID + " =? ", new String[] {String.valueOf(itemsCollection.getCardId())});
    }

    public int setNotFavoriteCol(ItemsCollection itemsCollection){
        SQLiteDatabase db = this.getWritableDatabase();
        String name = itemsCollection.getColTitle();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FAVORITE, 0);
        return db.update(TABLE_COLLECTIONS, contentValues, COLUMN_ID + " =? ", new String[] {String.valueOf(itemsCollection.getCardId())});
    }

    public int getFavoriteCol(ItemsCollection itemsCollection){
        SQLiteDatabase db = getWritableDatabase();
        int res = 0;
        String[] columns = {COLUMN_ID, COLUMN_FAVORITE};
        Cursor cursor = db.query(TABLE_COLLECTIONS, columns, COLUMN_ID + " = '"+ itemsCollection.getCardId() + "'", null, null, null, null );
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(COLUMN_FAVORITE);
            res = cursor.getInt(index);
        }
        return res;
    }
    //---------------------------------------------------------------------------------------------------------------------------- get item by id
    public Item getItem(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEMS, new String[]{
                COLUMN_ITEM_ID,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_DESCRIPTION,
                COLUMN_ITEM_URI,
                COLUMN_ITEM_CATEGORIES,
                COLUMN_ITEM_CATEGORIES_TEXT,
                COLUMN_ITEM_FAVORITE,
                COLUMN_ITEM_COLLECTION_ID}, COLUMN_ITEM_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

        Item item = new Item(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getBlob(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6),
                cursor.getInt(7));
        db.close();
        cursor.close();
        return item;

    }
    //------------------------------------------------------------------------------------------------------------------------------- update item
    public int updateItem(Item item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_NAME, item.getTitle());
        contentValues.put(COLUMN_ITEM_DESCRIPTION, item.getSubTitle());
        contentValues.put(COLUMN_ITEM_URI, item.getImage());
        contentValues.put(COLUMN_ITEM_CATEGORIES_TEXT, item.getItemCat());
        return db.update(TABLE_ITEMS, contentValues, COLUMN_ITEM_ID + "=?", new String[] {String.valueOf(item.getId())});
    }
    //---------------------------------------------------------------------------------------------------------------------- get collections count
    public int getCollectionsCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "  + TABLE_COLLECTIONS, null);

        int count = cursor.getCount();
        db.close();
        cursor.close();
        if(count > 0)
            return count;
        else
            return 0;
    }
    //---------------------------------------------------------------------------------------------------------------------------- get items Count
    public int getItemsCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "  + TABLE_ITEMS, null);

        int count = cursor.getCount();
        db.close();
        cursor.close();
        if(count > 0)
            return count;
        else
            return 0;
    }
}
