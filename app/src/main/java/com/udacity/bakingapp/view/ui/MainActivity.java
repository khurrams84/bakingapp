package com.udacity.bakingapp.view.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.bakingapp.R;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.RecipeFragmentListener{

    private AtomicBoolean runningTest;

    @Nullable
    private DataDownloaderIdlingResource mIdlingResource;

    @Nullable
    @VisibleForTesting
    public DataDownloaderIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new DataDownloaderIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //DataDownloaderIdlingResource idlingResource = getIdlingResource();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
            //Log.d("SetIdlingStatus", String.valueOf(false));
        } else {
            //Log.d("SetIdlingStatus", "Null");
        }


        RecipeListFragment fragment = new RecipeListFragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container,fragment,"asdf");
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void onIdlingResourceStatusChanged(boolean isIdle) {
        //DataDownloaderIdlingResource idlingResource = getIdlingResource();
        if (mIdlingResource != null) {
            //Log.d("SetIdlingStatus", String.valueOf(isIdle));
            mIdlingResource.setIdleState(isIdle);
        } else {
            //Log.d("SetIdlingStatus", "Null");
        }
    }
}
