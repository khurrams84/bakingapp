package com.udacity.bakingapp.service.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.udacity.bakingapp.service.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeRepository {
    private RecipeService recipeService;
    private static RecipeRepository recipeRepository;


    private RecipeRepository(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecipeService.HTTPS_API_RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeService = retrofit.create(RecipeService.class);
    }

    public static RecipeRepository getInstance() {
        if (recipeRepository == null) {
            if (recipeRepository == null) {
                recipeRepository = new RecipeRepository();
            }
        }
        return recipeRepository;
    }

    public LiveData<List<Recipe>> getRecipeList() {
        final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();

        recipeService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });

        return data;
    }

}
