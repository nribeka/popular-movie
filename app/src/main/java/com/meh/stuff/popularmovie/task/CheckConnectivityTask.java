package com.meh.stuff.popularmovie.task;

import android.os.AsyncTask;

import com.meh.stuff.popularmovie.listener.CheckConnectivityListener;

public class CheckConnectivityTask extends AsyncTask<Void, Void, Boolean> {

    private CheckConnectivityListener checkConnectivityListener;

    @Override
    protected Boolean doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Boolean connected) {
        super.onPostExecute(connected);
        checkConnectivityListener.deviceConnected(connected);
    }
}
