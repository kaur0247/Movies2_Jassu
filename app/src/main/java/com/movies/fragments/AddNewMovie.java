package com.movies.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.movies.R;
import com.movies.MoviesActivity;
import com.movies.Constants;
import com.movies.model.MovieModel;
import com.movies.common.SharedPreferenceUtility;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class AddNewMovie extends Fragment implements View.OnClickListener {
    private EditText etTitle, etGenre, etStarcast, etDuration, etRating, etDescripton;
    private ImageView imvPicture;
    private Button btnSumbit, btnCancel,btnAddPoster;
    private  MovieModel mMovie;
    private  Uri u=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_new_movie, container, false);
        etTitle = (EditText) v.findViewById(R.id.etTitle);
        etGenre = (EditText) v.findViewById(R.id.etGenre);
        etStarcast = (EditText) v.findViewById(R.id.etStarcast);
        etDuration = (EditText) v.findViewById(R.id.etDuration);
        etRating = (EditText) v.findViewById(R.id.etRating);
        etDescripton = (EditText) v.findViewById(R.id.etDesciption);
        imvPicture = (ImageView) v.findViewById(R.id.imvPicture);
        btnSumbit = (Button) v.findViewById(R.id.btnSubmit);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnAddPoster = (Button) v.findViewById(R.id.btnAddPoster);
        btnSumbit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnAddPoster.setOnClickListener(this);
        if(getArguments()!=null){
        if(getArguments().getBoolean("check")) {
                mMovie = (MovieModel) getArguments().getSerializable(Constants.sFragmentBundleData);
                etTitle.setText(mMovie.getTitle());
                etGenre.setText(mMovie.getGenre());
                etStarcast.setText(mMovie.getActors());
                etDuration.setText("" + mMovie.getLength());
                etRating.setText("" + mMovie.getRating());
                etDescripton.setText(mMovie.getDescription());
                if (mMovie.getImageUrl().contains("http")) {
                    Picasso.get().load(mMovie.getImageUrl()).error(R.drawable.help_icon).placeholder(R.drawable.help_icon).noFade()
                            .into(imvPicture);
                } else {
                    imvPicture.setImageURI(Uri.parse(mMovie.getImageUrl()));
                }
        }
        }
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit: {
                if (etTitle.getText().toString().trim().equals("")) {
                   Snackbar.make(v, "Enter Title!", Snackbar.LENGTH_SHORT).show();

                } else if (etGenre.getText().toString().trim().equals("")) {
                    Snackbar.make(v, "Enter Genre!", Snackbar.LENGTH_SHORT).show();

                } else if (etStarcast.getText().toString().trim().equals("")) {
                    Snackbar.make(v, "Enter StarCast!", Snackbar.LENGTH_SHORT).show();

                } else if (etDuration.getText().toString().trim().equals("")) {
                    Snackbar.make(v, "Enter Duration!", Snackbar.LENGTH_SHORT).show();

                } else if (etRating.getText().toString().trim().equals("")) {
                    Snackbar.make(v, "Enter Rating!", Snackbar.LENGTH_SHORT).show();

                }else if (Double.parseDouble(etRating.getText().toString().trim())>5) {
                    Snackbar.make(v, "Invalid Rating!", Snackbar.LENGTH_SHORT).show();

                }else if (etDescripton.getText().toString().trim().equals("")) {
                    Snackbar.make(v, "Enter Description!", Snackbar.LENGTH_SHORT).show();

                }else{
                    if(getArguments()!=null){
                    if(getArguments().getBoolean("check")) {
                        mMovie.setTitle(etTitle.getText().toString().trim());
                        mMovie.setGenre(etGenre.getText().toString().trim());
                        mMovie.setActors(etStarcast.getText().toString().trim());
                        mMovie.setLength(Integer.parseInt(etDuration.getText().toString().trim()));
                        mMovie.setRating(Float.parseFloat(etRating.getText().toString().trim()));
                        mMovie.setDescription(etDescripton.getText().toString().trim());
                        if (u != null) {
                            mMovie.setImageUrl("" + u);
                        }
                    }
                    }else {

                        MovieModel objMovie = new MovieModel();
                        objMovie.setTitle(etTitle.getText().toString().trim());
                        objMovie.setGenre(etGenre.getText().toString().trim());
                        objMovie.setActors(etStarcast.getText().toString().trim());
                        objMovie.setLength(Integer.parseInt(etDuration.getText().toString().trim()));
                        objMovie.setRating(Float.parseFloat(etRating.getText().toString().trim()));
                        objMovie.setDescription(etDescripton.getText().toString().trim());
                        objMovie.setImageUrl("" + u);
                        System.out.println("............111..." + MoviesActivity.alMovies.size());
                        MoviesActivity.alMovies.add(objMovie);
                        System.out.println("............222..." + MoviesActivity.alMovies.size());
                    }
                }
                SharedPreferenceUtility.storeMovies(getActivity(),MoviesActivity.alMovies);

                getActivity().startActivity(new Intent(getActivity(),MoviesActivity.class));
                break;
            }
            case R.id.btnCancel: {
                getActivity().startActivity(new Intent(getActivity(),MoviesActivity.class));
                break;
            }
            case R.id.btnAddPoster: {
                Intent intent =  new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 101);
                break;
            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        u=  data.getData();
        imvPicture.setImageURI(u);
        u=   getImageUrlWithAuthority(getActivity(),u);
    }

    public static Uri getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                System.out.println("....111.................."+uri);
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }
}
