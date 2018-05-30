package com.meh.stuff.popularmovie.listener;

import com.meh.stuff.popularmovie.data.Movie;

public interface MovieAdapterListener {
    void onOffsetHeightReached();
    void onMovieSelected(Movie movie);
}
