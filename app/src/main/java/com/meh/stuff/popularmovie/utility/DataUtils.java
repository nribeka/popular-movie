package com.meh.stuff.popularmovie.utility;

import android.util.Log;

import com.meh.stuff.popularmovie.data.Configuration;
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
            movie.setReleased(safeParseDate(movieResult.getString(Movie.MOVIE_RELEASE_DATE_KEY)));
            movie.setPosterUrl(movieResult.getString(Movie.MOVIE_POSTER_PATH_KEY));

            movies.add(movie);
        }

        return movies;
    }

    public static Configuration processConfigurationData(String configurationJsonString) throws JSONException {
        Configuration configuration = new Configuration();
        JSONObject configurationJson = new JSONObject(configurationJsonString);

        JSONObject imagesConfiguration = configurationJson.getJSONObject(Configuration.BASE_CONFIGURATION_KEY);
        configuration.setBaseUrl(imagesConfiguration.getString(Configuration.IMAGE_BASE_URL));
        configuration.setSecureBaseUrl(imagesConfiguration.getString(Configuration.IMAGE_SECURE_BASE_URL));

        return configuration;
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
