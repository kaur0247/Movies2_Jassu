package com.movies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.movies.MoviesActivity;
import com.movies.R;
import com.movies.Constants;
import com.movies.model.MovieModel;
import com.movies.common.SharedPreferenceUtility;
import com.squareup.picasso.Picasso;


public class MovieDetailsFragment extends Fragment {
    private TextView mTitle,mGenre,mStarCast,mDuration,mDescription,mRating;
    private ImageView imvPicture;
    private MovieModel mMovie;
    private ImageButton btnDelete,btnEdit;


    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie=(MovieModel) getArguments().getSerializable(Constants.sFragmentBundleData);

            }
    }
    public void replaceFragment(Fragment someFragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_movie_details, container, false);
        imvPicture=view.findViewById(R.id.imvPicture);
        mTitle=view.findViewById(R.id.movie_title);
        mGenre=view.findViewById(R.id.movie_genre);
        mStarCast=view.findViewById(R.id.movie_starcast);
        mDuration=view.findViewById(R.id.movie_length);
        mDescription=view.findViewById(R.id.movie_description);
        mRating=view.findViewById(R.id.movie_rating);
        btnDelete=view.findViewById(R.id.btnDelete);
        btnEdit=view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewMovie addNewMovieFrag = new AddNewMovie();
                Bundle b = new Bundle();
                b.putBoolean("check",true);
                b.putSerializable(Constants.sFragmentBundleData, mMovie);
                addNewMovieFrag.setArguments(b);

               replaceFragment(addNewMovieFrag);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MoviesActivity.alMovies.remove(mMovie);
                SharedPreferenceUtility.initEditor(getActivity());
                SharedPreferenceUtility.storeMovies(getActivity(),MoviesActivity.alMovies);
                if(MoviesActivity.alMovies.size()==0){
                    Bundle b= new Bundle();
                    b.putBoolean("size",true);
                    getActivity().startActivity(new Intent(getActivity(), MoviesActivity.class).putExtras(b));
                }else {
                    getActivity().startActivity(new Intent(getActivity(), MoviesActivity.class));
                }
                getActivity().finish();
            }
        });
        Picasso.get().load(mMovie.getImageUrl()).error(R.drawable.help_icon).placeholder(R.drawable.help_icon).noFade()
                .into(imvPicture);
        mTitle.setText(mMovie.getTitle());
        mGenre.setText(mMovie.getGenre());
        String actorsArray[]=mMovie.getActors().split(",");
        String actors="";
        for(int i=0;i<actorsArray.length;i++)
        {
            actors=actors+actorsArray[i];
            if(i!=actors.length()-2)
            {
                actors=actors+"\n";
            }
        }
        mStarCast.setText(actors);
        String duration=mMovie.getLength()+Constants.sEmptyString;
        mDuration.setText(duration);
        mDescription.setText(mMovie.getDescription());
        String rating=mMovie.getRating()+Constants.sEmptyString;
        mRating.setText(rating);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
