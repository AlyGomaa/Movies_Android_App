package com.example.aly.movie;

/*
****************************************A.G.A*************************************
*Adapter for reviews gridView and trailers gridView
****************************************A.G.A*************************************
*/
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aly on 12/01/2016.
 */
public class MovieReviewsTrailersAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private final String Log_Tag = ImageListAdapter.class.getSimpleName();
	
    private List<String> Urls;
    private List<String> authors ;
    private List<String> keys;
    private List<String> names ;
    private int type ;

    public MovieReviewsTrailersAdapter(Context context, List<String> urls, List<String> authors, List<String> keys, List<String> names, int type) {

        this.context = context;
        this.authors = authors;
        this.Urls = urls ;
        this.names = names;
        this.keys = keys ;
        this.type = type ;

        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            if (type == 1)
                convertView = inflater.inflate(R.layout.movie_reviews_items, parent, false);
            else
                convertView = inflater.inflate(R.layout.movie_trailers_items, parent, false);
        }

        if (type == 1) {
            if ((authors.size() != 0) && (position < authors.size())) {
                TextView review = (TextView) convertView.findViewById(R.id.reviews_text);
                review.setText(authors.get(position));

                ImageView revImg = (ImageView) convertView.findViewById(R.id.rev_img);
                revImg.setImageResource(R.drawable.reviews);


                review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.get(position)));

                        context.startActivity(launchBrowser);
                    }
                });

                revImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.get(position)));

                        context.startActivity(launchBrowser);
                    }
                });
            }
        }else {

            if ((names.size() != 0) && (position < names.size())) {
                TextView trailer = (TextView) convertView.findViewById(R.id.trailers_text);
                trailer.setText(names.get(position));

                ImageView trailerImg = (ImageView) convertView.findViewById(R.id.trailers_img);
                trailerImg.setImageResource(R.drawable.trailer1);


                trailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String url = "http://www.youtube.com/watch?v=" + keys.get(position);


                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                        context.startActivity(launchBrowser);
                    }
                });

                trailerImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String url = "http://www.youtube.com/watch?v=" + keys.get(position);


                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                        context.startActivity(launchBrowser);
                    }
                });

            }
            else if (names.size() == 0){
                TextView trailer = (TextView) convertView.findViewById(R.id.trailers_text);
                trailer.setText("No Trailers Exist");
            }
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return Urls.size();
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
        Urls.clear();
        authors.clear();
        notifyDataSetChanged();
    }
}