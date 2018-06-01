package com.meh.stuff.popularmovie.listener;

import com.meh.stuff.popularmovie.data.Movie;
import com.meh.stuff.popularmovie.utility.MovieOrdering;

import java.util.List;

public interface DownloadMoviesListener {
    void onStartDownloadingMovies();
    void onDownloadingMoviesCompleted(List<Movie> movies, MovieOrdering movieOrdering, int page);
}
