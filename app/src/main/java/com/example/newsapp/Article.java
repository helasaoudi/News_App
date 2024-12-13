package com.example.newsapp;
import com.example.newsapp.Source;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Article implements Serializable {
    @SerializedName("title")
    private String title;
    private String description;
    private String author;
    private String urlToImage;
    private String category;
    private Source source;

    public Article(String title, String description, String author, String urlToImage,Source name) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.urlToImage = urlToImage;
        this.category = category;
        this.source=name;

    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getCategory() {
        return category;
    }
}