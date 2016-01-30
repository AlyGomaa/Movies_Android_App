package com.example.aly.movie;

/**
 * *******************************************A.G.A*************************************************
 * adapter to set images and names of movies and tv series in fetching, searching and favourites modes
 * *******************************************A.G.A*************************************************
 */
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private final String Log_Tag = ImageListAdapter.class.getSimpleName();
    private List<String> imageUrls;
    private List<String> moviesName ;
    private List<Integer> favourite ;
    private ArrayList<byte[]> favImages ;

    public ImageListAdapter(Context context, List<String> imageUrls, List<String> moviesNames, List<Integer> favourite, ArrayList<byte[]> favImages) {

        this.context = context;
        this.imageUrls = imageUrls;
        this.moviesName = moviesNames ;
        this.favourite = favourite ;
        this.favImages = favImages ;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {

            convertView = inflater.inflate(R.layout.movie_item, parent, false);
        }

        if (favourite.get(0) == 0) {
            if (imageUrls.get(position).equals("null")) {
                ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
                imageView.setImageResource(R.drawable.small_poster);
            } else
                Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + imageUrls.get(position)).into((ImageView) convertView.findViewById(R.id.movie_image));

            TextView name = (TextView) convertView.findViewById(R.id.movie_text);
            name.setText(moviesName.get(position));


        }else if (favourite.get(0) == 1)
        {
            if (favImages.size() > 0) {

                ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(favImages.get(position),
                        0, favImages.get(position).length));

                TextView name = (TextView) convertView.findViewById(R.id.movie_text);
                name.setText(moviesName.get(position));
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
       if (moviesName.size() != 0)
             return moviesName.size();
        else
             return favImages.size();
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
        imageUrls.clear();
        moviesName.clear();
        favImages.clear();
        notifyDataSetChanged();
    }
}

