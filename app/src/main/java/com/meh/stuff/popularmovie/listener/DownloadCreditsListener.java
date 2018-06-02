package com.meh.stuff.popularmovie.listener;

import com.meh.stuff.popularmovie.data.Cast;

import java.util.List;

public interface DownloadCreditsListener {
    void onStartDownloadingCredits();
    void onDownloadingCreditsCompleted(List<Cast> casts, String movieId);
}
