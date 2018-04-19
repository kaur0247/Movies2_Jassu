package com.movies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.movies.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class MoviesAdapter extends ArrayAdapter<MovieModel> {
    private Context mContext;
    private ArrayList<MovieModel> alMovies;
    public MoviesAdapter(@NonNull Context pContext, ArrayList<MovieModel> pMoviesList) {
        super(pContext,-1, pMoviesList);
        mContext=pContext;
        alMovies=pMoviesList;

    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.item_layout, parent, false);
        MovieModel movie=alMovies.get(position);
        TextView title =  rowView.findViewById(R.id.movie_title);
        TextView genre = rowView.findViewById(R.id.movie_genre);
        TextView rating = rowView.findViewById(R.id.movie_rating);
        ImageView imgMoviePoster = rowView.findViewById(R.id.movie_poster);
        System.out.println("............"+movie.getImageUrl());
        if(movie.getImageUrl().contains("http")) {
            Picasso.get().load(movie.getImageUrl()).error(R.drawable.help_icon).placeholder(R.drawable.help_icon).noFade()
                    .into(imgMoviePoster);
        }else{
            imgMoviePoster.setImageURI(Uri.parse(movie.getImageUrl()));
        }
        title.setText(movie.getTitle());
        genre.setText(movie.getGenre());
        String ratingText=movie.getRating()+ Constants.sEmptyString;
        rating.setText(ratingText);
        return rowView;
    }


}
