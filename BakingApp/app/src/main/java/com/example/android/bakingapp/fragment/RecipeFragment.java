package com.example.android.bakingapp.fragment;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.ui.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeAdapter;
import com.example.android.bakingapp.data.RecipeModel;
import com.example.android.bakingapp.retrofit.RecipeRetroFit;
import com.example.android.bakingapp.retrofit.RetroFitBulder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lsitec101.macedo on 14/11/17.
 */

public class RecipeFragment extends Fragment {

    final static String GETRECIPES = "getRecipesFromRetroFit";
    private final String TAG = RecipeFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    RecipeRetroFit mRecipeRetrofit;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_recipe);
        final RecipeAdapter recipeAdapter = new RecipeAdapter((MainActivity) getActivity());
        mRecyclerView.setAdapter(recipeAdapter);

        if (rootView.getTag() != null && rootView.getTag().equals("phone-land")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 5);
            mRecyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }

        mRecipeRetrofit = RetroFitBulder.Retrieve();
        Call<ArrayList<RecipeModel>> recipesJSON = mRecipeRetrofit.getRecipe();

        recipesJSON.enqueue(new Callback<ArrayList<RecipeModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeModel>> call, Response<ArrayList<RecipeModel>> response) {
                Integer status = response.code();

                ArrayList<RecipeModel> recipesFromJson = response.body();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(GETRECIPES, recipesFromJson);

                recipeAdapter.setRecipeData(recipesFromJson, getContext());

            }

            @Override
            public void onFailure(Call<ArrayList<RecipeModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        return rootView;


    }
}
