package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Matthew Young on 7/1/16.
 */
public class ProductDatabaseDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ProductDatabase.db";

    public ProductDatabaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductDatabaseContract.ProductDatabase.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table product" +
                " (id integer, name text, price real, quantity integer, image text)");
    }

    public boolean insertProduct(Product product) {

        Cursor cursor = getData(product.getId());
        if (cursor.getCount() > 0) {
            return updateProduct(product);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_ID, product.getId());
            contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_NAME, product.getName());
            contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_PRICE, product.getPrice());
            contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_QUANTITY, product.getQuantity());
            contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_IMAGE, product.getImageURIString());
            db.insert(ProductDatabaseContract.ProductDatabase.TABLE_NAME, null, contentValues);
            return true;
        }
    }

    public Cursor getData(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from product where id=" + id, null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfRows = (int) DatabaseUtils.queryNumEntries(db, ProductDatabaseContract.ProductDatabase.TABLE_NAME);
        return numberOfRows;
    }

    public boolean updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_ID, product.getId());
        contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_NAME, product.getName());
        contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_PRICE, product.getPrice());
        contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_QUANTITY, product.getQuantity());
        contentValues.put(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_IMAGE, product.getImageURIString());
        db.update(ProductDatabaseContract.ProductDatabase.TABLE_NAME, contentValues, "id = ? ",
                new String[] { Integer.toString(product.getId())});
        return true;
    }

    public Integer deleteProduct (Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(ProductDatabaseContract.ProductDatabase.TABLE_NAME, "id = ? ",
                new String[] { Integer.toString(id)});
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> arrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from product", null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            Integer id = res.getInt(res.getColumnIndex(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_ID));
            String name = res.getString(res.getColumnIndex(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_NAME));
            float price = res.getFloat(res.getColumnIndex(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_PRICE));
            Integer quantity = res.getInt(res.getColumnIndex(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_QUANTITY));
            String imageURI = res.getString(res.getColumnIndex(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_IMAGE));

            Product product = new Product(id, name, price, quantity, imageURI);

            arrayList.add(product);
            res.moveToNext();
        }
        return arrayList;
    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }
}
