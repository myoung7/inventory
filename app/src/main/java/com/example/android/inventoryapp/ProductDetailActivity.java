package com.example.android.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

public class ProductDetailActivity extends AppCompatActivity {

    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setTitle(getString(R.string.product_details));

        Integer productId = Integer.parseInt(getIntent().getStringExtra("id"));

        loadProductDetails(productId);
    }

    public void increaseQuantityButtonPressed(View view) {
        ProductDatabaseDbHelper db = new ProductDatabaseDbHelper(getApplicationContext());

        Product updatedProduct = currentProduct;

        Integer updatedQuantity = updatedProduct.getQuantity() + 1;

        TextView productDetailQuantityTextView = (TextView) findViewById(R.id.product_detail_quantity_textview);


        updatedProduct.setQuantity(updatedQuantity);
        db.updateProduct(updatedProduct);
        productDetailQuantityTextView.setText(updatedQuantity.toString());
    }

    public void decreaseQuantityButtonPressed(View view) {
        ProductDatabaseDbHelper db = new ProductDatabaseDbHelper(getApplicationContext());

        Product updatedProduct = currentProduct;

        TextView productDetailQuantityTextView = (TextView) findViewById(R.id.product_detail_quantity_textview);

        if (currentProduct.getQuantity() >= 1) {
            Integer updatedQuantity = updatedProduct.getQuantity() - 1;
            updatedProduct.setQuantity(updatedQuantity);
            db.updateProduct(updatedProduct);
            productDetailQuantityTextView.setText(updatedQuantity.toString());
        }
    }

    public void orderFromSupplierButtonPressed(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.supplier_email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "We Need More: ID#:" + currentProduct.getId() + " " + currentProduct.getName());
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.supplier_email_body));
        startActivity(intent);
    }

    public void deleteFromDatabaseButtonPressed(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("DELETE CONFIRMATION");
        alertDialog.setMessage(R.string.confirm_deletion);
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProductDatabaseDbHelper db = new ProductDatabaseDbHelper(getApplicationContext());
                db.deleteProduct(currentProduct.getId());

                Intent mainActivityIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }
        });
        alertDialog.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
            }
        });
        alertDialog.show();

    }

    private void loadProductDetails(Integer productId) {
        final ProductDatabaseDbHelper db = new ProductDatabaseDbHelper(getApplicationContext());

        Cursor cursor = db.getData(productId);


        /* Referenced http://stackoverflow.com/questions/10244222/android-database-cursorindexoutofboundsexception-index-0-requested-with-a-size
         * when I got an error with Cursor.
         */

        if (cursor != null && cursor.moveToFirst()) {
            String productName = cursor.getString(cursor.getColumnIndex(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_NAME));
            Integer productQuantity = cursor.getInt(cursor.getColumnIndex(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_QUANTITY));
            Float productPrice = cursor.getFloat(cursor.getColumnIndex(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_PRICE));
            String imageURIString = cursor.getString(cursor.getColumnIndex(ProductDatabaseContract.ProductDatabase.COLUMN_NAME_PRODUCT_IMAGE));

            cursor.close();

            currentProduct = new Product(productId, productName, productPrice, productQuantity, imageURIString);

            TextView productDetailNameTextView = (TextView) findViewById(R.id.product_detail_name_textview);
            productDetailNameTextView.setText(productName);

            TextView productDetailQuantityTextView = (TextView) findViewById(R.id.product_detail_quantity_textview);
            productDetailQuantityTextView.setText(productQuantity.toString());

            TextView productDetailPriceTextView = (TextView) findViewById(R.id.product_detail_price_textview);
            productDetailPriceTextView.setText(NumberFormat.getCurrencyInstance().format(productPrice));

            TextView productDetailIdTextView = (TextView) findViewById(R.id.product_detail_id_textview);
            productDetailIdTextView.setText(productId.toString());

            ImageView productDetailImageView = (ImageView) findViewById(R.id.product_detail_imageview);
            productDetailImageView.setImageURI(Uri.parse(imageURIString));
        }
    }
}
