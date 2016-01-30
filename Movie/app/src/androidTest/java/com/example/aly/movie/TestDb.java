package com.example.aly.movie;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.aly.movie.data.MoviesDataBase;

/**
 * Created by aly on 06/01/2016.
 */
public class TestDb extends AndroidTestCase{

    public static final String LogTag = TestDb.class.getSimpleName();

    public void testCreateDb()throws Throwable{
        mContext.deleteDatabase(MoviesDataBase.DATABASE_NAME);
        SQLiteDatabase database = new MoviesDataBase(this.mContext).getWritableDatabase();
        assertEquals(true, database.isOpen());
        database.close();
    }

   /* public void testInsertReadDb (){
        String TestTitle = "XMen" ;
        String TestMovieID = "123456";
        String TestOverview = "it is science fiction movie" ;
        //String TestImage = "image";
        String Testpopularity = "100";
        String Testvotravg = "5";
        String Testvotec = "1000";
        String Testdate = "1024";

        MoviesDataBase dataBase = new MoviesDataBase(mContext) ;
        SQLiteDatabase db = dataBase.getWritableDatabase() ;

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, TestTitle);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, TestMovieID);
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, TestOverview);
        //contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, TestImage);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, Testpopularity);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, Testvotravg);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, Testvotec);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, Testdate);

        long movie = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,contentValues);

        assertTrue(movie != -1);
        Log.d(LogTag, "new row id : " + movie);

        String[] columns = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_POPULARITY
        };

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()){

            int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            String id = cursor.getString(idIndex);

            int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            String title = cursor.getString(titleIndex);

            int overIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            String over = cursor.getString(overIndex);

            assertEquals(TestMovieID, id);
            assertEquals(TestTitle, title);
            assertEquals(TestOverview, over);

        }else fail("no values returned ");



    }*/
}
