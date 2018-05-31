package com.meh.stuff.popularmovie.task;

import android.os.AsyncTask;
import android.util.Log;

import com.meh.stuff.popularmovie.data.Config;
import com.meh.stuff.popularmovie.listener.DownloadConfigListener;
import com.meh.stuff.popularmovie.utility.DataUtils;
import com.meh.stuff.popularmovie.utility.NetworkUtils;

import java.net.URL;

public class DownloadConfigTask extends AsyncTask<Void, Void, Config> {

    private static final String TAG = DownloadConfigTask.class.getSimpleName();

    private String apiKey;
    private DownloadConfigListener downloadConfigListener;

    public DownloadConfigTask(DownloadConfigListener downloadConfigListener) {
        this.downloadConfigListener = downloadConfigListener;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG, "Starting configuration downloader ...");
        downloadConfigListener.onStartDownloadingConfig();
    }

    @Override
    protected Config doInBackground(Void... params) {
        try {
            // Create the url for movie db
            URL configurationUrl = NetworkUtils.createConfigUrl(apiKey);

            // Get the all the movie data
            String configurationJsonString = NetworkUtils.getResponseFromHttpUrl(configurationUrl);

            // Convert movie data to the movie object
            return DataUtils.processConfigData(configurationJsonString);
        } catch (Exception e) {
            Log.e(TAG, "Unable to download configuration information from movie db.", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Config config) {
        super.onPostExecute(config);
        downloadConfigListener.onDownloadingConfigCompleted(config);
    }
}
