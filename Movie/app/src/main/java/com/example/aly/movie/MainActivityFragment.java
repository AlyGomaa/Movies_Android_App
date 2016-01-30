package com.example.aly.movie;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.aly.movie.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.widget.Toast.*;

/**
 * *****************************A.G.A***********************************
 * the main fragment for this app
 * it has grid view adapter to preview movies or tv series
 * and text box with button to search for movies or tv series with name.
 * *****************************A.G.A***********************************
 */
public class MainActivityFragment extends Fragment {

    // for fetching movies with page number
    private int page_number = 1 ;
    // for favourites
    private static int favourite = 0 ;
    // to set page number in different types of fetching movies
    private int s = 1 ;
    private int s2 = 0 ;
    // set the type of order for fetching movies
    private String check = "0" ;
    // information from json to get page number limit
    public static int totalPages = 1;
    public static int totalResults = 0 ;
    // the main adapter
    private ImageListAdapter adapter ;

    // movies informations
    // list of links for one page images
    private ArrayList<String> img = new ArrayList<String>() {
    };
    // list of images in bytes for favourites images
    private ArrayList<byte[]> favImg = new ArrayList<byte[]>() {
    };
    // list for movies names for one page fetching movies and for favourites
    private ArrayList<String> names = new ArrayList<String>() {
    };
    // list for movies overviews for one page fetching movies and for favourites
    private ArrayList<String> overviews = new ArrayList<String>() {
    };
    // list for movies popularity for one page fetching movies and for favourites
    private ArrayList<String> popularity = new ArrayList<String>() {
    };
    // list for movies vote average for one page fetching movies and for favourites
    private ArrayList<String> vote_average = new ArrayList<String>() {
    };
    // list for movies vote count for one page fetching movies and for favourites
    private ArrayList<String> vote_count = new ArrayList<String>() {
    };
    // list for movies release date for one page fetching movies and for favourites
    private ArrayList<String> release_date = new ArrayList<String>() {
    };
    // list for movies id for one page fetching movies and for favourites
    private ArrayList<String> movie_id = new ArrayList<String>() {
    };

    // MovieDb API key
    public static final String MOVIEDP_API_KEY = "51aad0d0be858f898efc98ee55b6579f";


    public MainActivityFragment() {
    }

    /*
    * interface for start detail activity when item selected
    * */
    public interface callback{
        // for fetching movies
        public void onItemSelected(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8);

        // for favourites
        public void onFavouritesItemSelected(byte[] s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.movies_update, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        /*
        *  refresh is used to return to fetching movies
        *  or to start fetching movies if app is opened when internet is not exist
        *  */
        if (id == R.id.action_refresh) {
            favourite = 0 ;
            MainActivity.Search = 0 ;
            page_number = 1 ;
            updateMovies();
            return true ;
        }
        /*
        * to fetch favourites movies*/
        else if (id == R.id.action_favourites){
            favourite = 1 ;
            MainActivity.Search = 0 ;
            MainActivity.fav.clear();
            MainActivity.fav.add(1);
            startFavourites();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        // set main adapter
        adapter = new ImageListAdapter(getActivity(), img, names, MainActivity.fav, favImg);

        // to save the last state when rotate the device
        if(MainActivity.fav.get(0) == 1)
            startFavourites();
        else if (MainActivity.Search == 1)
                 updateMovies();

        // Get a reference to the GridView, and attach this adapter to it.
        final GridView gridView = (GridView) rootview.findViewById(R.id.movie_grid);
        gridView.setAdapter(adapter);

        // when click on a movie
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView adapterView, View view, int position, long l) {

                                                // select the detail activity type
                                                if (MainActivity.fav.get(0) == 0)
                                                    ((callback) getActivity()).onItemSelected(img.get(position), names.get(position), overviews.get(position), popularity.get(position), vote_average.get(position), vote_count.get(position), release_date.get(position), movie_id.get(position), "1");
                                                else
                                                    ((callback) getActivity()).onFavouritesItemSelected(favImg.get(position), names.get(position), overviews.get(position), popularity.get(position), vote_average.get(position), vote_count.get(position), release_date.get(position), movie_id.get(position), "2");

                                            }

                                        }
        );

        // to make forward and backward buttons does not work in favourites mode
        if (MainActivity.fav.get(0) == 0) {
            // to change page number to fetch new page of movie
            ImageButton forward = (ImageButton) rootview.findViewById(R.id.forward_button);
            // to return to the previous page
            ImageButton backward = (ImageButton) rootview.findViewById(R.id.backward_button);

            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (page_number < totalPages) {
                        page_number = page_number + 1;
                        updateMovies();
                    }
                }
            });

            backward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (page_number > 1) {

                        page_number = page_number - 1;
                        updateMovies();
                    }
                }
            });
        }

        /*
        * for searching movies with movie name or tv series when click search button
        * */
        Button searchButton = (Button) rootview.findViewById(R.id.search_button);
        final EditText searchTextBox = (EditText) rootview.findViewById(R.id.search_text);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get movie name
                Editable searchMovie = searchTextBox.getText();
                String Movie2Search = searchMovie.toString();
                MainActivity.movieName = Movie2Search;

                // to delete the spaces in the end of movie name
                for (int i = Movie2Search.length() - 1 ; i > -1 ; i--){
                    String s = Movie2Search.substring(i, i+1) ;
                    if (s.equals(" "))
                        MainActivity.movieName = Movie2Search.substring(0,i);
                    else {
                        break;
                    }
                }

                // to make sure if user enter movie name
                if (MainActivity.movieName.equals("Enter Movie Name") || MainActivity.movieName.length() == 0) {
                    makeText(getContext(), "Enter Movie Name", LENGTH_LONG);
                    MainActivity.Search = 0 ;
                    Toast.makeText(getContext(), "Enter Movie Name", Toast.LENGTH_SHORT).show();
                }
                else{
                    // set search data mode
                    MainActivity.Search = 1 ;
                    favourite = 0 ;
                    MainActivity.fav.clear();
                    MainActivity.fav.add(0);
                    Toast.makeText(getContext(), "Loading Movies", Toast.LENGTH_SHORT).show();
                    updateMovies();
                }

            }
        });



        return rootview ;


    }

    /*
    * for update fetching movies */
    private void updateMovies(){
        Toast.makeText(getContext(), "Loading Movies", Toast.LENGTH_LONG).show();
        // to get user settings for app
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // to choose fetching movies or tv series
        String sortType = sharedPreferences.getString("sortType", "1");

        // in the case of fetching movies or tv series
        if (favourite == 0 && MainActivity.Search == 0) {
            MainActivity.fav.clear();
            MainActivity.fav.add(0);

            // to get order of movies type
            String sort_type = sharedPreferences.getString(getString(R.string.pref_Mov_key), "1");
            Integer sort_t = Integer.parseInt(sort_type);

            // to get release date for fetching movies
            String date = sharedPreferences.getString("releaseDate", "2015");

            // to make sure that user enter integer number
            int date1;
            try {
                date1 = Integer.parseInt(date);
            } catch (Exception e) {
                date = "2015";
                makeText(getActivity(), "Wrong Release Date", LENGTH_SHORT);
            }

            // to make sure that the date is 4 digits
            if (date.length() != 4)
                date = "2015";

            // get minimum vote count for movies
            String count = sharedPreferences.getString("voteCount", "100");

            //to make sure that user enter integer number for vote count
            try {
                date1 = Integer.parseInt(count);
            } catch (Exception e) {
                count = "100";
                makeText(getActivity(), "Wrong Vote Count Number", LENGTH_SHORT);
            }

            // to get state of using voteCountCheck from user
            boolean voteCountCheck = sharedPreferences.getBoolean("voteCountCheck", false);
            // to get state of using releaseDataCheck from user
            boolean releaseDateCheck = sharedPreferences.getBoolean("releaseDateCheck", false);

           // movies order type
            String sort;

             /*
            * to set sort of order type
            * and set number of page if change from type to another
            * */
            if (sort_t == 1) {
                sort = "popularity.desc";
                if (s == 2 || s == 3 || s == 4) {
                    page_number = 1;
                    s = 1;
                }
            } else if (sort_t == 2) {
                sort = "vote_average.desc";
                if (s == 1 || s == 3 || s == 4) {
                    page_number = 1;
                    s = 2;
                }
            } else if (sort_t == 3) {
                sort = "vote_count.desc";
                if (s == 2 || s == 1 || s == 4) {
                    page_number = 1;
                    s = 3;
                }
            } else {
                sort = "release_date.desc";
                if (s == 2 || s == 1 || s == 3) {
                    page_number = 1;
                    s = 4;
                }
            }

            /*
            * to set the used type of check elements (release date, vote count)
            * set page number when change from type to another
            * */
            if (voteCountCheck && releaseDateCheck) {
                check = "1";
                if (s2 == 0 || s2 == 2 || s2 == 3) {
                    page_number = 1;
                    s2 = 1;
                }
            } else if (voteCountCheck && (!releaseDateCheck)) {
                check = "2";
                if (s2 == 0 || s2 == 1 || s2 == 3) {
                    page_number = 1;
                    s2 = 2;
                }
            } else if ((!voteCountCheck) && releaseDateCheck) {
                check = "3";
                if (s2 == 0 || s2 == 1 || s2 == 2) {
                    page_number = 1;
                    s2 = 3;
                }
            } else {
                check = "0";
                if (s2 == 2 || s2 == 1 || s2 == 3) {
                    page_number = 1;
                    s2 = 0;
                }
            }

            // to get the state of using of adult filter
            boolean adultFilter = sharedPreferences.getBoolean("adultFilter", true);
            String filter = "1";
            if (!adultFilter)
                filter = "0";

            // start fetching for data
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
            fetchMoviesTask.execute(sort, String.valueOf(page_number), date, count, check, " ", filter, sortType);
        }
        /*
        * in the case of search for movie or tv series with name*/
        else if (MainActivity.Search == 1)
        {
            // state for search movies
            check = "4";

            // to get the state of using of adult filter
            boolean adultFilter = sharedPreferences.getBoolean("adultFilter", true);
            String filter = "1";
            if (!adultFilter)
                filter = "0" ;

            // start fetching for data
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
            fetchMoviesTask.execute(" ", String.valueOf(page_number), " ", " ", check, MainActivity.movieName, filter, sortType);
        }
    }


    /*
    * function for set favourites movies details from database*/
    private void startFavourites(){

        // clear adapter and details variables
        adapter.clear();
        overviews.clear();
        popularity.clear();
        vote_average.clear();
        vote_count.clear();
        release_date.clear();
        movie_id.clear();
        // set favourites mode
        favourite = 1 ;

        // try to open the database and get details
        try {
            MainActivity.movieDB = SQLiteDatabase.openDatabase("/data/data/com.example.aly.movie/databases/movies.db", null,
                    SQLiteDatabase.OPEN_READWRITE);

            // set a cursor
            Cursor cursor = MainActivity.movieDB.query(
                    MovieContract.MovieEntry.TABLE_NAME,
                    DetailFragmentAdapter.columns,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            // get movie details if exist
            if (cursor != null) {

                if (cursor.moveToFirst()) {
                    do {
                        int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                        movie_id.add(cursor.getString(idIndex));

                        int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                        names.add(cursor.getString(titleIndex));

                        int overIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
                        overviews.add(cursor.getString(overIndex));

                        int imgIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE);
                        favImg.add(cursor.getBlob(imgIndex));

                        int popIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY);
                        popularity.add(cursor.getString(popIndex));

                        int voteAvgIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                        vote_average.add(cursor.getString(voteAvgIndex));

                        int voteCountIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT);
                        vote_count.add(cursor.getString(voteCountIndex));

                        int dateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                        release_date.add(cursor.getString(dateIndex));

                    }while (cursor.moveToNext());

                    // update adapter
                    adapter.notifyDataSetChanged();

                    // close cursor and database
                    cursor.close();
                    MainActivity.movieDB.close();

                } else {
                    // if cursor at end
                    makeText(getContext(), "No favourites Exist", LENGTH_SHORT).show();
                }
            }
            else  {
                // if cursor = null
                makeText(getContext(), "No favourites Exist", LENGTH_SHORT).show();
            }

        } catch (SQLiteException e) {
            // database doesn't exist yet.
            makeText(getContext(), "No favourites Exist", LENGTH_SHORT).show();
        }

    }

    /*
    *function to fetch movies in the start of the app
     **/
    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    /*
    * fetching movies and tv series from MOVIEDB_API with some user definitions
    * */
    public class FetchMoviesTask extends AsyncTask<String,Void,String[][]> {

        // for log file
        private final String Log_Tag = FetchMoviesTask.class.getSimpleName();
        // type of fetching movies or tv_series
        private String type ;

        @Override
        protected String[][] doInBackground(String ... sort_type) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;


            try {
                // Construct the URL for the MOVIEDB query
                URL url = new URL("http://api.themoviedb.org/3/movie?sort_by=popularity.desc&api_key=51aad0d0be858f898efc98ee55b6579f");

                // initialize queries
                String MOVIEDB_BASE_URL =  "http://api.themoviedb.org/3/discover/movie?";
                String MOVIEDB_SEARCH_BASE_URL ;
                final String api_PARAM = "api_key";
                final String sort_PARAM = "sort_by";
                String year_PARAM = "primary_release_year";
                final String page_PARAM = "page";
                final String count_PARAM = "vote_count.gte";
                final String search_PARAM = "query";
                final String filter_PARAM = "include_adult";

                // get fetching queries values which defined by user
                String sort = sort_type[0];
                String page = sort_type[1];
                String date = sort_type[2];
                String count = sort_type[3];
                String check = sort_type[4];
                String search = sort_type[5];
                String filter = sort_type[6];
                type = sort_type[7];

                // set base url for fetching from MovieDb
                if (type.equals("1")) {
                    // base url for movie fetching
                    MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                    year_PARAM = "primary_release_year";
                    // base url for search for movies
                    MOVIEDB_SEARCH_BASE_URL = "http://api.themoviedb.org/3/search/movie?";
                }
                else {
                    // base url for tv series fetching
                    MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/tv?";
                    year_PARAM = "first_air_date_year";
                    // base url for searching for tv series
                    MOVIEDB_SEARCH_BASE_URL = "http://api.themoviedb.org/3/search/tv?";
                }
                /*
                * build uri according to user choices*/
                Uri builtUri ;
                Uri.Builder builder ;
                    // for fetching movies or tv series with order type and page only
                    if (check.equals("0")) {
                        builder = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                                .appendQueryParameter(sort_PARAM, sort)
                                .appendQueryParameter(page_PARAM, page)
                                .appendQueryParameter(api_PARAM, MOVIEDP_API_KEY);

                        // to enable adult filter or disable it
                        if (filter.equals("1"))
                            builtUri = builder.appendQueryParameter(filter_PARAM, "false").build();
                        else {
                            builtUri = builder.appendQueryParameter(filter_PARAM, "true").build();
                        }
                    }
                    // for fetching movies or tv series with order type, page number, count number and release date
                    else if (check.equals("1")) {
                        builder = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                                .appendQueryParameter(sort_PARAM, sort)
                                .appendQueryParameter(page_PARAM, page)
                                .appendQueryParameter(count_PARAM, count)
                                .appendQueryParameter(year_PARAM, date)
                                .appendQueryParameter(api_PARAM, MOVIEDP_API_KEY);

                        if (filter.equals("1"))
                            builtUri = builder.appendQueryParameter(filter_PARAM, "false").build();
                        else {
                            builtUri = builder.appendQueryParameter(filter_PARAM, "true").build();
                        }
                    }
                    // for fetching movies or tv series with order type, page number and count number
                    else if (check.equals("2")) {
                        builder = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                                .appendQueryParameter(sort_PARAM, sort)
                                .appendQueryParameter(page_PARAM, page)
                                .appendQueryParameter(count_PARAM, count)
                                .appendQueryParameter(api_PARAM, MOVIEDP_API_KEY);

                        if (filter.equals("1"))
                            builtUri = builder.appendQueryParameter(filter_PARAM, "false").build();
                        else {
                            builtUri = builder.appendQueryParameter(filter_PARAM, "true").build();
                        }
                    }
                    // for fetching movies or tv series with order type, page number and release date
                    else if (check.equals("3")) {
                        builder = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                                .appendQueryParameter(sort_PARAM, sort)
                                .appendQueryParameter(page_PARAM, page)
                                .appendQueryParameter(year_PARAM, date)
                                .appendQueryParameter(api_PARAM, MOVIEDP_API_KEY);

                        if (filter.equals("1"))
                            builtUri = builder.appendQueryParameter(filter_PARAM, "false").build();
                        else {
                            builtUri = builder.appendQueryParameter(filter_PARAM, "true").build();
                        }
                    }
                    // for searching movies or tv series with name and page number only
                    else {
                        builder = Uri.parse(MOVIEDB_SEARCH_BASE_URL).buildUpon()
                                .appendQueryParameter(search_PARAM, search)
                                .appendQueryParameter(page_PARAM, page)
                                .appendQueryParameter(api_PARAM, MOVIEDP_API_KEY);

                        if (filter.equals("1"))
                            builtUri = builder.appendQueryParameter(filter_PARAM, "false").build();
                        else {
                            builtUri = builder.appendQueryParameter(filter_PARAM, "true").build();
                        }
                    }
                // get url
                url = new URL(builtUri.toString());
                Log.v(Log_Tag, "Built URI " + builtUri.toString());

                // Create the request to MOVIEDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                // change moviejson to string
                movieJsonStr = buffer.toString();
                Log.v(Log_Tag,"Movie Json String:" + movieJsonStr) ;
            } catch (IOException e) {
                Log.e(Log_Tag, "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                // get details from json string
                return getMovieDataFromJson(movieJsonStr, type);
            }catch (JSONException e){
                Log.e(Log_Tag, e.getMessage(), e) ;
                e.printStackTrace();
            }

            return null;
        }

        // to extract details from json
        private String[][] getMovieDataFromJson(String movieJsonStr, String type)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_PATH = "poster_path";
            final String OWM_ID = "id";
            final String OWM_OVERVIEW = "overview";
            final String OWM_ORIGINAL_TITLE = "original_title";
            final String OWM_popularity = "popularity";
            final String OWM_vote_average = "vote_average" ;
            final String OWM_vote_count = "vote_count" ;
            final String OWM_release_date ="release_date";
            final String OWM_pages_number = "total_pages" ;
            final String OWM_total_results ="total_results";
            // names for tv series to be extracted
            final String OWM_ORIGINAL_NAME = "original_name";
            final String OWM_air_date ="first_air_date";

            // get json object for movieJSON
            JSONObject moviesJson = new JSONObject(movieJsonStr);
            // get total pages number
            totalPages = moviesJson.getInt(OWM_pages_number);
            // get total results number of movies
            totalResults = moviesJson.getInt(OWM_total_results);

            // get movies list for a page
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_LIST);
            // array to store movies details
            String[][] resultStrs = new String[8][moviesArray.length()];

            // loop on movies list to get details for each movie
            for(int i = 0; i < moviesArray.length(); i++) {
                // variables to get details
                String poster_path;
                String id;
                String original_title;
                String overview ;
                String popularity ;
                String vote_average ;
                String vote_count ;
                String release_date ;

                // Get the JSON object representing the movie
                JSONObject movie = moviesArray.getJSONObject(i);
                // get poster link
                poster_path = movie.getString(OWM_PATH);
                // get movie id
                int idi = movie.getInt(OWM_ID);
                id = Integer.toString(idi);
                // get movie or tv series name and release date
                if (type.equals("1")) {
                    original_title = movie.getString(OWM_ORIGINAL_TITLE);
                    release_date = movie.getString(OWM_release_date);
                }
                else {
                    original_title = movie.getString(OWM_ORIGINAL_NAME);
                    release_date = movie.getString(OWM_air_date);
                }
                // get overview
                overview = movie.getString(OWM_OVERVIEW);
                // get popularity
                popularity = movie.getString(OWM_popularity);
                // get vote average
                vote_average = movie.getString(OWM_vote_average);
                // get vote count
                vote_count = movie.getString(OWM_vote_count);

                // set details in array
                resultStrs[0][i] = poster_path   ;
                resultStrs[1][i] = original_title ;
                resultStrs[2][i] = overview ;
                resultStrs[3][i] = popularity ;
                resultStrs[4][i] = vote_average ;
                resultStrs[5][i] = vote_count ;
                resultStrs[6][i] = release_date ;
                resultStrs[7][i] = id ;
            }

            for (String s : resultStrs[0]) {
                Log.v(Log_Tag, "Movies : " + s);
            }
            return resultStrs;

        }

        protected void onPostExecute(String[][] result){
            // update adapter data
            if (result != null)
            {
                adapter.clear();
                overviews.clear();
                popularity.clear();
                vote_average.clear();
                vote_count.clear();
                release_date.clear();
                movie_id.clear();


                for (int i = 0 ; i < result[1].length ; i++)
                {
                    img.add(result[0][i]);
                    names.add(result[1][i]);
                    overviews.add(result[2][i]);
                    popularity.add(result[3][i]);
                    vote_average.add(result[4][i]);
                    vote_count.add(result[5][i]);
                    release_date.add(result[6][i]);
                    movie_id.add(result[7][i]);
                }

                adapter.notifyDataSetChanged();


            }
        }
    }

}
