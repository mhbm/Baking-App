package com.example.android.bakingapp.utilities;

import com.example.android.bakingapp.data.IngredientModel;
import com.example.android.bakingapp.data.RecipeModel;
import com.example.android.bakingapp.data.StepModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mateus Macedo on 09/11/17.
 */

public class OpenBakingJsonUtils {

    public static ArrayList<RecipeModel> getRecipeFromJson(String recipeJsonString) throws JSONException {

        ArrayList<RecipeModel> recipes = new ArrayList<>();

        JSONObject recipeJson = new JSONObject(recipeJsonString);

        JSONArray recipeArray = recipeJson.getJSONArray("results");





        for (int i = 0; i < recipeArray.length(); i++) {

            RecipeModel recipeGet = new RecipeModel();

            JSONObject recipe = recipeArray.getJSONObject(i);

            System.out.println("asuhehuseauhseauhsuhause");

            recipeGet.setId(recipe.getString("id"));
            recipeGet.setName(recipe.getString("name"));
            recipeGet.setName(recipe.getString("servings"));
            recipeGet.setName(recipe.getString("image"));

            JSONArray ingredientsArray = recipeJson.getJSONArray("ingredients");

            for (int j = 0; j < ingredientsArray.length(); j++) {
                JSONObject ingredients = ingredientsArray.getJSONObject(j);
                IngredientModel ingredientModel = new IngredientModel();
                ingredientModel.setQuantity(ingredients.getString("quantity"));
                ingredientModel.setMeasure(ingredients.getString("measure"));
                ingredientModel.setQuantity(ingredients.getString("ingredient"));

                recipeGet.setIngredients(ingredientModel);
            }

            JSONArray stepsArray = recipeJson.getJSONArray("steps");

            for (int j = 0; j < stepsArray.length(); j++) {
                JSONObject steps = stepsArray.getJSONObject(j);
                StepModel stepModel = new StepModel();

                stepModel.setId(steps.getString("id"));
                stepModel.setShortDescription(steps.getString("shortDescription"));
                stepModel.setDescription(steps.getString("description"));
                stepModel.setVideoURL(steps.getString("videoURL"));
                stepModel.setThumbnailURL(steps.getString("thumbnailURL"));

                recipeGet.setSteps(stepModel);
            }
            System.out.println("erro");
            System.out.println(recipeGet.getName());
            recipes.add(recipeGet);
        }

        return recipes;
    }


}
