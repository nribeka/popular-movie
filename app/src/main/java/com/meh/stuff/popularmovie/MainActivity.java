package com.meh.stuff.popularmovie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meh.stuff.popularmovie.data.Config;
import com.meh.stuff.popularmovie.data.Movie;
import com.meh.stuff.popularmovie.listener.CheckConnectivityListener;
import com.meh.stuff.popularmovie.listener.DownloadConfigListener;
import com.meh.stuff.popularmovie.listener.DownloadMoviesListener;
import com.meh.stuff.popularmovie.listener.MovieAdapterListener;
import com.meh.stuff.popularmovie.recycler.MovieAdapter;
import com.meh.stuff.popularmovie.task.CheckConnectivityTask;
import com.meh.stuff.popularmovie.task.DownloadConfigTask;
import com.meh.stuff.popularmovie.task.DownloadMoviesTask;
import com.meh.stuff.popularmovie.utility.AppUtils;
import com.meh.stuff.popularmovie.utility.MovieOrdering;
import com.meh.stuff.popularmovie.utility.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity
        implements CheckConnectivityListener, DownloadMoviesListener, DownloadConfigListener, MovieAdapterListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOVIE_DB_KEY = "moviedb.key";

    private static final String CURRENT_PAGE_BUNDLE_KEY = "bundleKey.nextPageToDownload";
    private static final String CURRENT_MOVIES_BUNDLE_KEY = "bundleKey.currentMovies";

    private static final int CONNECTIVITY_CHECK_MAX_RETRY = 3;

    private static final int PORTRAIT_SPAN_COUNT = 2;
    private static final int LANDSCAPE_SPAN_COUNT = 4;

    private RecyclerView movieList;
    private ProgressBar loadingMovies;
    private TextView noInternetLabel;

    private MovieAdapter movieAdapter;
    private Properties appProperties;

    private int nextPageToDownload = 1;
    private int connectivityRetry = 0;
    private boolean deviceConnected = false;

    private volatile boolean shouldDownload = true;
    private volatile boolean downloadingConfig = false;
    private volatile boolean downloadingMovies = false;

    private MovieOrdering movieOrdering = MovieOrdering.POPULAR_MOVIE;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkInternetConnectionStatus();
        }
    };

    // Lifecycle methods.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingMovies = findViewById(R.id.pb_loading_movies);
        noInternetLabel = findViewById(R.id.tv_no_internet);

        int spanCount = PORTRAIT_SPAN_COUNT;
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            spanCount = LANDSCAPE_SPAN_COUNT;
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);

        movieList = findViewById(R.id.rv_movies);
        movieList.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(this, this);
        movieList.setAdapter(movieAdapter);

        // restore data if we have it in the db.
        restoreInstanceBundle(savedInstanceState);

        appProperties = AppUtils.loadApplicationProperties(this);
        if (!appProperties.containsKey(MOVIE_DB_KEY)) {
            Log.e(TAG, "Unable to find movie db key in the props file.");
        }

        checkInternetConnectionStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
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
        switch (id) {
            case R.id.action_settings:
                Context context = this;
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_now_playing:
                item.setChecked(true);
                changeSortSelection(MovieOrdering.NOW_PLAYING);
                return true;
            case R.id.action_popular:
                item.setChecked(true);
                changeSortSelection(MovieOrdering.POPULAR_MOVIE);
                return true;
            case R.id.action_top_rated:
                item.setChecked(true);
                changeSortSelection(MovieOrdering.TOP_RATED_MOVIE);
                return true;
            case R.id.action_upcoming:
                item.setChecked(true);
                changeSortSelection(MovieOrdering.UPCOMING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSortSelection(MovieOrdering movieOrdering) {
        this.nextPageToDownload = 1;
        this.movieOrdering = movieOrdering;
        this.movieAdapter = new MovieAdapter(this, this);
        this.movieList.setAdapter(movieAdapter);
        checkInternetConnectionStatus();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        ArrayList<Movie> movies = (ArrayList<Movie>) movieAdapter.getMovies();
        savedInstanceState.putParcelableArrayList(CURRENT_MOVIES_BUNDLE_KEY, movies);
        savedInstanceState.putInt(CURRENT_PAGE_BUNDLE_KEY, nextPageToDownload);
    }

    // Utility methods
    private void restoreInstanceBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_MOVIES_BUNDLE_KEY)) {
                List<Movie> movies = savedInstanceState.getParcelableArrayList(CURRENT_MOVIES_BUNDLE_KEY);
                movieAdapter.appendMovies(movies);
            }

            if (savedInstanceState.containsKey(CURRENT_PAGE_BUNDLE_KEY)) {
                nextPageToDownload = savedInstanceState.getInt(CURRENT_PAGE_BUNDLE_KEY, 1);
            }

            // When we're restoring state, we don't want to download the next page.
            shouldDownload = false;
        }
    }

    private void checkInternetConnectionStatus() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            startCheckConnectivityTask();
        } else {
            movieList.setVisibility(View.GONE);
            loadingMovies.setVisibility(View.GONE);
            noInternetLabel.setVisibility(View.VISIBLE);
        }
    }

    private void retryCheckingConnectivity() {
        if (connectivityRetry < CONNECTIVITY_CHECK_MAX_RETRY) {
            startCheckConnectivityTask();
        }
    }

    // Task starter methods.
    private void startCheckConnectivityTask() {
        CheckConnectivityTask checkConnectivityTask = new CheckConnectivityTask(this);
        checkConnectivityTask.execute();
    }

    private void startDownloadConfigTask() {
        if (!downloadingConfig) {
            DownloadConfigTask downloadConfigTask = new DownloadConfigTask(this);
            downloadConfigTask.setApiKey(appProperties.getProperty(MOVIE_DB_KEY));
            downloadConfigTask.execute();
        }
    }

    private void startDownloadMovieTask() {
        if (shouldDownload && !downloadingMovies) {
            DownloadMoviesTask downloadMoviesTask = new DownloadMoviesTask(movieOrdering, this);
            downloadMoviesTask.setApiKey(appProperties.getProperty(MOVIE_DB_KEY));
            downloadMoviesTask.setPage(nextPageToDownload);
            downloadMoviesTask.execute();
        }
    }

    // Listener implementation methods.
    @Override
    public void onDeviceConnected(final boolean deviceConnected) {
        this.deviceConnected = deviceConnected;
        connectivityRetry = connectivityRetry + 1;
        if (this.deviceConnected) {
            // we have internet, download the movies
            movieList.setVisibility(View.VISIBLE);
            loadingMovies.setVisibility(View.GONE);
            noInternetLabel.setVisibility(View.GONE);
            startDownloadConfigTask();
            startDownloadMovieTask();
            // reset the connectivity retry count
            connectivityRetry = 0;
        } else {
            // no internet, display the no internet message.
            movieList.setVisibility(View.GONE);
            loadingMovies.setVisibility(View.GONE);
            noInternetLabel.setVisibility(View.VISIBLE);
            // should we retry checking checking the connectivity?
            retryCheckingConnectivity();
        }
    }

    @Override
    public void onStartDownloadingMovies() {
        downloadingMovies = true;
    }

    @Override
    public void onDownloadingMoviesCompleted(List<Movie> movies) {
        movieAdapter.appendMovies(movies);
        downloadingMovies = false;
        nextPageToDownload++;
    }

    @Override
    public void onStartDownloadingConfig() {
        downloadingConfig = true;
    }

    @Override
    public void onDownloadingConfigCompleted(final Config config) {
        movieAdapter.setConfig(config);

        String preferenceKey = getResources().getString(R.string.preference_base_url_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences
                .edit()
                .putString(preferenceKey, config.getSecureBaseUrl())
                .apply();

        downloadingConfig = false;
    }

    @Override
    public void onOffsetHeightReached() {
        // we almost reached the end of the recycler view, we should download more.
        shouldDownload = true;
        if (deviceConnected) {
            startDownloadMovieTask();
        }
    }

    @Override
    public void onMovieSelected(final Movie movie) {
        Config config = movieAdapter.getConfig();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_CONFIG_BASE_URL, config.getSecureBaseUrl());
        intent.putExtra(DetailActivity.EXTRA_KEY_MOVIE, movie);
        startActivity(intent);
    }
}
