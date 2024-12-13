package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.NewsViewHolder> {
    private Context context;
    private List<Article> newsList;

    public Adapter(Context context, List<Article> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        Article newsItem = newsList.get(position);

        Glide.with(context)
                .load(newsItem.getUrlToImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.newsImage);

        holder.title.setText(newsItem.getSource().getName());
        holder.title.setTextColor(Color.RED);
        holder.title.setGravity(View.TEXT_ALIGNMENT_CENTER);
        holder.title.setTypeface(null, Typeface.BOLD);

        holder.description.setText(newsItem.getDescription());
        holder.author.setText(newsItem.getAuthor());

        // Ajout de l'écouteur de clic pour rediriger vers la page de détails
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("article", newsItem); // Passer l'article à l'activité
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView newsImage;
        public TextView title;
        public TextView description;
        public TextView author;

        public NewsViewHolder(View view) {
            super(view);
            newsImage = view.findViewById(R.id.newsImage);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            author = view.findViewById(R.id.author);
        }
    }
}
