package com.meh.stuff.popularmovie.task;

import android.os.AsyncTask;
import android.util.Log;

import com.meh.stuff.popularmovie.data.Configuration;
import com.meh.stuff.popularmovie.listener.DownloadConfigurationListener;
import com.meh.stuff.popularmovie.utility.DataUtils;
import com.meh.stuff.popularmovie.utility.NetworkUtils;

import java.net.URL;

public class DownloadConfigurationTask extends AsyncTask<Void, Void, Configuration> {

    private static final String TAG = DownloadConfigurationTask.class.getSimpleName();

    private String apiKey;
    private DownloadConfigurationListener downloadConfigurationListener;

    public DownloadConfigurationTask(DownloadConfigurationListener downloadConfigurationListener) {
        this.downloadConfigurationListener = downloadConfigurationListener;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected Configuration doInBackground(Void... params) {
        try {
            // Create the url for movie db
            URL configurationUrl = NetworkUtils.createConfigurationUrl(apiKey);

            // Get the all the movie data
            String configurationJsonString = NetworkUtils.getResponseFromHttpUrl(configurationUrl);

            // Convert movie data to the movie object
            return DataUtils.processConfigurationData(configurationJsonString);
        } catch (Exception e) {
            Log.e(TAG, "Unable to download configuration information from movie db.", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Configuration configuration) {
        super.onPostExecute(configuration);
        downloadConfigurationListener.onConfigurationDownloaded(configuration);
    }
}
