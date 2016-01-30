package com.example.aly.movie.data;

/**
 * Created by aly on 06/01/2016.
 */
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class MovieContract {

    /*public static final String CONTENT_AUTHORITY = "com.example.aly.movie.app";

    public static final Uri Base_content_Uir = Uri.parse("content://" + CONTENT_AUTHORITY);*/
    /*
        Inner class that defines the table contents of the location table
        Students: This is where you will add the strings.  (Similar to what has been
        done for WeatherEntry)
     */
     //public static final String Movies_Path = "movies";
    /* Inner class that defines the table contents of the table table */
    public static final class MovieEntry implements BaseColumns {

       // public static final Uri uri = Base_content_Uir.buildUpon().appendPath(Movies_Path).build();

        //public static final String Content_Type = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/"+ Movies_Path ;
        //public static final String Content_Item_Type = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/"+ Movies_Path ;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_OVERVIEW = "overview";


        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";


        public static final String COLUMN_VOTE_COUNT = "vote_count";


        public static final String COLUMN_TITLE = "title";


        public static final String COLUMN_RELEASE_DATE = "release_date";

       /* public static Uri buildMoviesUri(long id){

            return ContentUris.withAppendedId(uri,id);
        }*/

    }
}
