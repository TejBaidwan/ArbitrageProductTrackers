package com.example.arbitragetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class represents the DatabaseHandler for the backend of this application
 * @author Evan Proulx and Tej Baidwan
 */
public class ProductDatabase extends SQLiteOpenHelper {
    private static ProductDatabase instance;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "product_database";

    //Creating the table(s)
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMG_URL = "img_url";
    private static final String COLUMN_STATUS = "status";

    public static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
            TABLE_PRODUCTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_PRICE + " REAL, " + COLUMN_IMG_URL + " TEXT, " +
            COLUMN_STATUS + " INTEGER DEFAULT 0)";

    public ProductDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // Recreate the table with the updated structure
        onCreate(db);
    }


    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMG_URL, product.getImgUrl());
        values.put(COLUMN_STATUS, product.isSold()); // 1 for sold, 0 for unsold
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
        values.put(COLUMN_STATUS, product.isSold());
        return db.update(TABLE_PRODUCTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(product.getId())});
    }

    public int updateProductStatus(int productId, int status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        return db.update(TABLE_PRODUCTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(productId)});
    }

    public Product getProduct(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Product product = null;
        Cursor cursor = db.query(
                TABLE_PRODUCTS, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_PRICE, COLUMN_IMG_URL, COLUMN_STATUS},
                COLUMN_ID + "= ?", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor.moveToFirst()) {
            product = new Product(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getInt(5));
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
                    cursor.getString(4),
                    cursor.getInt(5)));
        }
        cursor.close();
        db.close();
        return products;
    }

    //delete single product from db
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

    //Method to get the total prices
    public double getTotalPrice() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_PRICE + ") FROM " + TABLE_PRODUCTS, null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    // Method to get highest price
    public double getHighestPrice() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_PRICE + ") FROM " + TABLE_PRODUCTS, null);
        double highest = 0;
        if (cursor.moveToFirst()) {
            highest = cursor.getDouble(0);
        }
        cursor.close();
        return highest;
    }

    //Method to get the lowest price
    public double getLowestPrice() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MIN(" + COLUMN_PRICE + ") FROM " + TABLE_PRODUCTS, null);
        double lowest = 0;
        if (cursor.moveToFirst()) {
            lowest = cursor.getDouble(0);
        }
        cursor.close();
        return lowest;
    }

    // Method to get average price
    public double getAveragePrice() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + COLUMN_PRICE + ") FROM " + TABLE_PRODUCTS, null);
        double average = 0;
        if (cursor.moveToFirst()) {
            average = cursor.getDouble(0);
        }
        cursor.close();
        return average;
    }

    // Method to get the total number of items in the table
    public int getTotalItemCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PRODUCTS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Singleton instance getter
    public static ProductDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new ProductDatabase(context.getApplicationContext());
        }
        return instance;
    }

}
