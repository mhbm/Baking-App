package com.example.android.bakingapp.fragment;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingapp.data.RecipeModel;

import java.util.ArrayList;

/**
 * Created by lsitec101.macedo on 16/11/17.
 */

public class DetailRecipeFragment extends Fragment {

    ArrayList<RecipeModel> recipe;
    String recipeName;
    RecyclerView mRecyclerView;
    final String RECIPESTEP = "RecipeStep";


//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        recipe = new ArrayList<>();
//
//        if (savedInstanceState != null) {
//            recipe = savedInstanceState.getParcelableArrayList(RECIPESTEP);
//        } else {
//            recipe = getArguments().getParcelableArrayList(RECIPESTEP);
//        }
//
//        ArrayList<IngredientModel> ingredientFromRecipe = recipe.get(0).getIngredients();
//        recipeName = recipe.get(0).getName();
//
//
//    }
}
