package com.meh.stuff.popularmovie.listener;

import com.meh.stuff.popularmovie.data.Configuration;

public interface DownloadConfigurationListener {
    void onStartDownloadingConfig();
    void onDownloadingConfigCompleted(Configuration configuration);
}
