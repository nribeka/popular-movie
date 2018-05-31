package com.meh.stuff.popularmovie.listener;

import com.meh.stuff.popularmovie.data.Config;

public interface DownloadConfigListener {
    void onStartDownloadingConfig();
    void onDownloadingConfigCompleted(Config config);
}
