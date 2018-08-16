package com.udacity.bakingapp.view.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.service.model.Recipe;
import com.udacity.bakingapp.service.model.Step;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements IngredientListFragment.StepFragmentListener {

    public static final String RECIPE_KEY = "recipe_k";

    private Recipe mRecipe;
    private boolean mTwoPane;


    //recipe_detail_content_view
    //recipe_step_content_view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        IngredientListFragment ingredientListFragment = new IngredientListFragment();

        Intent intent = getIntent();
        mRecipe = (Recipe) intent.getParcelableExtra("SelectedRecipe");

        Bundle bundle = new Bundle();
        bundle.putParcelable("SelectedRecipe", mRecipe);


        FragmentManager manager = getFragmentManager();


        // Check for two pane mode
        if(findViewById(R.id.recipe_step_content_view) != null){
            mTwoPane = true;
            StepDetailFragment stepDetailFragment = new StepDetailFragment();

            Bundle bundle2 = new Bundle();
            //int currentStepIndex = mRecipe.getSteps();
            bundle2.putParcelable("step", mRecipe.getSteps().get(0));
            bundle2.putBoolean("isTablet", true);
            bundle.putBoolean("isTablet", true);

            stepDetailFragment.setArguments(bundle2);

            FragmentTransaction stepFragmentTransaction = manager.beginTransaction();
            stepFragmentTransaction.add(R.id.recipe_step_content_view,stepDetailFragment,"StepDetails");
            stepFragmentTransaction.commit();

            // In tablet mode set orientation to landscape
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

            /*fragment = new StepDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("step", steps.get(stepIndex));
            bundle.putBoolean("isTablet", false);
            fragment.setArguments(bundle);*/

            //transaction.add(R.id.recipe_step_content_view, stepDetailFragment, "StepDetails");
            //getSupportFragmentManager().beginTransaction()
            //        .replace(R.id.recipe_step_content_view,          StepDetailFragment,"StepDetails")
            //        .commit();


        } else {
            mTwoPane = false;
        }


        ingredientListFragment.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.recipe_detail_content_view,ingredientListFragment,"RecipeDetails");
        //transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onStepClicked(Step step) {
        FragmentManager manager = getFragmentManager();
        StepDetailFragment stepDetailFragment = new StepDetailFragment();

        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("step", step);
        bundle2.putBoolean("isTablet", mTwoPane);

        stepDetailFragment.setArguments(bundle2);

        FragmentTransaction stepFragmentTransaction = manager.beginTransaction();
        stepFragmentTransaction.replace(R.id.recipe_step_content_view,stepDetailFragment,"StepDetails");
        stepFragmentTransaction.commit();
    }
}
