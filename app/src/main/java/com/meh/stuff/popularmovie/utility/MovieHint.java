package com.meh.stuff.popularmovie.utility;

public enum MovieHint {

    POPULAR_MOVIE("popular"), TOP_RATED_MOVIE("top_rated");

    private String hint;

    MovieHint(String hint) {
        this.hint = hint;
    }

    public String toString() {
        return hint;
    }

}
