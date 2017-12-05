package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeAdapter;
import com.example.android.bakingapp.data.RecipeModel;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    static String ALL_RECIPES = "All_Recipes";
    static String SELECTED_RECIPES = "Selected_Recipes";
    static String SELECTED_STEPS = "Selected_Steps";
    static String SELECTED_INDEX = "Selected_Index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar menuToolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_title));

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
