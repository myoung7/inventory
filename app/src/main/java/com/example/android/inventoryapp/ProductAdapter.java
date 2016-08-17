package com.example.android.inventoryapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Matthew Young on 7/1/16.
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View productItemView = convertView;

        if (productItemView == null) {
            productItemView = LayoutInflater.from(getContext()).inflate(R.layout.product_inventory_listview,
                    parent, false);
        }

        final Product currentProduct = getItem(position);

        TextView productNameTextView = (TextView) productItemView.findViewById(R.id.product_name_textView);
        productNameTextView.setText(currentProduct.getName());

        TextView productIdTextView = (TextView) productItemView.findViewById(R.id.id_textView);
        String idString = currentProduct.getId().toString();
        productIdTextView.setText(idString);

        TextView productPriceTextView = (TextView) productItemView.findViewById(R.id.price_amount_textView);
        String priceString = NumberFormat.getCurrencyInstance().format(currentProduct.getPrice());
        productPriceTextView.setText(priceString);

        TextView productQuantityTextView = (TextView) productItemView.findViewById(R.id.quantity_amount_textView);
        String quantityString = currentProduct.getQuantity().toString();
        productQuantityTextView.setText(quantityString);

        Button saleButton = (Button) productItemView.findViewById(R.id.list_sale_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductDatabaseDbHelper db = new ProductDatabaseDbHelper(getContext().getApplicationContext());

                Product updatedProduct = currentProduct;

                if (currentProduct.getQuantity() >= 1) {
                    Integer updatedQuantity = updatedProduct.getQuantity() - 1;
                    updatedProduct.setQuantity(updatedQuantity);
                    db.updateProduct(updatedProduct);
                }
                notifyDataSetChanged();
            }
        });

        return productItemView;
    }
}
