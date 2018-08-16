package com.udacity.bakingapp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.service.model.Ingredient;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientListViewHolder> {

    //final private ItemClickListener mItemClickListener;

    private List<Ingredient> mIngredients;
    private Context mContext;

    public IngredientListAdapter(Context context) {
        mContext = context;
        //mItemClickListener = listener;
    }

    @Override
    public IngredientListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.ingredient_list_item, parent, false);

        return new IngredientListAdapter.IngredientListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(IngredientListViewHolder holder, int position) {

        Ingredient ingredient = mIngredients.get(position);
        String IngredientName = ingredient.getIngredient();
        String IngredientQuantityandMeasure = ingredient.getQuantity() + ingredient.getMeasure();

        holder.tvIngredient.setText(IngredientName);
        holder.tvQuantity.setText(IngredientQuantityandMeasure);

    }



    @Override
    public int getItemCount() {
        if (mIngredients == null) {
            return 0;
        }
        return mIngredients.size();
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }


    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    //public interface ItemClickListener {
    //    void onItemClickListener(Ingredient ingredient);
    //}


    // Inner class for creating ViewHolders
    class IngredientListViewHolder extends RecyclerView.ViewHolder {

        TextView tvIngredient;
        TextView tvQuantity;


        public IngredientListViewHolder(View itemView) {
            super(itemView);

            tvIngredient = itemView.findViewById(R.id.tvIngredient);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            //itemView.setOnClickListener(this);
        }

        //@Override
        //public void onClick(View view) {
        //    Ingredient ingredient = mIngredients.get(getAdapterPosition());
        //    mItemClickListener.onItemClickListener(ingredient);
        //}
    }
}