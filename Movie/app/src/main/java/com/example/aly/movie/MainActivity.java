package com.example.aly.movie;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aly.movie.data.MoviesDataBase;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.callback{

    public static android.support.v7.widget.ShareActionProvider mShareActionProvider ;

    // database for favourites movies
    public static SQLiteDatabase movieDB = null ;
    // for type of device tablet or not
    private boolean mTwoPane ;
    // to set adapter on the cse of favourites
    public static ArrayList<Integer> fav = new ArrayList<Integer>() {{
        add(0);  // initialize to start with fetching movies
    }
    };
    // use to set the case of search for movies
    public static int Search = 0 ;
    // name of movie to search
    public static String movieName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        /*
        * try to find database if exist and create a new one if not
        * in the case of the first open for this app a new one will be created
        * */
        try {
            movieDB = SQLiteDatabase.openDatabase("/data/data/com.example.aly.movie/databases/movies.db", null,
                    SQLiteDatabase.OPEN_READONLY);
            movieDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
            MoviesDataBase dataBase = new MoviesDataBase(getApplicationContext()) ;
            movieDB = dataBase.getWritableDatabase() ;
            movieDB.close();
        }

        /*
        * check for TUI
        * */
        if (findViewById(R.id.fragment_detail) != null) {

            mTwoPane = true ;
            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_detail, new DetailActivityFragment()).commit();
            }
        }else {
            mTwoPane = false ;
        }

    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            /*
            * start settings activity
            * */
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivityFragment ff = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    /*
    * to start detail activity for movies in two modes :
    * TUI mode
    * Phone mode
    * */
    @Override
    public void onItemSelected(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {

        if (mTwoPane)
        {
            Bundle args = new Bundle();

            args.putString("images", s);
            args.putString("title", s1);
            args.putString("overview", s2);
            args.putString("popularity", s3);
            args.putString("vote_average", s4);
            args.putString("vote_count", s5);
            args.putString("release_date", s6);
            args.putString("movie_id", s7);
            args.putString("fav", s8);

            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_detail, detailActivityFragment).commit();

        }else {

            Intent intent = new Intent(this, DetailActivity.class).putExtra("images", s);

            intent.putExtra("title", s1);
            intent.putExtra("overview", s2);
            intent.putExtra("popularity", s3);
            intent.putExtra("vote_average", s4);
            intent.putExtra("vote_count", s5);
            intent.putExtra("release_date", s6);
            intent.putExtra("movie_id", s7);
            intent.putExtra("fav", s8);

            startActivity(intent);
        }
    }

    /*
    * to start detail activity for favourites movies in two modes :
    * TUI mode
    * Phone mode
    * */
    @Override
    public void onFavouritesItemSelected(byte[] s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {

        if (mTwoPane)
        {
            Bundle args = new Bundle();

            args.putByteArray("images", s);
            args.putString("title", s1);
            args.putString("overview", s2);
            args.putString("popularity", s3);
            args.putString("vote_average", s4);
            args.putString("vote_count", s5);
            args.putString("release_date", s6);
            args.putString("movie_id", s7);
            args.putString("fav", s8);

            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_detail, detailActivityFragment).commit();

        }
        else {

            Intent intent = new Intent(this, DetailActivity.class).putExtra("images", s);

            intent.putExtra("title", s1);
            intent.putExtra("overview", s2);
            intent.putExtra("popularity", s3);
            intent.putExtra("vote_average", s4);
            intent.putExtra("vote_count", s5);
            intent.putExtra("release_date", s6);
            intent.putExtra("movie_id", s7);
            intent.putExtra("fav", s8);

            startActivity(intent);
        }
    }
}
