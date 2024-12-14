package com.example.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter newsAdapter;
    private List<Article> articleList;
    private List<String> categories;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        articleList = new ArrayList<>();

        categories = new ArrayList<>();
        categories.add("Business");
        categories.add("Entertainment");
        categories.add("General");
        categories.add("Health");
        categories.add("Science");
        categories.add("Sports");
        categories.add("Technology");
        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        setupSpinner();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Actualités");
    }

    private void setupSpinner() {
        Spinner categorySpinner = findViewById(R.id.categorySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                fetchNewsByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void fetchNewsByCategory(String category) {
        if (isNetworkAvailable()) {
            apiService.getTopHeadlines("us", category, "a76f3be0acc44130aea600b277037630")
                    .enqueue(new Callback<NewsResponse>() {
                        @Override
                        public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Article> articles = response.body().getArticles();

                                List<Article> articlesWithImageAndDescription = new ArrayList<>();
                                List<Article> otherArticles = new ArrayList<>();

                                for (Article article : articles) {
                                    if (article.getUrlToImage() != null && article.getDescription() != null) {
                                        articlesWithImageAndDescription.add(article);
                                    } else {
                                        otherArticles.add(article);
                                    }
                                }

                                articlesWithImageAndDescription.addAll(otherArticles);

                                articleList.clear();
                                articleList.addAll(articlesWithImageAndDescription);

                                if (newsAdapter == null) {
                                    newsAdapter = new Adapter(MainActivity.this, articleList);
                                    recyclerView.setAdapter(newsAdapter);
                                } else {
                                    newsAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.e("NewsApp", "Error in response: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<NewsResponse> call, Throwable t) {
                            Log.e("NewsApp", "Error fetching news", t);
                        }
                    });
        } else {
            Log.e("NewsApp", "No internet connection");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
