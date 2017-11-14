package com.example.android.bakingapp.retrofit;

import com.example.android.bakingapp.data.RecipeModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lsitec101.macedo on 14/11/17.
 */

public interface RecipeRetroFit {
    @GET("baking.json")
    Call<ArrayList<RecipeModel>> getRecipe();
}
