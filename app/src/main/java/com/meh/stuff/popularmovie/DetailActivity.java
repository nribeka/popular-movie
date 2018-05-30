package com.meh.stuff.popularmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.meh.stuff.popularmovie.data.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_MOVIE = "extraKey.movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            Movie movie = intent.getParcelableExtra(EXTRA_KEY_MOVIE);
        }

        setContentView(R.layout.activity_detail);
    }
}
