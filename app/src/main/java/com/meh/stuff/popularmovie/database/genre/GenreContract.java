package com.meh.stuff.popularmovie.database.genre;

import android.net.Uri;
import android.provider.BaseColumns;

import com.meh.stuff.popularmovie.database.ContractConstant;

public class GenreContract {

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + ContractConstant.CONTENT_AUTHORITY);
    public static final String PATH_GENRE = "genre";

    private GenreContract() {
    }

    public static final class GenreEntry implements BaseColumns {

        // Full content uri will become: content://com.meh.stuff.popularmovie/genre
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_GENRE)
                .build();

        public static final String TABLE_NAME = "genre";

        // id (long)
        public static final String COLUMN_ID = "id";

        // name (string)
        public static final String COLUMN_NAME = "name";

        private GenreEntry() {
        }
    }
}
