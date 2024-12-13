package com.example.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter newsAdapter;
    private List<Article> articleList;
    private Retrofit retrofit;

    private Map<String, List<String>> categoryKeywords = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        articleList = new ArrayList<>();

        setupCategoryKeywords();

        retrofit = ApiClient.getClient();
        ApiService apiService = retrofit.create(ApiService.class);


        apiService.getTopHeadlines("us", "general", "a76f3be0acc44130aea600b277037630").enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Article> articles = response.body().getArticles();
                    articleList.addAll(articles);
                    newsAdapter = new Adapter(MainActivity.this, articleList);
                    recyclerView.setAdapter(newsAdapter);
                } else {
                    Log.e("NewsApp", "Response unsuccessful or empty");
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("NewsApp", "Error fetching news", t);
            }
        });

        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                filterArticlesByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

       private void setupCategoryKeywords() {
        List<String> sportKeywords = Arrays.asList("football", "basketball", "sport", "match");
        List<String> politiqueKeywords = Arrays.asList("élection", "gouvernement", "loi", "politique");
        List<String> artKeywords = Arrays.asList("exposition", "art", "peinture");

        categoryKeywords.put("sport", sportKeywords);
        categoryKeywords.put("politique", politiqueKeywords);
        categoryKeywords.put("art", artKeywords);
    }

    private void filterArticlesByCategory(String category) {
        List<String> keywords = categoryKeywords.get(category.toLowerCase());

        if (keywords != null && !keywords.isEmpty()) {
            List<Article> filteredArticles = new ArrayList<>();

            for (Article article : articleList) {
                for (String keyword : keywords) {
                    if (article.getTitle().toLowerCase().contains(keyword) ||
                            article.getDescription().toLowerCase().contains(keyword)) {
                        filteredArticles.add(article);
                        break;
                    }
                }
            }

            if (newsAdapter != null) {
               // newsAdapter.updateArticles(filteredArticles);
            } else {
                Log.e("NewsApp", "Adapter is not initialized");
            }
        }
    }
}
