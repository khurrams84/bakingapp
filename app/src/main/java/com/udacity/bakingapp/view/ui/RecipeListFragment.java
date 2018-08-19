package com.udacity.bakingapp.view.ui;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.service.model.Recipe;
import com.udacity.bakingapp.view.adapter.RecipeListAdapter;
import com.udacity.bakingapp.viewmodel.RecipeListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeListFragment extends Fragment implements RecipeListAdapter.ItemClickListener {

    @BindView(R.id.rv_recipes)
    RecyclerView mRecyclerView;

    private List<Recipe> recipeList;
    private RecipeListAdapter recipeListAdapter;
    private Unbinder unbinder;
    private RecipeListViewModel recipeListViewModel;

    @Nullable private DataDownloaderIdlingResource mDataDownloaderIdlingResource;
    private RecipeFragmentListener recipeListFragmentListener;

    public RecipeListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        setIdlingResourceStatus(false);

        setData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void setData() {


        recipeListAdapter = new RecipeListAdapter(getActivity(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(recipeListAdapter);

        recipeListViewModel = ViewModelProviders.of((AppCompatActivity) getActivity()).get(RecipeListViewModel.class);

        observeViewModel(recipeListViewModel);


    }

    private void observeViewModel(RecipeListViewModel recipeListViewModel) {
        recipeListViewModel.getRecipeList().observe((AppCompatActivity) getActivity(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipeList) {
                if (recipeList != null) {
                    recipeListAdapter.setRecipes(recipeList);
                    setIdlingResourceStatus(true);

                }
            }
        });
    }

    @Override
    public void onItemClickListener(Recipe recipe) {
        //Toast mToast = Toast.makeText(getActivity(), "clicked", Toast.LENGTH_LONG);
        //mToast.show();
        Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("SelectedRecipe", recipe);
        startActivity(intent);
    }

    @Override
    public void onAddWidgetClickListener(Recipe recipe) {
        // Generate Widget Text Data
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            result.append("- ");
            result.append(recipe.getIngredients().get(i).getIngredient());
            result.append(" (");
            result.append(recipe.getIngredients().get(i).getQuantity());
            result.append(" ");
            result.append(recipe.getIngredients().get(i).getMeasure());
            result.append(").");
            if (i != (recipe.getIngredients().size() - 1)) {
                result.append("\n");
            }

            // Populate / Updating Widgets
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(), RecipeWidget.class));
            if (appWidgetIds.length == 0) {
                Toast.makeText(getActivity(), "Please make a home screen widget first!", Toast.LENGTH_SHORT).show();
            } else {
                for (int j = 0; j < appWidgetIds.length; i++) {
                    RecipeWidget.updateAppWidget(getActivity(), appWidgetManager, appWidgetIds[j], recipe.getName() + " - Ingredients", result.toString());
                    Toast.makeText(getActivity(), "Widget Added!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        recipeListFragmentListener = (RecipeFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recipeListFragmentListener = null;
    }

    public interface RecipeFragmentListener {
        void onIdlingResourceStatusChanged(boolean isIdle);
    }


    public void setIdlingResourceStatus(boolean isIdle) {
        recipeListFragmentListener.onIdlingResourceStatusChanged(isIdle);
    }

}
