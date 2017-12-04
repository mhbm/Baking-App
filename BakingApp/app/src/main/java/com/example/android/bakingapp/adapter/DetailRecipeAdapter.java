package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.RecipeModel;
import com.example.android.bakingapp.data.StepModel;

import java.util.ArrayList;

/**
 * Created by lsitec101.macedo on 16/11/17.
 */

public class DetailRecipeAdapter extends RecyclerView.Adapter<DetailRecipeAdapter.RecyclerViewHolder> {

    final private ListItemClickListener mListItemClickListener;
    ArrayList<StepModel> mStepRecipe;
    private String recipeName;

    public DetailRecipeAdapter(ListItemClickListener listener) {
        mListItemClickListener = listener;
    }

    public void setRecipeData(ArrayList<RecipeModel> recipe, Context context) {
        mStepRecipe = recipe.get(0).getSteps();
        recipeName = recipe.get(0).getName();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.activity_detail_recipe;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(mStepRecipe.get(position).getId() + ". " + mStepRecipe.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {

        return mStepRecipe != null ? mStepRecipe.size() : 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(ArrayList<StepModel> stepRecipe, int clickItemIndex, String recipeName);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textRecyclerView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            textRecyclerView = itemView.findViewById(R.id.tv_recipe_ingredients);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(mStepRecipe, clickedPosition, recipeName);
        }

    }

}
