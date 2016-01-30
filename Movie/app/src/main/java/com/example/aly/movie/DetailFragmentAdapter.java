package com.example.aly.movie;

/****************************A.G.A**************************
*Adapter for main grid view in details activity 
*set details in the case of fetching movies or tv series
*and for favourites movies and tv series 
*and for search by name
*****************************A.G.A**************************
*/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aly.movie.data.MovieContract;
import com.example.aly.movie.data.MoviesDataBase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class DetailFragmentAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private final String Log_Tag = ImageListAdapter.class.getSimpleName();
    // details variables
	private static String overviwe ;
    private String img ;
    private String title ;
    private String popularity ;
    private String vote_average ;
    private String vote_count ;
    private String release_date ;
    private String id ;
	// favourites mode check
    public static int fav = 1 ;
    private static int sort ;
	// convert image into byte array and save it to database 
    private  byte[] movieImgByteArray ;
    private byte[] favImg ;

	// clumns of database 
    public static String[] columns = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_IMAGE,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_VOTE_COUNT
    };
    private Cursor cursor;


    public DetailFragmentAdapter(Context context,  String img, byte[] favImg,String overviwe, String title ,String popularity, String vote_average, String vote_count, String release_date, String id, int sort) {

        this.context = context;
        this.overviwe = overviwe ;
        this.img = img ;
        this.title = title ;
        this.popularity = popularity ;
        this.vote_average = vote_average ;
        this.vote_count = vote_count ;
        this.release_date = release_date ;
        this.id = id ;
        this.sort = sort ;
        this.favImg = favImg ;

        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	    // try to open database 
        try {
            MainActivity.movieDB = SQLiteDatabase.openDatabase("/data/data/com.example.aly.movie/databases/movies.db", null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // database doesn't exist yet.
            MoviesDataBase dataBase = new MoviesDataBase(context) ;
            MainActivity.movieDB = dataBase.getWritableDatabase();
        }
         // set cursor to databaase
         cursor = MainActivity.movieDB.query(
                MovieContract.MovieEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );


        if (null == convertView) {

            convertView = inflater.inflate(R.layout.detail_items, parent, false);
        }
        final ImageView movie_image = (ImageView) convertView.findViewById(R.id.detail_img);
        // to set image in image view in the case of favourites mode 
		if (sort == 2 && movie_image != null)
        {
           movie_image.setImageBitmap(BitmapFactory.decodeByteArray(favImg,
                    0, favImg.length));

        }
		//  to set image view in the case of fetching for movies or searching by name and poster image doesn't exist  
		else if (img.equals("null") && sort == 1) {

                movie_image.setImageResource(R.drawable.small_poster);
            }
			// set image if poster image exist 
            else {
                Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + img).into(movie_image);

            }

		// convert image view to bytes array	
        BitmapDrawable imgDrawable = (BitmapDrawable) movie_image.getDrawable();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imgDrawable.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        movieImgByteArray = stream.toByteArray();

        // set details titles texts
        ((TextView) convertView.findViewById(R.id.detail_text3)).setText("OverView :");
        ((TextView) convertView.findViewById(R.id.detail_text4)).setText("Popularity :");
        ((TextView) convertView.findViewById(R.id.detail_text5)).setText("Vote Average :");
        ((TextView) convertView.findViewById(R.id.detail_text6)).setText("Vote Count :");
        ((TextView) convertView.findViewById(R.id.detail_text7)).setText("Release Date :");

        // set details values
        if (overviwe.equals(""))
            ((TextView) convertView.findViewById(R.id.detail_overview)).setText(" Not Found");
        else
            ((TextView) convertView.findViewById(R.id.detail_overview)).setText(overviwe);

        if (title.equals(""))
            ((TextView) convertView.findViewById(R.id.detail_text)).setText("Not Found");
        else
            ((TextView) convertView.findViewById(R.id.detail_text)).setText(title);

        if (popularity.equals(""))
            ((TextView) convertView.findViewById(R.id.detail_popularity)).setText("Not Found");
        else
        ((TextView) convertView.findViewById(R.id.detail_popularity)).setText(popularity);

        if (vote_average.equals(""))
            ((TextView) convertView.findViewById(R.id.detail_vote_average)).setText("Not Found");
        else
            ((TextView) convertView.findViewById(R.id.detail_vote_average)).setText(vote_average);

        if (vote_count.equals(""))
            ((TextView) convertView.findViewById(R.id.detail_vote_count)).setText("Not Found");
        else
            ((TextView) convertView.findViewById(R.id.detail_vote_count)).setText(vote_count);
        if (release_date.equals(""))
            ((TextView) convertView.findViewById(R.id.detail_release_date)).setText("Not Found");
        else
            ((TextView) convertView.findViewById(R.id.detail_release_date)).setText(release_date);

		// set image button for favourite and unfavourite
        final ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.fav_button);

		// check if this movie is in the favourites database and set image for image button
        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                    String movie_id = cursor.getString(idIndex);

                    if (id.equals(movie_id)) {
                        fav = 2;
                        imageButton.setImageResource(R.drawable.unfav_h);
                        break;
                    }
                    else{
                        fav = 1 ;
                        imageButton.setImageResource(R.drawable.fav_h);
                    }

                }while (cursor.moveToNext());



            } else {
                fav = 1 ;
                imageButton.setImageResource(R.drawable.fav_h);
            }
        }
        else  {
            fav = 1 ;
            imageButton.setImageResource(R.drawable.fav_h);
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when user hit favourite button to add movie to favourites
                if (fav == 1)
                {   // add details to database
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overviwe);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, movieImgByteArray);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, vote_average);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, vote_count);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, release_date);
                    
                    long movie = MainActivity.movieDB.insert(MovieContract.MovieEntry.TABLE_NAME,null,contentValues);
                    // change image of imagebutton 
                    if (movie != -1)
                    {
                        fav = 2 ;
                        imageButton.setImageResource(R.drawable.unfav_h);
                    }

                }
                else {
					// delete movie details from database when user unfavourite the movie 
                    cursor = MainActivity.movieDB.query(
                            MovieContract.MovieEntry.TABLE_NAME,
                            columns,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    if (cursor.moveToFirst()) {
                        do {
                            int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                            String movie_id = cursor.getString(idIndex);

                            if (id.equals(movie_id)) {
                                String key = cursor.getString(0);
                                MainActivity.movieDB.delete(MovieContract.MovieEntry.TABLE_NAME, "_id" + "=" + key, null);
                                fav = 1;
                                imageButton.setImageResource(R.drawable.fav_h);
                                break;
                            }

                        } while (cursor.moveToNext());
                    }
                }
            }
        });
		
        return convertView;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public void clear()
    {
        notifyDataSetChanged();
    }




}



