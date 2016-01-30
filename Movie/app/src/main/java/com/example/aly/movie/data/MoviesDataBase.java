package com.example.aly.movie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aly on 06/01/2016.
 */
public class MoviesDataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1 ;

    public MoviesDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieEntry.COLUMN_IMAGE + " BLOB  NULL, "
                + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NULL, "
                + MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_POPULARITY + " TEXT NULL, "
                + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE +" TEXT NULL, "
                + MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " TEXT NULL, "
                + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NULL,"
                +" UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieEntry.TABLE_NAME);

        onCreate(db);
    }
}
