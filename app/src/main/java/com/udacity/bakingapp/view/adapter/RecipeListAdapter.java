package com.udacity.bakingapp.view.adapter;

import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.service.model.Recipe;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    final private ItemClickListener mItemClickListener;

    private List<Recipe> mRecipes;
    private Context mContext;

    public RecipeListAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {

        Recipe recipe = mRecipes.get(position);
        String recipeName = recipe.getName();
        int recipeServing = recipe.getServings();

        holder.tvRecipeName.setText(recipeName);
        holder.tvRecipeServing.setText(String.valueOf(recipeServing));

    }



    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        }
        return mRecipes.size();
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }


    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(Recipe recipe);
        void onAddWidgetClickListener(Recipe recipe);
    }


    // Inner class for creating ViewHolders
    class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRecipeName;
        TextView tvRecipeServing;
        Button btn_favorite;

        public RecipeListViewHolder(View itemView) {
            super(itemView);

            tvRecipeName = itemView.findViewById(R.id.recipe_name);
            tvRecipeServing = itemView.findViewById(R.id.recipe_servings);
            btn_favorite = itemView.findViewById(R.id.btn_favorite);
            itemView.setOnClickListener(this);
            btn_favorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe recipe = mRecipes.get(getAdapterPosition());
            if(view.getId() == btn_favorite.getId())
            {
                mItemClickListener.onAddWidgetClickListener(recipe);
            }
            else
            {
                mItemClickListener.onItemClickListener(recipe);
            }
        }
    }
}
