package com.example.android.inventoryapp;

import android.provider.BaseColumns;

/**
 * Created by Matthew Young on 7/1/16.
 */
public class ProductDatabaseContract {
    public ProductDatabaseContract() {}

    public static abstract class ProductDatabase implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_NAME_PRODUCT_ID = "id";
        public static final String COLUMN_NAME_PRODUCT_NAME = "name";
        public static final String COLUMN_NAME_PRODUCT_PRICE = "price";
        public static final String COLUMN_NAME_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_NAME_PRODUCT_IMAGE = "image";
    }
}
