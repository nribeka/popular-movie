package com.meh.stuff.popularmovie.listener;

import com.meh.stuff.popularmovie.data.Person;

public interface DownloadPersonListener {
    void onStartDownloadingPerson();
    void onDownloadingPersonCompleted(Person person, String personId);
}
