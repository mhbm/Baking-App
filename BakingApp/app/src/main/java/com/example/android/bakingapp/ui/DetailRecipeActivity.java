package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.DetailRecipeAdapter;
import com.example.android.bakingapp.data.RecipeModel;
import com.example.android.bakingapp.data.StepModel;
import com.example.android.bakingapp.fragment.DetailRecipeFragment;

import java.util.ArrayList;


public class DetailRecipeActivity extends AppCompatActivity implements DetailRecipeAdapter.ListItemClickListener {


    private static final String TAG = DetailRecipeActivity.class.getSimpleName();
    static String SELECTED_RECIPES = "Selected_Recipes";
    static String SELECTED_STEP = "Selected_STEP";
    static String SELECTED_POSITION = "Selected_POSITION";
    RecipeModel recipeModel = new RecipeModel();
    DetailRecipeFragment detailRecipeFragment;
    TextView mTextViewIngredient;

    private boolean tabletLayout;

    private Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tabletLayout = getResources().getBoolean(R.bool.tabletLayout);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        mTextViewIngredient = findViewById(R.id.tv_recipe_ingredients);

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

            setIngredientList();
        }


        if (tabletLayout) {
            setContentView(R.layout.activity_step_view);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        setIngredientList();
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setIngredientList();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void setIngredientList() {
        String aux = "";
        for (int i = 0; i < recipeModel.getIngredients().size(); i++) {
            aux += "Ingredient : " + recipeModel.getIngredients().get(i).getIngredient() + "; Measure " + recipeModel.getIngredients().get(i).getMeasure() + "; Quantity: " + recipeModel.getIngredients().get(i).getQuantity();

            if (i < recipeModel.getIngredients().size() - 1) {
                aux += "\n\n";
            }
        }

        mTextViewIngredient.setText(aux);
    }

    @Override
    public void onListItemClick(ArrayList<StepModel> stepModel, int clickItemIndex, String recipeName) {
        Bundle selectedRecipeBundle = new Bundle();
        selectedRecipeBundle.putParcelableArrayList(SELECTED_STEP, stepModel);
        selectedRecipeBundle.putInt(SELECTED_POSITION, clickItemIndex);

        if (!tabletLayout) {
            final Intent intent = new Intent(this, StepViewActivity.class);
            intent.putExtras(selectedRecipeBundle);
            startActivity(intent);
        }

    }
}
