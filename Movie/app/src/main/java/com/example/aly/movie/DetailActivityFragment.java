package com.example.aly.movie;

/****************************A.G.A**************************
*DetailActivity for present details for movies or tv series
*****************************A.G.A**************************
*/
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

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
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    // details variables 
	private static String overviwe ;
    private String img ;
    private String title ;
    private String popularity ;
    private String vote_average ;
    private String vote_count ;
    private String release_date ;
    private String movie_id ;
    private String favourite ;
    private static String sort ;
    private byte[] favImg ;

	// reviews and trails variables
    private ArrayList<String> urls = new ArrayList<String>() {
    };
    private ArrayList<String> authors = new ArrayList<String>() {
    };
    public static ArrayList<String> keys = new ArrayList<String>() {
    };
    private ArrayList<String> names = new ArrayList<String>() {
    };
	// adapter for reviews  
    private MovieReviewsTrailersAdapter adapter1 ;
	// adapter for trailers
    private MovieReviewsTrailersAdapter adapter2 ;

	// main adapter for details activity 
    private DetailFragmentAdapter adapter ;

    private final String Log_Tag = DetailActivityFragment.class.getSimpleName();

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(Log_Tag, "done");
        // get arguments from detailActivity 
        Bundle extras = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if (extras != null)
       {
		   // get details values
            overviwe = extras.getString("overview");
            title    = extras.getString("title");
            popularity = extras.getString("popularity");
            vote_average = extras.getString("vote_average");
            vote_count = extras.getString("vote_count");
            release_date = extras.getString("release_date");
            movie_id = extras.getString("movie_id");
            favourite = extras.getString("fav");

		   // check for mode of details favourites or fetching movies and get image of movie
           if (favourite.equals("1"))
               img = extras.getString("images");
           else
               favImg = extras.getByteArray("images");

           if (favourite.equals("1")) {
                // set adapter for favourites movies
                adapter = new DetailFragmentAdapter(getActivity(), img, null,overviwe, title, popularity, vote_average, vote_count, release_date, movie_id, 1);

           }else {
               // set adapter for fetching movies or tv series or search with name
               adapter = new DetailFragmentAdapter(getActivity(), null,favImg, overviwe, title, popularity, vote_average, vote_count, release_date, movie_id, 2);

           }

		   // main gridView for details activity and set adapter
           final GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid_detail);
           gridView.setAdapter(adapter);

		   // set reviews text 
           TextView reviews = (TextView) rootView.findViewById(R.id.rev_col);
           reviews.setText("Reviews :");
           // set trailers text
           TextView trails = (TextView) rootView.findViewById(R.id.trail_col);
           trails.setText("Trailers :");

		   // set reviews adapter and gridView
           adapter1 = new MovieReviewsTrailersAdapter(getActivity(), urls, authors, keys, names,1);
           GridView gridview = (GridView) rootView.findViewById(R.id.movie_rev_detail);
           gridview.setAdapter(adapter1);
           
		   // set trailers adapter and gridView
           adapter2 = new MovieReviewsTrailersAdapter(getActivity(), urls, authors, keys, names,2);
           GridView gridview2 = (GridView) rootView.findViewById(R.id.movie_trail_detail);
           gridview2.setAdapter(adapter2);

		   // fetch for trailers details
           FetchMoviesTrailers fetchMoviesTrailers = new FetchMoviesTrailers();
           fetchMoviesTrailers.execute(movie_id, "2");

		   // fetch for reviews details
           FetchMoviesReviews fetchMoviesReviews = new FetchMoviesReviews();
           fetchMoviesReviews.execute(movie_id, "1");
       }


        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public class FetchMoviesReviews extends AsyncTask<String,Void,String[][]> {

        private final String Log_Tag = FetchMoviesReviews.class.getSimpleName();

        @Override
        protected String[][] doInBackground(String ... id) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            String id1 = Arrays.toString(id).substring(1, Arrays.toString(id).length() - 4);
            sort = Arrays.toString(id).substring(Arrays.toString(id).length() - 2, Arrays.toString(id).length() - 1);

            try {

                URL url = new URL("http://api.themoviedb.org/3/movie/" + id1 + "/reviews?");

                final String MOVIE_BASE_URL ;

                if (sort.equals("1")) {
                    MOVIE_BASE_URL =
                            "http://api.themoviedb.org/3/movie/" + id1 + "/reviews?";
                }
                else{
                    MOVIE_BASE_URL =
                            "http://api.themoviedb.org/3/movie/" + id1 + "/videos?";
                }

                final String api_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(api_PARAM, MainActivityFragment.MOVIEDP_API_KEY)
                        .build();

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
                movieJsonStr = buffer.toString();
                Log.v(Log_Tag,"Movie Json String:" + movieJsonStr) ;
            } catch (IOException e) {
                Log.e(Log_Tag, "Error ", e);
                // If the code didn't successfully get the reviews or trailers data, there's no point in attemping
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
                return getMovieDataFromJson(movieJsonStr,sort);
            }catch (JSONException e){
                Log.e(Log_Tag, e.getMessage(), e) ;
                e.printStackTrace();
            }

            return null;
        }


        private String[][] getMovieDataFromJson(String forecastJsonStr, String sort)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_author ;
            final String OWM_url;

            if (sort.equals("1")) {

                 OWM_author = "author";
                 OWM_url = "url";
            }
            else {

                 OWM_author = "name";
                 OWM_url = "key";
            }

            JSONObject moviesJson = new JSONObject(forecastJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_LIST);


            String[][] resultStrs = new String[2][moviesArray.length()];

            for(int i = 0; i < moviesArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String author;
                String url;

                // Get the JSON object representing the day
                JSONObject movie = moviesArray.getJSONObject(i);


                author = movie.getString(OWM_author);
                url = movie.getString(OWM_url);

                resultStrs[0][i] = author   ;
                resultStrs[1][i] = url ;
            }

            for (String s : resultStrs[0]) {
                Log.v(Log_Tag, "Movies : " + s);
            }
            return resultStrs;

        }

        protected void onPostExecute(String[][] result){
            if (result != null) {
                if (sort.equals("1")) {
                    authors.clear();
                    urls.clear();

                    for (int i = 0; i < result[1].length; i++) {
                        authors.add(result[0][i]);
                        urls.add(result[1][i]);

                    }

                    adapter1.notifyDataSetChanged();
                }
                else
                {
                    names.clear();
                    keys.clear();

                    for (int i = 0; i < result[1].length; i++) {
                        names.add(result[0][i]);
                        keys.add(result[1][i]);

                    }

                    adapter2.notifyDataSetChanged();
                }
            }
        }
    }

    public class FetchMoviesTrailers extends AsyncTask<String,Void,String[][]> {

        private final String Log_Tag = FetchMoviesReviews.class.getSimpleName();

        @Override
        protected String[][] doInBackground(String ... id) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            String id1 = Arrays.toString(id).substring(1, Arrays.toString(id).length() - 4);
            sort = Arrays.toString(id).substring(Arrays.toString(id).length() - 2, Arrays.toString(id).length() - 1);

            try {

                URL url = new URL("http://api.themoviedb.org/3/movie/" + id1 + "/reviews?");

                final String MOVIE_BASE_URL ;

                if (sort.equals("1")) {
                    MOVIE_BASE_URL =
                            "http://api.themoviedb.org/3/movie/" + id1 + "/reviews?";
                }
                else{
                    MOVIE_BASE_URL =
                            "http://api.themoviedb.org/3/movie/" + id1 + "/videos?";
                }

                final String api_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(api_PARAM, MainActivityFragment.MOVIEDP_API_KEY)
                        .build();

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
                movieJsonStr = buffer.toString();
                Log.v(Log_Tag,"Movie Json String:" + movieJsonStr) ;
            } catch (IOException e) {
                Log.e(Log_Tag, "Error ", e);
                // If the code didn't successfully get the trailers data, there's no point in attemping
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
                return getMovieDataFromJson(movieJsonStr,sort);
            }catch (JSONException e){
                Log.e(Log_Tag, e.getMessage(), e) ;
                e.printStackTrace();
            }

            return null;
        }


        private String[][] getMovieDataFromJson(String forecastJsonStr, String sort)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_author ;
            final String OWM_url;

                OWM_author = "name";
                OWM_url = "key";


            JSONObject moviesJson = new JSONObject(forecastJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_LIST);


            String[][] resultStrs = new String[2][moviesArray.length()];

            for(int i = 0; i < moviesArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String author;
                String url;

                // Get the JSON object representing the day
                JSONObject movie = moviesArray.getJSONObject(i);


                author = movie.getString(OWM_author);
                url = movie.getString(OWM_url);

                resultStrs[0][i] = author   ;
                resultStrs[1][i] = url ;
            }

            for (String s : resultStrs[0]) {
                Log.v(Log_Tag, "Movies : " + s);
            }
            return resultStrs;

        }

        protected void onPostExecute(String[][] result){
            if (result != null) {

                    names.clear();
                    keys.clear();

                    for (int i = 0; i < result[1].length; i++) {
                        names.add(result[0][i]);
                        keys.add(result[1][i]);

                    }

                    adapter2.notifyDataSetChanged();
                }
            }

    }


}
