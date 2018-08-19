package com.udacity.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.bakingapp.view.ui.DetailActivity;
import com.udacity.bakingapp.view.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;


@RunWith(AndroidJUnit4.class)
public class BakingRecipesTests {

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = activityTestRule.getActivity().getIdlingResource();
        //Espresso.registerIdlingResources(mIdlingResource);
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void clickRecyclerViewItemHasIntentWithAKey() {
        //Checks if the key is present
        Intents.init();

        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasExtraWithKey(DetailActivity.RECIPE_KEY));

        Intents.release();

    }

    @Test
    public void clickOnRecyclerViewItem_opensRecipeInfoActivity() {


        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


        onView(withId(R.id.rvIngredient)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            //Espresso.unregisterIdlingResources(mIdlingResource);
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
