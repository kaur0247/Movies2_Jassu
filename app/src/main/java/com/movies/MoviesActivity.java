package com.movies;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.movies.fragments.AddNewMovie;
import com.movies.fragments.MovieDetailsFragment;
import com.movies.model.MovieModel;
import com.movies.common.SharedPreferenceUtility;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity {
    private ListView lvMovies;
    public static  ArrayList<MovieModel> alMovies;

    private MoviesAdapter mMoviesAdapter;
    private FrameLayout mFrameLayout;
    private FloatingActionButton mFloatingAddButton;
    private MovieModel mMovieToBeUpdatedOrAdded;
    private View mView;
    private ImageView imgPoster;


    @Override
    public void onBackPressed() {
        if (mFrameLayout.getVisibility() == View.VISIBLE) {
            showView(true);
        } else {
            super.onBackPressed();
        }
    }
    private void showView(boolean flag) {
        if(flag) {
            mFloatingAddButton.setVisibility(View.VISIBLE);
            mFrameLayout.setVisibility(View.GONE);
            lvMovies.setVisibility(View.VISIBLE);
            MoviesActivity.this.setTitle(MoviesActivity.this.getString(R.string.title_movies));
            if (MoviesActivity.this.getSupportActionBar() != null) {
                MoviesActivity.this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }else{
            mFloatingAddButton.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
            lvMovies.setVisibility(View.GONE);
            MoviesActivity.this.setTitle(MoviesActivity.this.getString(R.string.title_details));
            if (MoviesActivity.this.getSupportActionBar() != null) {
                MoviesActivity.this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        mView = findViewById(R.id.root_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        XmlPullParserFactory pullParserFactory;

        try {
            Bundle b = getIntent().getExtras();

            if(b!=null){
                if(b.getBoolean("size")) {
                  Snackbar.make(mView,"No Movies Found",Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
                if (SharedPreferenceUtility.getMovies(this).size() > 0) {
                    alMovies = SharedPreferenceUtility.getMovies(this);
                    System.out.println(".....11555511.....");
                } else {
                    pullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = pullParserFactory.newPullParser();

                    InputStream in_s = getApplicationContext().getAssets().open("moviesfile.xml");
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in_s, null);

                    alMovies = parseXML(parser);
                    SharedPreferenceUtility.initEditor(MoviesActivity.this);
                    SharedPreferenceUtility.storeMovies(MoviesActivity.this, alMovies);
                }

            } catch(XmlPullParserException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        mView = findViewById(R.id.root_layout);
        lvMovies = findViewById(R.id.list_view_movies);
        mFrameLayout = findViewById(R.id.fragment_container);
        mFloatingAddButton = findViewById(R.id.fab_add);
        mMoviesAdapter = new MoviesAdapter(getApplicationContext(), alMovies);
        lvMovies.setAdapter(mMoviesAdapter);
        if (ActivityCompat.checkSelfPermission(MoviesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(MoviesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MoviesActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
            return;

        }
        mFloatingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showView(false);
                Fragment fragment = new AddNewMovie();
                getSupportActionBar().setTitle("Add New MovieModel");
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
        lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showView(false);
                Fragment fragment = new MovieDetailsFragment();
                Bundle bundle = new Bundle();

                bundle.putSerializable(Constants.sFragmentBundleData, alMovies.get(position));
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                showView(true);

                break;
            }
            case R.id.action_help: {
                showDialog();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MoviesActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);


        dialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }


    private ArrayList<MovieModel> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<MovieModel> moviesList = null;
        int eventType = parser.getEventType();
        MovieModel movie = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    moviesList = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("MovieModel")) {
                        movie = new MovieModel();
                    } else if (movie != null) {
                        if (name.equals("Title")) {
                            movie.setTitle(parser.nextText().trim());
                        } else if (name.equals("Actors")) {
                            movie.setActors(parser.nextText().trim());
                        } else if (name.equals("Rating")) {
                            movie.setRating(Float.parseFloat(parser.nextText()));
                        } else if (name.equals("Genre")) {
                            movie.setGenre(parser.nextText().trim());
                        } else if (name.equals("Length")) {
                            movie.setLength(Integer.parseInt(parser.nextText()));
                        } else if (name.equals("Description")) {
                            movie.setDescription(parser.nextText().trim());
                        } else if (name.equals("URL")) {
                            movie.setImageUrl(parser.getAttributeValue(null, "value"));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("MovieModel") && movie != null) {
                        moviesList.add(movie);
                    }
            }
            eventType = parser.next();
        }

        return moviesList;

    }











}

