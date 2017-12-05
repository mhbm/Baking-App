package com.example.android.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.DetailRecipeAdapter;
import com.example.android.bakingapp.data.RecipeModel;
import com.example.android.bakingapp.data.StepModel;
import com.example.android.bakingapp.fragment.DetailRecipeFragment;

import java.util.ArrayList;

public class DetailRecipeActivity extends AppCompatActivity implements DetailRecipeAdapter.ListItemClickListener{


    static String SELECTED_RECIPES="Selected_Recipes";

    RecipeModel recipeModel = new RecipeModel();

    DetailRecipeFragment detailRecipeFragment;

    private static final String TAG = DetailRecipeActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        recipeModel = getIntent().getParcelableExtra(SELECTED_RECIPES);

        if (getSupportFragmentManager().findFragmentByTag(SELECTED_RECIPES) == null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(SELECTED_RECIPES, recipeModel);
            detailRecipeFragment = new DetailRecipeFragment();
            detailRecipeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, detailRecipeFragment, SELECTED_RECIPES)
                    .addToBackStack(null)
                    .commit();
        }



        recipeModel = getIntent().getParcelableExtra(SELECTED_RECIPES);
        System.out.println(recipeModel.getIngredients().get(1).getQuantity());


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }




    @Override
    public void onListItemClick(ArrayList<StepModel> stepModel, int clickItemIndex, String recipeName) {

    }
}
