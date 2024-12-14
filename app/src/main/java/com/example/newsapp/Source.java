package com.example.newsapp;

import android.os.Parcel;
import java.io.Serializable;


public class Source implements Serializable {
    private String id;
    private String name;
    private String category;


    protected Source(Parcel in) {
        id = in.readString();
        name = in.readString();
    }


    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }
}
