package com.meh.stuff.popularmovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.meh.stuff.popularmovie.database.genre.GenreContract;
import com.meh.stuff.popularmovie.database.movie.MovieContract;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_MOVIE_TABLE = ""
            + " CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ( "
            + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieContract.MovieEntry.COLUMN_ID + " INTEGER NOT NULL, "

            + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, "

            + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL,"

            + MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "

            + MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "

            + MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL,"
            + MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL,"
            + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "

            + MovieContract.MovieEntry.COLUMN_ADULT + " INTEGER DEFAULT 0, "
            + " UNIQUE (" + MovieContract.MovieEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";


    private static final String CREATE_GENRE_TABLE = ""
            + " CREATE TABLE " + GenreContract.GenreEntry.TABLE_NAME + " ( "
            + GenreContract.GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GenreContract.GenreEntry.COLUMN_ID + " INTEGER NOT NULL, "
            + GenreContract.GenreEntry.COLUMN_NAME + " TEXT NOT NULL, "
            + " UNIQUE (" + GenreContract.GenreEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

    public static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(CREATE_GENRE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GenreContract.GenreEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}