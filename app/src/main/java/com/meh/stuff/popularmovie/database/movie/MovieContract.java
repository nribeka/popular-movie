package com.meh.stuff.popularmovie.database.movie;

import android.net.Uri;
import android.provider.BaseColumns;

import com.meh.stuff.popularmovie.database.ContractConstant;

public class MovieContract {

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + ContractConstant.CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    private MovieContract() {
    }

    public static final class MovieEntry implements BaseColumns {

        // Full content uri will become: content://com.meh.stuff.popularmovie/movie
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        // id (long)
        public static final String COLUMN_ID = "id";

        // vote_count (long)
        public static final String COLUMN_VOTE_COUNT = "vote_count";

        // vote_average (double)
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        // title (string)
        public static final String COLUMN_TITLE = "title";

        // popularity (double)
        public static final String COLUMN_POPULARITY = "popularity";

        // poster_path (string)
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // original_language (string)
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";

        // original_title (string)
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        // backdrop_path (string)
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        // adult (boolean)
        public static final String COLUMN_ADULT = "adult";

        // overview: (string)
        public static final String COLUMN_OVERVIEW = "overview";

        // release_date: (iso date)
        public static final String COLUMN_RELEASE_DATE = "release_date";

        private MovieEntry() {
        }

    }

}
