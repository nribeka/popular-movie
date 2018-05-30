package com.meh.stuff.popularmovie.task;

import android.os.AsyncTask;
import android.util.Log;

import com.meh.stuff.popularmovie.data.Movie;
import com.meh.stuff.popularmovie.listener.DownloadMoviesListener;
import com.meh.stuff.popularmovie.utility.DataUtils;
import com.meh.stuff.popularmovie.utility.MovieOrdering;
import com.meh.stuff.popularmovie.utility.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

    private static final String TAG = DownloadMoviesTask.class.getSimpleName();

    private Integer page;
    private String apiKey;
    private MovieOrdering movieOrdering;
    private DownloadMoviesListener downloadMoviesListener;

    public DownloadMoviesTask(MovieOrdering movieOrdering, DownloadMoviesListener downloadMoviesListener) {
        this.movieOrdering = movieOrdering;
        this.downloadMoviesListener = downloadMoviesListener;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG, "Starting movie downloader ...");
        downloadMoviesListener.onStartDownloadingMovies();
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        List<Movie> movies = new ArrayList<>();
        try {
            // Create the url for movie db
            URL movieUrl = NetworkUtils.createMovieUrl(movieOrdering, apiKey, page);

            // Get the all the movie data
            String movieJsonString = NetworkUtils.getResponseFromHttpUrl(movieUrl);

            // Convert movie data to the movie object
            movies = DataUtils.processMovieData(movieJsonString);
        } catch (Exception e) {
            Log.e(TAG, "Unable to download movies.", e);
        }

        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        downloadMoviesListener.onDownloadingMoviesCompleted(movies);
    }
}
