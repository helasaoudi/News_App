package com.example.newsapp;

import android.os.Parcel;
import java.io.Serializable;


public class Source implements Serializable {
    private String id;
    private String name;

    // Getters et setters

    protected Source(Parcel in) {
        id = in.readString();
        name = in.readString();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
