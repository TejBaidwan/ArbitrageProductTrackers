package com.example.arbitragetracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents the structure of a Product object
 */
public class Product implements Parcelable {

    //Properties
    private int id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private int sold;

    //Constructor
    public Product(String name, String description, Double price, String imgUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }
    public Product(int id, String name, String description, Double price, String imgUrl, int soldStatus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.sold = soldStatus;
    }

    //No-args constructor
    public Product(){

    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int isSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    //Parcelable methods//

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeDouble(this.price);
        parcel.writeString(this.imgUrl);
        parcel.writeInt(this.sold);
    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        imgUrl = in.readString();
        sold = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
