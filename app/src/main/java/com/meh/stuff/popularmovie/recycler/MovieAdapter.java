package com.meh.stuff.popularmovie.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.meh.stuff.popularmovie.R;
import com.meh.stuff.popularmovie.data.Config;
import com.meh.stuff.popularmovie.data.Movie;
import com.meh.stuff.popularmovie.listener.MovieAdapterListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private static final int MOVIE_DB_PAGE_SIZE = 20;
    private int currentPage;

    private Context context;
    private List<Movie> movies;
    private Config config;

    private MovieAdapterListener movieAdapterListener;

    public MovieAdapter(Context context, MovieAdapterListener movieAdapterListener) {
        this.currentPage = 0;
        this.context = context;
        this.movies = new ArrayList<>();
        this.movieAdapterListener = movieAdapterListener;
    }

    public void setConfig(Config config) {
        this.config = config;
        notifyDataSetChanged();
    }

    public Config getConfig() {
        return config;
    }

    public void appendMovies(List<Movie> movies) {
        if (this.movies.isEmpty()) {
            setMovies(movies);
        } else {
            this.movies.addAll(movies);
        }
        this.currentPage = this.currentPage + 1;
        notifyDataSetChanged();
    }

    private void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (config == null) {
            return;
        }

        Movie movie = movies.get(position);
        String baseImageUrl = config.getSecureBaseUrl();
        // only try to load if the movie poster is set, otherwise leave it as loading screen.
        if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
            String imageUrl = baseImageUrl + "w185" + movie.getPosterPath();
            Picasso
                    .get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .fit()
                    .centerInside()
                    .into(holder.moviePoster);
            holder.moviePoster.setContentDescription(movie.getTitle());
            holder.progressBar.setVisibility(View.GONE);

            if (position >= movies.size() - MOVIE_DB_PAGE_SIZE) {
                movieAdapterListener.onOffsetHeightReached();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null && config != null) {
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
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = movies.get(adapterPosition);
            movieAdapterListener.onMovieSelected(selectedMovie);
        }
    }

}
