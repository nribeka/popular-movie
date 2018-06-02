package com.meh.stuff.popularmovie.task;

import android.os.AsyncTask;
import android.util.Log;

import com.meh.stuff.popularmovie.data.Cast;
import com.meh.stuff.popularmovie.listener.DownloadCreditsListener;
import com.meh.stuff.popularmovie.utility.DataUtils;
import com.meh.stuff.popularmovie.utility.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadCreditsTask extends AsyncTask<Void, Void, List<Cast>> {

    private static final String TAG = DownloadCreditsTask.class.getSimpleName();

    private String apiKey;
    private String movieId;
    private DownloadCreditsListener downloadCreditsListener;

    public DownloadCreditsTask(DownloadCreditsListener downloadCreditsListener) {
        this.downloadCreditsListener = downloadCreditsListener;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        downloadCreditsListener.onStartDownloadingCredits();
    }

    @Override
    protected List<Cast> doInBackground(Void... voids) {
        List<Cast> casts = new ArrayList<>();
        try {
            // Create the url for credits
            URL creditsUrl = NetworkUtils.createCreditsUrl(movieId, apiKey);

            // Get the all the credits data
            String creditsJsonString = NetworkUtils.getResponseFromHttpUrl(creditsUrl);

            // Convert credits data to the cast objects
            casts = DataUtils.processCreditsData(creditsJsonString);
        } catch (Exception e) {
            Log.e(TAG, "Unable to download movies.", e);
        }

        return casts;
    }

    @Override
    protected void onPostExecute(List<Cast> casts) {
        super.onPostExecute(casts);
        downloadCreditsListener.onDownloadingCreditsCompleted(casts, movieId);
    }
}
