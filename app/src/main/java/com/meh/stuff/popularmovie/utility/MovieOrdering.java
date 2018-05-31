package com.meh.stuff.popularmovie.utility;

public enum MovieOrdering {

    POPULAR_MOVIE("popular"), TOP_RATED_MOVIE("top_rated"), UPCOMING("upcoming"),
    NOW_PLAYING("now_playing");

    private String hint;

    MovieOrdering(String hint) {
        this.hint = hint;
    }

    public String toString() {
        return hint;
    }

}
