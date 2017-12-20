package com.example.android.bakingapp.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.DetailRecipeAdapter;
import com.example.android.bakingapp.data.RecipeModel;
import com.example.android.bakingapp.ui.DetailRecipeActivity;

/**
 * Created by lsitec101.macedo on 16/11/17.
 */

public class DetailRecipeFragment extends Fragment {

    static String SELECTED_RECIPES = "Selected_Recipes";
    private final String TAG = DetailRecipeFragment.class.getSimpleName();
    RecyclerView mRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail_step_description, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_recipe_step_description);


        final DetailRecipeAdapter recipeAdapter = new DetailRecipeAdapter((DetailRecipeActivity) getActivity());

        mRecyclerView.setAdapter(recipeAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);


        Bundle bundle = this.getArguments();

        if (bundle != null) {
            recipeAdapter.setRecipeData((RecipeModel) bundle.getParcelable(SELECTED_RECIPES), getContext());
        }

        return rootView;

    }

}
