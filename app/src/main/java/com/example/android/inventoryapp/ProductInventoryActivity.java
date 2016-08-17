package com.example.android.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class ProductInventoryActivity extends AppCompatActivity {

    TextView noProductsFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_inventory);

        noProductsFound = (TextView) findViewById(R.id.no_product_textView);

        displayListOfProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayListOfProducts();
    }


    //Reference: http://www.higherpass.com/android/tutorials/accessing-data-with-android-cursors/2/
    public void displayListOfProducts() {

        final ProductDatabaseDbHelper db = new ProductDatabaseDbHelper(getApplicationContext());

        ArrayList<Product> productArrayList = db.getAllProducts();

        if (productArrayList != null) {
            if (productArrayList.isEmpty()) {
                noProductsFound.setVisibility(View.VISIBLE);
            } else {
            noProductsFound.setVisibility(View.INVISIBLE);
            ProductAdapter adapter = new ProductAdapter(this, productArrayList);

            ListView productListView = (ListView) findViewById(R.id.product_inventory_list);
            productListView.setAdapter(adapter);

            //Found code at http://www.ezzylearning.com/tutorial/handling-android-listview-onitemclick-event
            productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    Intent productIntent = new Intent(ProductInventoryActivity.this, ProductDetailActivity.class);
                    TextView productIdTextView = (TextView) view.findViewById(R.id.id_textView);
                    String productId = productIdTextView.getText().toString();

                    productIntent.putExtra("id", productId);

                    startActivity(productIntent);
                }
            });
            }
        } else {
            noProductsFound.setVisibility(View.VISIBLE);
        }
    }
}
