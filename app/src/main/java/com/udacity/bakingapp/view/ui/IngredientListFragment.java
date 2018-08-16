package com.udacity.bakingapp.view.ui;

import android.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.service.model.Ingredient;
import com.udacity.bakingapp.service.model.Recipe;
import com.udacity.bakingapp.service.model.Step;
import com.udacity.bakingapp.view.adapter.IngredientListAdapter;
import com.udacity.bakingapp.view.adapter.StepListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IngredientListFragment extends Fragment implements StepListAdapter.StepClickListener {

    private Recipe mRecipe;

    @BindView(R.id.rvIngredient)
    RecyclerView mIngredientRecyclerView;

    @BindView(R.id.rvStep)
    RecyclerView mStepRecyclerView;


    private List<Ingredient> ingredientList;
    private IngredientListAdapter ingredientListAdapter;
    private List<Step> stepList;
    private StepListAdapter stepListAdapter;

    private Unbinder unbinder;

    private int currentStepIndex;
    private boolean isTablet;
    private StepFragmentListener fragmentListener;

    public IngredientListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        unbinder = ButterKnife.bind(this,view);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable("SaveStateRecipe");
            isTablet = savedInstanceState.getBoolean("SaveStateIsTablet");
        }
        else {
            Bundle bundle = this.getArguments();
            mRecipe = (Recipe)bundle.getParcelable("SelectedRecipe");
            isTablet = bundle.getBoolean("isTablet");
        }
        setData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (StepFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SaveStateRecipe", mRecipe);
        outState.putBoolean("SaveStateIsTablet", isTablet);
    }

    private void setData(){

        ingredientListAdapter = new IngredientListAdapter(getActivity());
        mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIngredientRecyclerView.setAdapter(ingredientListAdapter);
        ingredientListAdapter.setIngredients(mRecipe.getIngredients());

        stepListAdapter = new StepListAdapter(getActivity(), this);
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStepRecyclerView.setAdapter(stepListAdapter);
        stepListAdapter.setSteps(mRecipe.getSteps());

    }

    @Override
    public void onStepItemClick(Step step) {
        //Toast mToast = Toast.makeText(getActivity(), " step clicked", Toast.LENGTH_LONG);
        //mToast.show();
        currentStepIndex = mRecipe.getSteps().indexOf(step);
        if (isTablet) {
            fragmentListener.onStepClicked(step);
        } else {
            currentStepIndex = mRecipe.getSteps().indexOf(step);
            Intent intent = new Intent(getActivity(), StepDetailActivity.class);
            intent.putParcelableArrayListExtra("steps", (ArrayList<? extends Parcelable>) mRecipe.getSteps());
            intent.putExtra("currentStepIndex", currentStepIndex);
            startActivity(intent);
        }
    }

    public interface StepFragmentListener {
        void onStepClicked(Step step);
    }
}
