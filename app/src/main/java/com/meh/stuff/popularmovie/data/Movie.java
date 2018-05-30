package com.meh.stuff.popularmovie.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {

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
    private String rating;
    private String synopsis;
    private String posterUrl;

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Date getReleased() {
        return released;
    }

    public void setReleased(Date released) {
        this.released = released;
    }

    public Movie() {
    }

    private Movie(Parcel parcel) {
        id = parcel.readString();
        title = parcel.readString();
        rating = parcel.readString();
        synopsis = parcel.readString();
        posterUrl = parcel.readString();
        released = new Date(parcel.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getRating());
        parcel.writeString(getSynopsis());
        parcel.writeString(getPosterUrl());
        parcel.writeLong(getReleased().getTime());
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}