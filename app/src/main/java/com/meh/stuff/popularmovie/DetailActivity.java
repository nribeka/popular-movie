package com.meh.stuff.popularmovie;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.meh.stuff.popularmovie.data.Movie;
import com.squareup.picasso.Picasso;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_MOVIE = "extraKey.movie";
    public static final String EXTRA_CONFIG_BASE_URL = "extraKey.baseUrl";

    private Movie movie;
    private String baseImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView moviePoster = findViewById(R.id.iv_movie_detail);

        TextView textTitle = findViewById(R.id.tv_title);
        TextView textRating = findViewById(R.id.tv_rating);
        TextView textSynopsis = findViewById(R.id.tv_synopsis);
        TextView textReleaseDate = findViewById(R.id.tv_release_date);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_KEY_MOVIE)) {
                movie = intent.getParcelableExtra(EXTRA_KEY_MOVIE);
            }

            if (intent.hasExtra(EXTRA_CONFIG_BASE_URL)) {
                baseImageUrl = intent.getStringExtra(EXTRA_CONFIG_BASE_URL);
            }
        }

        if (movie != null) {

            int placeholder = R.drawable.ic_backdrop;
            String imageUrl = baseImageUrl + "w500" + movie.getBackdropPath();
            if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                placeholder = R.drawable.ic_placeholder;
                imageUrl = baseImageUrl + "w500" + movie.getPosterPath();
            }

            Picasso
                    .get()
                    .load(imageUrl)
                    .placeholder(placeholder)
                    .fit()
                    .centerInside()
                    .into(moviePoster);

            textTitle.setText(movie.getTitle());

            String rating = String.format(getResources().getString(R.string.rating_default_text), movie.getRating());
            textRating.setText(rating);

            textSynopsis.setText(movie.getSynopsis());
            textReleaseDate.setText(movie.getFormattedReleaseDate());
        }
    }
}
