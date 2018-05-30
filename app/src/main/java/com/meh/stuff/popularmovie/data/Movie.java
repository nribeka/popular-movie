package com.meh.stuff.popularmovie.data;

import java.util.Date;

public class Movie {

    // The movie db json key to build this object
    public static final String BASE_MOVIE_KEY = "results";

    public static final String MOVIE_ID_KEY = "id";
    public static final String MOVIE_TITLE_KEY = "title";
    public static final String MOVIE_OVERVIEW_KEY = "overview";
    public static final String MOVIE_VOTE_AVERAGE_KEY = "vote_average";
    public static final String MOVIE_RELEASE_DATE_KEY = "release_date";
    public static final String MOVIE_POSTER_PATH_KEY = "poster_path";

    private String id;
    private String title;
    private String posterUrl;
    private String synopsis;
    private String rating;
    private Date released;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Date getReleased() {
        return released;
    }

    public void setReleased(Date released) {
        this.released = released;
    }
}