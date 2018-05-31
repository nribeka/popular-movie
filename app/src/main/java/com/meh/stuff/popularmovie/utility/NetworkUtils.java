package com.meh.stuff.popularmovie.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    private static final String PATH_CONFIG = "configuration";
    private static final String PATH_MOVIE = "movie";

    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_PAGE = "page";

    private NetworkUtils() {
        // utility class, don't instantiate.
    }

    public static URL createConfigUrl(String apiKey) {
        return buildConfigUrl(apiKey);
    }

    public static URL createMovieUrl(MovieOrdering movieOrdering, String apiKey, Integer page) {
        if (page == null) {
            return buildMovieUrl(movieOrdering, apiKey, 1);
        }
        return buildMovieUrl(movieOrdering, apiKey, page);
    }

    private static URL buildConfigUrl(String apiKey) {
        Uri configUri = Uri.parse(MOVIE_DB_BASE_URL)
                .buildUpon()
                .appendPath(PATH_CONFIG)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        try {
            URL configUrl = new URL(configUri.toString());
            Log.i(TAG, "Config url: " + configUrl.toString());
            return configUrl;
        } catch (IOException e) {
            Log.e(TAG, "Unable to create url with the specified params.", e);
            return null;
        }
    }

    // Based on the sunshine project code to generate the url
    private static URL buildMovieUrl(MovieOrdering movieOrdering, String apiKey, Integer page) {
        Uri movieUri = Uri.parse(MOVIE_DB_BASE_URL)
                .buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(movieOrdering.toString())
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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
