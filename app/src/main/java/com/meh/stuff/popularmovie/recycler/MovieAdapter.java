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
import com.meh.stuff.popularmovie.data.Configuration;
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
    private Configuration configuration;

    private MovieAdapterListener movieAdapterListener;

    public MovieAdapter(Context context, MovieAdapterListener movieAdapterListener) {
        this.currentPage = 0;
        this.context = context;
        this.movies = new ArrayList<>();
        this.movieAdapterListener = movieAdapterListener;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        notifyDataSetChanged();
    }

    public void appendMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        this.currentPage = this.currentPage + 1;
        notifyDataSetChanged();
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
        if (configuration == null) {
            return;
        }

        Movie movie = movies.get(position);
        String baseImageUrl = configuration.getSecureBaseUrl();
        // only try to load if the movie poster is set, otherwise leave it as loading screen.
        if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            String imageUrl = baseImageUrl + "w185" + movie.getPosterUrl();
            Picasso.get().setIndicatorsEnabled(true);
            Picasso
                    .get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .fit()
                    .centerInside()
                    .into(holder.moviePoster);

            holder.progressBar.setVisibility(View.GONE);

            if (position >= movies.size() - MOVIE_DB_PAGE_SIZE) {
                movieAdapterListener.onOffsetHeightReached();
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
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = movies.get(adapterPosition);
            movieAdapterListener.onMovieSelected(selectedMovie);
        }
    }

}
