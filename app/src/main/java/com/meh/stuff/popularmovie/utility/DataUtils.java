package com.meh.stuff.popularmovie.utility;

import android.util.Log;

import com.meh.stuff.popularmovie.data.Config;
import com.meh.stuff.popularmovie.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataUtils {

    private static final String TAG = DataUtils.class.getSimpleName();
    private static final DateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private DataUtils() {
        // utility class, don't instantiate.
    }

    public static List<Movie> processMovieData(String movieJsonString) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieJsonString);
        JSONArray movieResults = movieJson.getJSONArray(Movie.BASE_MOVIE_KEY);
        for (int i = 0; i < movieResults.length(); i++) {
            JSONObject movieResult = movieResults.getJSONObject(i);

            Movie movie = new Movie();
            movie.setId(movieResult.getString(Movie.MOVIE_ID_KEY));
            movie.setTitle(movieResult.getString(Movie.MOVIE_TITLE_KEY));
            movie.setSynopsis(movieResult.getString(Movie.MOVIE_OVERVIEW_KEY));
            movie.setRating(movieResult.getString(Movie.MOVIE_VOTE_AVERAGE_KEY));
            movie.setPosterPath(movieResult.getString(Movie.MOVIE_POSTER_PATH_KEY));
            movie.setBackdropPath(movieResult.getString(Movie.MOVIE_BACKDROP_PATH_KEY));

            movie.setReleaseDate(safeParseDate(movieResult.getString(Movie.MOVIE_RELEASE_DATE_KEY)));

            movies.add(movie);
        }

        return movies;
    }

    public static Config processConfigData(String configJsonString) throws JSONException {
        Config config = new Config();
        JSONObject configJson = new JSONObject(configJsonString);

        JSONObject imagesConfig = configJson.getJSONObject(Config.BASE_CONFIG_KEY);
        config.setBaseUrl(imagesConfig.getString(Config.IMAGE_BASE_URL));
        config.setSecureBaseUrl(imagesConfig.getString(Config.IMAGE_SECURE_BASE_URL));

        return config;
    }

    private static Date safeParseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse date data.", e);
            return null;
        }
    }
}
