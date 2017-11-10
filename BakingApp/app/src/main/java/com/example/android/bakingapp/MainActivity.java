package com.example.android.bakingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.data.RecipeModel;
import com.example.android.bakingapp.utilities.NetworkUtils;
import com.example.android.bakingapp.utilities.OpenBakingJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int RECIPE_LOADER_ID = 0;
    private static final String TAG = MainActivity.class.getSimpleName();


    private ArrayList<RecipeModel> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchRecipeTask().execute();


    }

    private class FetchRecipeTask extends AsyncTask<RecipeModel, Object, ArrayList<RecipeModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<RecipeModel> doInBackground(RecipeModel... recipeModels) {
            URL recipeRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonRecipeResponse = NetworkUtils.getResponseFromHttpUrl(recipeRequestUrl);

                mRecipes = OpenBakingJsonUtils.getRecipeFromJson(jsonRecipeResponse);
                return mRecipes;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<RecipeModel> recipes) {


        }
    }
}
