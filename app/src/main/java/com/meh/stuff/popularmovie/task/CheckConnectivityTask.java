package com.meh.stuff.popularmovie.task;

import android.os.AsyncTask;
import android.util.Log;

import com.meh.stuff.popularmovie.listener.CheckConnectivityListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class CheckConnectivityTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = CheckConnectivityTask.class.getSimpleName();

    private static final int GOOGLE_DNS_PORT = 53;
    private static final String GOOGLE_DNS_IP = "8.8.8.8";

    private CheckConnectivityListener checkConnectivityListener;

    public CheckConnectivityTask(CheckConnectivityListener checkConnectivityListener) {
        this.checkConnectivityListener = checkConnectivityListener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            int timeoutMs = 1500;
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(GOOGLE_DNS_IP, GOOGLE_DNS_PORT);

            socket.connect(socketAddress, timeoutMs);
            socket.close();

            return true;
        } catch (IOException e) {
            Log.e(TAG, "Unable to communicate with google public dns.", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean connected) {
        super.onPostExecute(connected);
        checkConnectivityListener.onDeviceConnected(connected);
    }
}
