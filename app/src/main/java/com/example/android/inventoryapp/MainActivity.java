package com.example.android.inventoryapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    public static final int GET_FROM_GALLERY = 1;

    private String selectedImageURIString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button inventoryButton = (Button) findViewById(R.id.view_product_list_button);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProductInventoryActivity(view);
            }
        });

        Button selectImageButton = (Button) findViewById(R.id.select_image_button);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        GET_FROM_GALLERY);

            }
        });

    }

    /*
     * Referenced from: http://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImageURI = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageURI);

                ImageView imageView = (ImageView) findViewById(R.id.product_imageview);
                imageView.setImageURI(selectedImageURI);
                selectedImageURIString = selectedImageURI.toString();
                System.out.println(selectedImageURI);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadProductInventoryActivity(View view) {
        Intent productInventoryActivityIntent = new Intent(MainActivity.this, ProductInventoryActivity.class);
        startActivity(productInventoryActivityIntent);
    }

    public void addProductToDatabase(View view) {
        String productName;
        Integer productId;
        Integer productQuantity;
        float productPrice;

        EditText productNameEditText = (EditText) findViewById(R.id.product_name_editText);
        EditText productIdEditText = (EditText) findViewById(R.id.product_id_editText);
        EditText productQuantityEditText = (EditText) findViewById(R.id.product_quantity_editText);
        EditText productPriceEditText = (EditText) findViewById(R.id.product_price_editText);

        if (isEmpty(productNameEditText) || isEmpty(productIdEditText) ||
                isEmpty(productQuantityEditText) || isEmpty(productPriceEditText) || selectedImageURIString.isEmpty()) {
            Toast insertProductErrorToast = Toast.makeText(this, getString(R.string.missing_fields), Toast.LENGTH_SHORT);
            insertProductErrorToast.show();
        } else {

            productName = productNameEditText.getText().toString();
            productId = Integer.parseInt(productIdEditText.getText().toString());
            productQuantity = Integer.parseInt(productQuantityEditText.getText().toString());
            productPrice = Float.parseFloat(productPriceEditText.getText().toString());

            Product newProduct = new Product(productId, productName, productPrice, productQuantity, selectedImageURIString);

            ProductDatabaseDbHelper db = new ProductDatabaseDbHelper(getApplicationContext());

            boolean didInsertProduct = db.insertProduct(newProduct);

            if (!didInsertProduct) {
                Toast insertProductErrorToast = Toast.makeText(this, getString(R.string.error_insert_product), Toast.LENGTH_SHORT);
                insertProductErrorToast.show();
            } else {
                Toast insertProductSuccessToast = Toast.makeText(this, getString(R.string.success_insert_product), Toast.LENGTH_SHORT);
                insertProductSuccessToast.show();
            }
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
