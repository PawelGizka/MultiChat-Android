package com.pgizka.gsenger.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.pgizka.gsenger.R;
import com.pgizka.gsenger.mainView.MainActivity;
import com.pgizka.gsenger.welcome.registration.RegistrationFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity  {

    private WelcomeActivityContent contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentFragment = getCurrentFragment(this);

        // If there's no fragment to use, we're done here.
        if (contentFragment == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Wire up the fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, (Fragment) contentFragment);
        fragmentTransaction.commit();
    }

    /**
     * Get the current fragment to display.
     *
     * This is the first fragment in the list that WelcomeActivityContent.shouldDisplay().
     *
     * @param context the application context.
     * @return the WelcomeActivityContent to display or null if there's none.
     */
    private static WelcomeActivityContent getCurrentFragment(Context context) {
        List<WelcomeActivityContent> welcomeActivityContents = getWelcomeFragments();

        for (WelcomeActivityContent fragment : welcomeActivityContents) {
            if (fragment.shouldDisplay(context)) {
                return fragment;
            }
        }

        return null;
    }

    /**
     * Get all WelcomeFragments for the WelcomeActivity.
     *
     * @return the List of WelcomeFragments.
     */
    private static List<WelcomeActivityContent> getWelcomeFragments() {
        return new ArrayList<WelcomeActivityContent>(Arrays.asList(
                new RegistrationFragment()
        ));
    }

    /**
     * Whether to display the WelcomeActivity.
     *
     * Decided whether any of the fragments need to be displayed.
     *
     * @param context the application context.
     * @return true if the activity should be displayed.
     */
    public static boolean shouldDisplay(Context context) {
        WelcomeActivityContent fragment = getCurrentFragment(context);
        if (fragment == null) {
            return false;
        }
        return true;
    }

    /**
     * The definition of a Fragment for a use in the WelcomeActivity.
     */
    public interface WelcomeActivityContent {
        /**
         * Whether the fragment should be displayed.
         *
         * @param context the application context.
         * @return true if the WelcomeActivityContent should be displayed.
         */
        public boolean shouldDisplay(Context context);
    }
}

