package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.RecipeModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lsitec101.macedo on 14/11/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    private final ListItemClickListener mOnLickListener;
    ArrayList<RecipeModel> mRecipes;
    Context mContext;

    public RecipeAdapter(ListItemClickListener listener) {
        mOnLickListener = listener;
    }

    public void setRecipeData(ArrayList<RecipeModel> recipeModel, Context context) {
        mRecipes = recipeModel;
        mContext = context;
        notifyDataSetChanged();
    }

    @Override
    public RecipeAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.grid_item_layout;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(mRecipes.get(position).getName());
        String imageUrl = mRecipes.get(position).getImage();

        if (imageUrl != "") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builtUri).into(holder.imageRecyclerView);
        }

    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        } else {
            return mRecipes.size();
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(RecipeModel clickedItemIndex);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textRecyclerView;
        ImageView imageRecyclerView;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            textRecyclerView = itemView.findViewById(R.id.title_recipe);
            imageRecyclerView = itemView.findViewById(R.id.recipe_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnLickListener.onListItemClick(mRecipes.get(clickedPosition));
        }

    }
}
