package com.meh.stuff.popularmovie.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.meh.stuff.popularmovie.R;
import com.meh.stuff.popularmovie.data.Configuration;
import com.meh.stuff.popularmovie.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private Context context;
    private List<Movie> movies;
    private Configuration configuration;

    public MovieAdapter(Context context) {
        this.context = context;
        this.movies = new ArrayList<>();
    }

    public void appendMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        notifyDataSetChanged();
    }

    public void addFakeMovie(Movie movie) {
        movies.add(movie);
        notifyDataSetChanged();
    }

    public void removeFakeMovie() {
        // the fake element will be the last element in the list.
        movies.remove(movies.size() - 1);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (getItemCount() > position) {
            String baseImageUrl = configuration.getSecureBaseUrl();
            Movie movie = movies.get(position);
            // only try to load if the movie poster is set, otherwise leave it as loading screen.
            if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
                String imageUrl = baseImageUrl + "w185" + movie.getPosterUrl();
                Log.i(TAG, "Picasso loading: " + imageUrl);
                Picasso.get().setIndicatorsEnabled(true);
                Picasso
                        .get()
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .fit()
                        .centerInside()
                        .into(holder.moviePoster);

                holder.progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null && configuration != null) {
            return movies.size();
        } else {
            return 0;
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView moviePoster;
        ProgressBar progressBar;

        MovieViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.pb_movie);
            moviePoster = itemView.findViewById(R.id.iv_movie);
            moviePoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Movie poster clicked", Toast.LENGTH_LONG).show();
        }
    }

}
