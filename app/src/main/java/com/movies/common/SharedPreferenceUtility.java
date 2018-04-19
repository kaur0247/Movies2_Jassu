package com.movies.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.movies.Constants;
import com.movies.model.MovieModel;

import java.util.ArrayList;
import java.util.Arrays;

public class SharedPreferenceUtility {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor edior;

    public static void initSharedPreferences(Context pContext) {
        sp = pContext.getSharedPreferences(Constants.sSharedPreferences, Context.MODE_PRIVATE);
    }

    public static void initEditor(Context pContext) {
        if (sp == null) {
            initSharedPreferences(pContext);
        }
        edior = sp.edit();
    }

    public static void storeMovies(Context pContext, ArrayList<MovieModel> pMoviesList) {
        if (edior == null) {
            initEditor(pContext);
        }
        Gson gson = new Gson();
        String json = gson.toJson(pMoviesList);
        edior.putString(Constants.sKeyMoviesData, json);
        edior.apply();
    }

    public static ArrayList<MovieModel> getMovies(Context pContext) {
        ArrayList<MovieModel> al = new ArrayList<>();
        if (sp == null) {
            initSharedPreferences(pContext);
        }
        if (sp.contains(Constants.sKeyMoviesData)) {
            MovieModel[] arrayListIndexes = new Gson().fromJson
                    (sp.getString(Constants.sKeyMoviesData, null), MovieModel[].class);
            al = new ArrayList<>(Arrays.asList(arrayListIndexes));
        }
        return al;
    }
}
