package com.example.arbitragetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "product_database";

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMG_URL = "img_url";

    public static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
            TABLE_PRODUCTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_PRICE + " REAL, " + COLUMN_IMG_URL + " TEXT)";

    public ProductDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMG_URL, product.getImgUrl());
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        Log.d("PRODUCT_DB", "Product added: " + product.getName());
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMG_URL, product.getImgUrl());
        return db.update(TABLE_PRODUCTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(product.getId())});
    }

    public Product getProduct(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Product product = null;
        Cursor cursor = db.query(
                TABLE_PRODUCTS, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_PRICE, COLUMN_IMG_URL},
                COLUMN_ID + "= ?", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor.moveToFirst()) {
            product = new Product(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4));
        }
        cursor.close();
        db.close();
        return product;
    }

    public ArrayList<Product> getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Product> products = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
        while (cursor.moveToNext()) {
            products.add(new Product(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4)));
        }
        db.close();
        return products;
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + "=?", new String[]{String.valueOf(productId)});
        db.close();
        Log.d("PRODUCT_DB", "Product deleted with ID: " + productId);
    }

    public void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, null, null);
        db.close();
        Log.d("PRODUCT_DB", "All products deleted.");
    }
}
