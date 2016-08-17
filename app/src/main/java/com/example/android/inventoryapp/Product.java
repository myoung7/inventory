package com.example.android.inventoryapp;

/**
 * Created by Matthew Young on 7/1/16.
 */
public class Product {

    private Integer mId;
    private String mName;
    private float mPrice;
    private Integer mQuantity;
    private String mImageURIString;

    public Product(Integer id, String name, float price, Integer quantity, String imageURIString) {
        this.mId = id;
        this.mName = name;
        this.mPrice = price;
        this.mQuantity = quantity;
        this.mImageURIString = imageURIString;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float price) {
        this.mPrice = price;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Integer quantity) {
        this.mQuantity = quantity;
    }

    public String getImageURIString() {
        return mImageURIString;
    }

    public void setImageURIString(String imageURIString) {
        this.mImageURIString = imageURIString;
    }


}
