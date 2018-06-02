package com.meh.stuff.popularmovie.task;

import android.os.AsyncTask;
import android.util.Log;

import com.meh.stuff.popularmovie.data.Person;
import com.meh.stuff.popularmovie.listener.DownloadPersonListener;
import com.meh.stuff.popularmovie.utility.DataUtils;
import com.meh.stuff.popularmovie.utility.NetworkUtils;

import java.net.URL;

public class DownloadPersonTask extends AsyncTask<Void, Void, Person> {

    private static final String TAG = DownloadPersonTask.class.getSimpleName();

    private String apiKey;
    private String personId;
    private DownloadPersonListener downloadPersonListener;

    public DownloadPersonTask(DownloadPersonListener downloadPersonListener) {
        this.downloadPersonListener = downloadPersonListener;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        downloadPersonListener.onStartDownloadingPerson();
    }

    @Override
    protected Person doInBackground(Void... voids) {
        Person person = null;
        try {
            // Create the url for person
            URL personUrl = NetworkUtils.createPersonUrl(personId, apiKey);

            // Get the person data
            String personJsonString = NetworkUtils.getResponseFromHttpUrl(personUrl);

            // Convert person data to the person object
            person = DataUtils.processPersonData(personJsonString);
        } catch (Exception e) {
            Log.e(TAG, "Unable to download person.", e);
        }

        return person;
    }

    @Override
    protected void onPostExecute(Person person) {
        super.onPostExecute(person);
        downloadPersonListener.onDownloadingPersonCompleted(person, personId);
    }
}
