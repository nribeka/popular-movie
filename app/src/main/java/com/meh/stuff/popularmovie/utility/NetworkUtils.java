package com.meh.stuff.popularmovie.utility;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/";

    private static final String PATH_CONFIGURATION = "configuration";
    private static final String PATH_MOVIE = "movie";

    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_PAGE = "page";

    private static final String DEFAULT_LANGUAGE = "en-US";

    private NetworkUtils() {
        // utility class, don't instantiate.
    }

    public static URL createConfigurationUrl(String apiKey) {
        return buildConfigurationUrl(apiKey);
    }

    public static URL createMovieUrl(MovieHint movieHint, String apiKey, Integer page) {
        if (page == null) {
            return buildMovieUrl(movieHint, apiKey, 1);
        }
        return buildMovieUrl(movieHint, apiKey, page);
    }

    private static URL buildConfigurationUrl(String apiKey) {
        Uri configurationUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(PATH_CONFIGURATION)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        try {
            URL configurationUrl = new URL(configurationUri.toString());
            Log.i(TAG, "Configuration url: " + configurationUrl.toString());
            return configurationUrl;
        } catch (IOException e) {
            Log.e(TAG, "Unable to create url with the specified params.", e);
            return null;
        }
    }

    // Based on the sunshine project code to generate the url
    private static URL buildMovieUrl(MovieHint movieHint, String apiKey, Integer page) {
        Uri movieUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(movieHint.toString())
                .appendQueryParameter(PARAM_LANGUAGE, DEFAULT_LANGUAGE)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(page))
                .build();

        try {
            URL movieUrl = new URL(movieUri.toString());
            Log.i(TAG, "Movies url: " + movieUrl.toString());
            return movieUrl;
        } catch (IOException e) {
            Log.e(TAG, "Unable to create url with the specified params.", e);
            return null;
        }
    }

    // Taken from the sunshine project NetworkUtils.java
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}
