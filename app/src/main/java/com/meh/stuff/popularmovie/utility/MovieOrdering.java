package com.meh.stuff.popularmovie.utility;

public enum MovieOrdering {

    POPULAR_MOVIE("popular"), TOP_RATED_MOVIE("top_rated");

    private String hint;

    MovieOrdering(String hint) {
        this.hint = hint;
    }

    public String toString() {
        return hint;
    }

}
