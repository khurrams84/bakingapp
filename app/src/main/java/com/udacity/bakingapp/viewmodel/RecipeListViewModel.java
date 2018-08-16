package com.udacity.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.bakingapp.service.model.Recipe;
import com.udacity.bakingapp.service.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private final LiveData<List<Recipe>> recipeList;
    //private final RecipeRepository recipeRepository;

    public RecipeListViewModel(Application application) {
        super(application);

        //recipeRepository = new RecipeRepository();

        recipeList = RecipeRepository.getInstance().getRecipeList();
    }

    public LiveData<List<Recipe>> getRecipeList() {
        return recipeList;
    }

}
