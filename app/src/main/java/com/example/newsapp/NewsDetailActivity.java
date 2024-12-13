    package com.example.newsapp;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;

    import com.bumptech.glide.Glide;

    public class NewsDetailActivity extends AppCompatActivity {
        private TextView title, description, author;
        private ImageView imageView;
        private Button shareButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news_detail);

            title = findViewById(R.id.title);
            description = findViewById(R.id.description);
            author = findViewById(R.id.author);
            imageView = findViewById(R.id.newsImage);
            shareButton = findViewById(R.id.share_button);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Récupérer l'article envoyé via Intent
            Article article = (Article) getIntent().getSerializableExtra("article");

            if (article != null) {
                title.setText(article.getTitle());
                description.setText(article.getDescription());
                author.setText(article.getAuthor());

                // Charger l'image avec Glide
                Glide.with(this)
                        .load(article.getUrlToImage())
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
            }

            // Mettre en place l'action de partage sur le bouton
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (article != null) {
                        shareNews(article);
                    }
                }
            });
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.action_share) {
                shareContent();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void shareContent() {
            // Récupérer l'article envoyé via Intent
            Article article = (Article) getIntent().getSerializableExtra("article");

            if (article != null) {
                // Créer une chaîne de texte qui combine le titre, la description et l'URL de l'image
                String contentToShare = "Check out this article:\n\n"
                        + "Title: " + article.getTitle() + "\n"
                        + "Description: " + article.getDescription() + "\n" ;


                // Créer un Intent pour partager l'article
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, contentToShare);

                // Lancer l'activité de partage
                startActivity(Intent.createChooser(shareIntent, "Partager via"));
            }
        }




        private void shareNews(Article article) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, article.getUrlToImage());
            startActivity(Intent.createChooser(shareIntent, "Share News"));
        }
    }
