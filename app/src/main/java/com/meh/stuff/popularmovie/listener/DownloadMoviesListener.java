package com.meh.stuff.popularmovie.listener;

import com.meh.stuff.popularmovie.data.Movie;

import java.util.List;

public interface DownloadMoviesListener {
    void onStartDownloadingMovies();
    void onDownloadingMoviesCompleted(List<Movie> movies);
}
