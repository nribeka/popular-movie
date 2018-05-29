package com.meh.stuff.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.meh.stuff.popularmovie.data.Configuration;
import com.meh.stuff.popularmovie.data.Movie;
import com.meh.stuff.popularmovie.listener.CheckConnectivityListener;
import com.meh.stuff.popularmovie.listener.DownloadConfigurationListener;
import com.meh.stuff.popularmovie.listener.DownloadMoviesListener;
import com.meh.stuff.popularmovie.recycler.MovieAdapter;
import com.meh.stuff.popularmovie.task.CheckConnectivityTask;
import com.meh.stuff.popularmovie.task.DownloadConfigurationTask;
import com.meh.stuff.popularmovie.task.DownloadMoviesTask;
import com.meh.stuff.popularmovie.utility.AppUtils;
import com.meh.stuff.popularmovie.utility.MovieHint;

import java.util.List;
import java.util.Properties;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity
        implements CheckConnectivityListener, DownloadMoviesListener, DownloadConfigurationListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOVIE_DB_KEY = "moviedb.key";

    private static final int PORTRAIT_SPAN_COUNT = 2;
    private static final int LANDSCAPE_SPAN_COUNT = 4;

    private TextView noInternetLabel;

    private RecyclerView movieList;
    private MovieAdapter movieAdapter;

    private Properties appProperties;

    private boolean networkConnectivityAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void checkNetworkStatus() {
        if (networkConnectivityAvailable()) {
            CheckConnectivityTask checkConnectivityTask = new CheckConnectivityTask(this);
            checkConnectivityTask.execute();
        } else {
            noInternetLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noInternetLabel = findViewById(R.id.tv_no_internet);

        movieList = findViewById(R.id.rv_movies);
        movieList.setHasFixedSize(true);

        int spanCount = PORTRAIT_SPAN_COUNT;
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            spanCount = LANDSCAPE_SPAN_COUNT;
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        movieList.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(this);
        movieList.setAdapter(movieAdapter);

        appProperties = AppUtils.loadApplicationProperties(this);
        if (!appProperties.containsKey(MOVIE_DB_KEY)) {
            Log.e(TAG, "Unable to find movie db key in the props file.");
        }

        checkNetworkStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Context context = this;
            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeviceConnected(boolean connected) {
        if (connected) {
            // we have internet, download the movies
            noInternetLabel.setVisibility(View.GONE);

            DownloadConfigurationTask downloadConfigurationTask = new DownloadConfigurationTask(this);
            downloadConfigurationTask.setApiKey(appProperties.getProperty(MOVIE_DB_KEY));
            downloadConfigurationTask.execute();

            DownloadMoviesTask downloadMoviesTask = new DownloadMoviesTask(MovieHint.POPULAR_MOVIE, this);
            downloadMoviesTask.setApiKey(appProperties.getProperty(MOVIE_DB_KEY));
            downloadMoviesTask.execute();
        } else {
            // no internet, display the no internet message.
            noInternetLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMoviesDownloaded(List<Movie> movies) {
        movieAdapter.setMovies(movies);
    }

    @Override
    public void onConfigurationDownloaded(Configuration configuration) {
        movieAdapter.setConfiguration(configuration);
    }
}
