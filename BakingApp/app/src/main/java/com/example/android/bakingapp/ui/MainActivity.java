package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeAdapter;
import com.example.android.bakingapp.data.RecipeModel;
import com.example.android.bakingapp.retrofit.RecipeRetroFit;
import com.example.android.bakingapp.retrofit.RetroFitBulder;
import com.example.android.bakingapp.utilitaries.SimpleIdlingResource;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    final static String GETRECIPES = "getRecipesFromRetroFit";
    private static final String TAG = MainActivity.class.getSimpleName();
    static String SELECTED_RECIPES = "Selected_Recipes";
    RecyclerView mRecyclerView;
    RecipeRetroFit mRecipeRetrofit;


    private SimpleIdlingResource mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar menuToolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_title));

        mRecyclerView = findViewById(R.id.rv_recipe);
        final RecipeAdapter recipeAdapter = new RecipeAdapter(this);

        mRecyclerView.setAdapter(recipeAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecipeRetrofit = RetroFitBulder.Retrieve();
        Call<ArrayList<RecipeModel>> recipesJSON = mRecipeRetrofit.getRecipe();
        mIdlingResource = new SimpleIdlingResource();
        mIdlingResource.setIdleState(false);
        recipesJSON.enqueue(new Callback<ArrayList<RecipeModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeModel>> call, Response<ArrayList<RecipeModel>> response) {

                ArrayList<RecipeModel> recipesFromJson = response.body();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(GETRECIPES, recipesFromJson);

                recipeAdapter.setRecipeData(recipesFromJson, getBaseContext());
                mIdlingResource.setIdleState(true);
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


        getIdlingResource();

    }

    @Override
    public void onListItemClick(RecipeModel selectedItemIndex) {

        Bundle selectedRecipeBundle = new Bundle();
//        ArrayList<RecipeModel> selectedRecipe = new ArrayList<>();
//        selectedRecipe.add(selectedItemIndex);
        selectedRecipeBundle.putParcelable(SELECTED_RECIPES, selectedItemIndex);
        final Intent intent = new Intent(this, DetailRecipeActivity.class);
        intent.putExtras(selectedRecipeBundle);
        startActivity(intent);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
